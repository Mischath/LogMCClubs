package logmc.logmcclubs;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "logmcclubs",
        name = "LogMCClubs",
        description = "LogMC Clubs for Pixelmon",
        url = "https://logmc.eu",
        authors = {
                "Mischa"
        }
)
public class Logmcclubs {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
