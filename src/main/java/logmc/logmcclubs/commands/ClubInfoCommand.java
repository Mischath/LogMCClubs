package logmc.logmcclubs.commands;

import logmc.logmcclubs.Logmcclubs;
import logmc.logmcclubs.commands.helpers.*;
import logmc.logmcclubs.entities.Club;
import logmc.logmcclubs.facades.ClubFacade;
import logmc.logmcclubs.services.ClubService;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

@Aliases({"info"})
@Permission("logmcclubs.club.info")
@Description("Displays info for club")

public class ClubInfoCommand implements PlayerCommand, ParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, CommandContext args) throws CommandException {
        if (args == null) {
            Logmcclubs.getInstance().getClubFacade().printPlayerClub(source);
        } else {
            Club club = Logmcclubs.getInstance().getClubFacade().getClub(args.<String>getOne("club").get());
            Logmcclubs.getInstance().getClubFacade().printPlayerClub(source, club);
        }

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.string(Text.of("club")),
                GenericArguments.string(Text.of("player"))
        };
    }
}
