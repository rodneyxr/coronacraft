package io.github.rodneyxr.coronacraft;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

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

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
