package io.github.rodneyxr.coronacraft;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSources;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CoronaEntity {
    private Entity entity;

    // Attributes
    private boolean isInfected;
    private boolean hasPreExitingCondition;

    public CoronaEntity(Entity entity) {
        this.entity = entity;
        this.setInfected(false);
        int chance = Coronacraft.RNG.nextInt(100);
        if (chance < 25)
            this.hasPreExitingCondition = true;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    public void checkInfectedHealth(){
        double currentHP = entity.get(Keys.HEALTH).orElse(0.0);
        if (currentHP <= 1)
            entity.damage(2.0, DamageSources.FIRE_TICK);
    }

    public void infect(CoronaLevel level) {
        this.setInfected(true);
        PotionEffect coronaVirus = PotionEffect.builder()
                .duration(14 * 24000) // 14 minecraft days
                .amplifier(level.value)
                .particles(true)
                .potionType(PotionEffectTypes.POISON)
                .build();
        List<PotionEffect> potionEffects = entity.getOrElse(Keys.POTION_EFFECTS, new ArrayList<>(1));
        potionEffects.add(coronaVirus);
        entity.tryOffer(Keys.POTION_EFFECTS, potionEffects);

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
