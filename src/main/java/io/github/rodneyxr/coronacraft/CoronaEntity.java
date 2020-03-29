package io.github.rodneyxr.coronacraft;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoronaEntity {
    private Entity entity;

    // Attributes
    private boolean isInfected;

    public CoronaEntity(Entity entity) {
        this.entity = entity;
        this.setInfected(false);
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    public void infect(CoronaLevel level) {
        this.setInfected(true);
        PotionEffect coronaVirus = PotionEffect.builder()
                .duration(14 * 24000) // 14 minecraft days
                .amplifier(level.value)
                .particles(false)
                .potionType(PotionEffectTypes.POISON)
                .build();
        List<PotionEffect> potionEffects = entity.getOrElse(Keys.POTION_EFFECTS, new ArrayList<>(1));
        potionEffects.add(coronaVirus);
        entity.offer(Keys.POTION_EFFECTS, potionEffects);

        // Notify the server if this was a player
        if (isPlayer()) {
            Sponge.getServer().getBroadcastChannel().send(Text.of(((Player) entity).getName() + " has been infected with a " + level.toString() + " case of COVID-19."));
        }
    }

    public boolean isPlayer() {
        return entity.getType().equals(EntityTypes.PLAYER);
    }

    public boolean isVillager() {
        return entity.getType().equals(EntityTypes.VILLAGER);
    }

    public Player getPlayerOrNull() {
        if (isPlayer())
            return (Player) entity;
        return null;
    }

    public UUID getUniqueId() {
        return this.entity.getUniqueId();
    }

    public Entity getEntity() {
        return this.entity;
    }

}
