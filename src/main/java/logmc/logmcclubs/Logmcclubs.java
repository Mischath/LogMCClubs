package logmc.logmcclubs;

import com.google.inject.Inject;
import com.google.inject.Injector;
import logmc.logmcclubs.commands.ClubCommand;
import logmc.logmcclubs.commands.helpers.CommandService;
import logmc.logmcclubs.data.ClubData;
import logmc.logmcclubs.data.ClubKeys;
import logmc.logmcclubs.facades.ClubFacade;
import logmc.logmcclubs.facades.ClubMessagingFacade;
import logmc.logmcclubs.services.ClubService;
import org.slf4j.Logger;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
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

    private static boolean init = false;

    @Inject
    private Logger logger;

    @Inject
    Injector spongeInjector;
    private Injector clubInjector;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Starting LogMC Clubs...");
    }

    private void init() {
        instance = this;

        components = new Components();
        clubInjector = spongeInjector.createChildInjector(new LogmcclubsModule());
        clubInjector.injectMembers(components);

        init = true;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        ClubKeys.CLUB_DATA_REGISTRATION = DataRegistration.builder()
                .dataClass(ClubData.class)
                .immutableClass(ClubData.Immutable.class)
                .builder(new ClubData.Builder())
                .name("Club")
                .id("club")
                .build();
    }

    private void start() {
        try {
            getCommandService().register(new ClubCommand(), this);
        } catch ( CommandService.AnnotatedCommandException e) {
            e.printStackTrace();
        }
    }

    @Listener(order = Order.EARLY)
    public void onInit(GameInitializationEvent event) {
        init();
    }

    @Listener
    public void onStart(GameInitializationEvent event) {
        if (init) {
            start();
        }
    }

    public Logger getLogger() {
        return logger;
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

    public static CommandService getCommandService() {
        return CommandService.getInstance();
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
