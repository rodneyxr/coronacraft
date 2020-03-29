package io.github.rodneyxr.coronacraft;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

public class EventListeners {
    @Listener
    public void onPlayerMove(MoveEntityEvent e, @Root Player player) {
//        Location<World> playerLocation = player.getLocation();
    }

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event) {
        CoronaEntity entity = Coronacraft.BANK.getOrCreate(event.getTargetEntity());
        if (entity.isInfected()) {
            entity.setInfected(false);
        }
    }
}
