package logmc.logmcclubs.facades;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import logmc.logmcclubs.entities.Club;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class ClubMessagingFacade extends AbstractMessagingFacade{

    @Inject
    private ClubFacade clubFacade;

    public ClubMessagingFacade(){
        super("Club");
    }

    public void sendInfoToClub(Club club, Object... msg) {
        for (Player member : clubFacade.getOnlineClubMembers(club)) {
            info(member, msg);
        }
    }

    public void sendErrorToClub(Club club, Object... msg) {
        for (Player member : clubFacade.getOnlineClubMembers(club)) {
            error(member, msg);
        }
    }
}
