package io.github.rodneyxr.coronacraft;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class EventListeners {

    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        Entity entity = event.getTargetEntity();
        CoronaEntity coronaEntity = Coronacraft.BANK.getOrNull(entity);
        if (coronaEntity == null) {
            return;
        }

        // Remove from BANK
        Coronacraft.BANK.remove(entity);

        if (coronaEntity.isInfected()) {
            coronaEntity.cure();
        }
    }

    @Listener
    public void onEntityDamage(DamageEntityEvent event) {
        Entity entity = event.getTargetEntity();
        CoronaEntity coronaEntity = Coronacraft.BANK.getOrNull(entity);
        if (coronaEntity == null)
            return;

        if (coronaEntity.getCoronaLevel() == CoronaLevel.CRITICAL) {
            Optional<Double> health = entity.get(Keys.HEALTH);
            if (health.isPresent() && health.get() < 3) {
                int chance = Coronacraft.RNG.nextInt(100);
                if (chance < 68) {
                    // Death for entity
                    entity.tryOffer(Keys.HEALTH, 0.0);
                    if (coronaEntity.isPlayer()) {
                        Player player = coronaEntity.getPlayerOrNull();
                        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.LIGHT_PURPLE, player.getName() + " has died from COVID-19"));
                    } else {
                        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.LIGHT_PURPLE, "A villager has died from COVID-19"));
                    }
                }
            }
        }
    }
}
