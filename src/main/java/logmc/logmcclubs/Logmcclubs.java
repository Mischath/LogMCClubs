package logmc.logmcclubs;

import com.google.inject.Inject;
import logmc.logmcclubs.commands.ClubCommand;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.command.spec.CommandSpec;


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

    private static Logmcclubs instance;

    @Inject
    private Logger logger;

    public void loaded() {
        registerCommands();
    }

    public void registerCommands() {
        CommandSpec clubCommandSpec = CommandSpec.builder()
                .description(Text.of("Show LogMC clubs commands."))
                .permission("logmcclubs.user.command.base")
                .executor(new ClubCommand())
                .build();

        Sponge.getCommandManager().register(instance, clubCommandSpec, "club", "cl");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Starting LogMC Clubs...");
    }

    @Listener
    public void onGameLoaded(GameLoadCompleteEvent event) {
        instance = this;

        loaded();
    }
}
