package logmc.logmcclubs.commands;

import logmc.logmcclubs.Logmcclubs;
import logmc.logmcclubs.commands.helpers.Aliases;
import logmc.logmcclubs.commands.helpers.Description;
import logmc.logmcclubs.commands.helpers.Permission;
import logmc.logmcclubs.commands.helpers.PlayerCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nonnull;

@Aliases({"list"})
@Permission("logmcclubs.club.list")
@Description("Lists the club")

public class ClubListCommand implements PlayerCommand{

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {
        Logmcclubs.getInstance().getClubFacade().printClubList(source);

        return CommandResult.success();
    }
}
