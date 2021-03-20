package logmc.logmcclubs.exceptions;

import logmc.logmcclubs.Logmcclubs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.User;

public class ClubCommandException extends CommandException {
    public ClubCommandException(Object... message) {
        super(
                Logmcclubs.getInstance().getClubMessagingFacade().formatError(message)
        );
    }

    public static ClubCommandException noClub() {
        return new ClubCommandException("You are not in a club.");
    }

    public static ClubCommandException notLeader() {
        return new ClubCommandException("You are not the club leader.");
    }

    public static ClubCommandException notInClub(User player) {
        return new ClubCommandException(player.getName(), " is not in your club.");
    }
}
