package io.github.rodneyxr.coronacraft;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.concurrent.TimeUnit;

@Plugin(
        id = "coronacraft",
        name = "Coronacraft",
        description = "Coronavirus simulator in Minecraft to promote awareness and social distancing.",
        url = "http://rodneyxr.github.io/coronacraft/",
        authors = {
                "rodneyxr",
                "magar832",
                "ideaxplatforming"
        }
)
public class Coronacraft {

    public static int INFECTION_DISTANCE = 6;

    // Where all the entities that we track are stored
    public static CoronaEntityBank BANK;

    @Inject
    private Logger logger;

    @Listener
    public void onServerInit(GameInitializationEvent event) {
        Sponge.getEventManager().registerListeners(this, new EventListeners());
        BANK = new CoronaEntityBank();
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        Player player = e.getTargetEntity();
        if (player.getName().equalsIgnoreCase("rodneyxr")) {
            CoronaEntity entity = BANK.getOrCreate(player);
            entity.infect(CoronaLevel.MILD);
        }
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        Task coronaTask = Task.builder().execute(() -> {
//          Sponge.getServer().getBroadcastChannel().send(Text.of("Checking for covid-19 in players..."));
            for (CoronaEntity host : BANK.getAll()) {
                // Skip if host is not a player or villager
                if (!host.isPlayer() && !host.isVillager())
                    continue;

                // If the player is not infected, they cannot infect anybody
                if (!host.isInfected())
                    continue;

                for (Entity otherEntity : host.getEntity().getNearbyEntities(INFECTION_DISTANCE)) {
                    CoronaEntity other = BANK.getOrCreate(otherEntity);

                    // If the host is not a player or a villager, they cannot be infected
                    if (!other.isPlayer() && !other.isVillager())
                        continue;

                    // Skip if entity is the player itself
                    if (host.getEntity() == otherEntity)
                        continue;

                    // If other entity is infected, skip since they cannot get infected again
                    if (other.isInfected())
                        continue;

                    Player hostPlayer = host.getPlayerOrNull();
                    Player otherPlayer = other.getPlayerOrNull();

                    // Infect the other entity
                    other.infect(CoronaLevel.SEVERE); // TODO: RNG for CoronaLevel

                    if (hostPlayer != null) {
                        if (other.isPlayer()) {
                            // Notify the host that they infected somebody
                            hostPlayer.sendMessage(Text.of(TextColors.DARK_GRAY, "You infected: " + otherPlayer.getName()));
                        } else if (other.isVillager()) {
                            // Notify the host that they have infected a villager
                            hostPlayer.sendMessage(Text.of(TextColors.DARK_GRAY, "You infected a villager!"));
                        }
                    }

                    if (otherPlayer != null) {
                        if (hostPlayer != null) {
                            // Notify the other player that they were infected by another player
                            otherPlayer.sendMessage(Text.of(TextColors.RED, "You were infected by: " + hostPlayer.getName()));
                        } else if (host.isVillager()) {
                            // Notify the other player that they were infected by a villager
                            otherPlayer.sendMessage(Text.of(TextColors.RED, "You were infected by a villager!"));
                        }
                    }
                }
            }
        })
                .async()
                .delay(1, TimeUnit.SECONDS)
                .interval(5, TimeUnit.SECONDS)
                .name("COVID-19 Scan").submit(this);
    }


}
