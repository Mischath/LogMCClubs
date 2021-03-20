package logmc.logmcclubs;

import com.google.inject.Inject;
import logmc.logmcclubs.commands.ClubCommand;
import logmc.logmcclubs.facades.ClubFacade;
import logmc.logmcclubs.facades.ClubMessagingFacade;
import logmc.logmcclubs.services.ClubService;
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
        },
        version = "1.0.0"
)
public class Logmcclubs {

    private static Logmcclubs instance;
    private Components components;

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Starting LogMC Clubs...");
    }

    @Listener
    public void onGameLoaded(GameLoadCompleteEvent event) {
        instance = this;

        loaded();
    }

    private void init() {
        instance = this;

        components = new Components();
    }

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

    public static Logmcclubs getInstance() {
        return instance;
    }

    public ClubFacade getClubFacade() {
        return components.clubFacade;
    }

    public ClubService getClubService() {
        return components.clubService;
    }

    public ClubMessagingFacade getClubMessagingFacade() {
        return components.clubMessagingFacade;
    }

    private static class Components {
        @Inject
        private ClubFacade clubFacade;

        @Inject
        private ClubService clubService;

        @Inject
        private ClubMessagingFacade clubMessagingFacade;
    }
}
