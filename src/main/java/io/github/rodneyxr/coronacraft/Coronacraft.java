package io.github.rodneyxr.coronacraft;

import com.google.inject.Inject;
import com.google.inject.internal.cglib.core.$KeyFactory;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.Careers;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
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

    public static int INFECTION_DISTANCE = 10;

    @Inject
    private Logger logger;

    @Listener
    public void onServerInit(GameInitializationEvent event) {
        Sponge.getEventManager().registerListeners(this, new EventListeners());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Coronacraft has been loaded!");
    }

//    @Listener
//    public void onInit(GameInitializationEvent event) {
//    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        Player player = e.getTargetEntity();
        if (player.getName().equalsIgnoreCase("rodneyxr")) {
            // TODO: support infected key
            logger.info("" + player.supports(CoronaKeys.INFECTED));
            player.offer(CoronaKeys.INFECTED, true);
            Sponge.getServer().getBroadcastChannel().send(Text.of(player.getName() + " has been infected with COVID-19."));
        }
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        Task coronaTask = Task.builder().execute(() -> {
//          Sponge.getServer().getBroadcastChannel().send(Text.of("Checking for covid-19 in players..."));
            Collection<Player> players = Sponge.getServer().getOnlinePlayers();
            for (Player p : players) {
                boolean playerInfected = p.get(CoronaKeys.INFECTED).orElse(false);
//                logger.info(String.format("%s: %s", p.getName(), playerInfected));

                // If the player is not infected, they cannot infect anybody
                if (!playerInfected)
                    continue;

                for (Entity entity : p.getNearbyEntities(INFECTION_DISTANCE)) {
                    // Skip if p is the player itself
                    if (p == entity)
                        continue;

                    // Get infection status of other entity
                    boolean otherInfected = entity.get(CoronaKeys.INFECTED).orElse(false);

                    // If other entity is infected, skip since they cannot get infected again
                    if (otherInfected)
                        continue;

                    if (entity instanceof Player) {
                        Player other = (Player) entity;
                        // TODO: RNG
                        // Notify the player that they infected somebody
                        // DEBUG: p.sendMessage(Text.of(String.format("You infected: %s", other.getName())));

                        // Notify the other player that they were infected
                        other.offer(CoronaKeys.INFECTED, true);
                        other.sendMessage(Text.of(TextColors.RED, String.format("You were infected by: %s", p.getName())));
                    } else if (entity.getType().equals(EntityTypes.VILLAGER)) {
                        // This is a villager
                        // TODO: infect villagers
                        p.sendMessage(Text.of(TextColors.DARK_RED, "You cannot infect villagers yet!"));
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
