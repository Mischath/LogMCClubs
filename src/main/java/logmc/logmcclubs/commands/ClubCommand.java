package logmc.logmcclubs.commands;

import logmc.logmcclubs.Logmcclubs;
import logmc.logmcclubs.commands.helpers.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;

import javax.annotation.Nonnull;

@Aliases("club")
@Children({
        ClubInviteCommand.class,
        ClubKickCommand.class,
        ClubLeaderCommand.class,
        ClubLeaveCommand.class,
        ClubDisbandCommand.class,
        ClubCreateCommand.class
})
@HelpCommand(title = "Club Help", command = "help")
@Permission("logmcclubs.club")
public class ClubCommand implements PlayerCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player src,@Nonnull CommandContext args) throws CommandException {
        Logmcclubs.getInstance().getClubFacade().printPlayerClub(src);

        return CommandResult.success();
    }
}
