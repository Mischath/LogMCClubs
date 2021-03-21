package logmc.logmcclubs;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import logmc.logmcclubs.data.ClubsManager;
import logmc.logmcclubs.facades.ClubFacade;
import logmc.logmcclubs.facades.ClubMessagingFacade;
import logmc.logmcclubs.services.ClubService;

public class LogmcclubsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClubFacade.class).in(Scopes.SINGLETON);
        bind(ClubService.class).in(Scopes.SINGLETON);
        bind(ClubMessagingFacade.class).in(Scopes.SINGLETON);
    }
}
