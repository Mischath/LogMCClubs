package logmc.logmcclubs.commands;

import logmc.logmcclubs.Logmcclubs;
import logmc.logmcclubs.commands.helpers.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

@Aliases({"create"})
@Permission("logmcclubs.club.create")
@Description("Creates a club")

public class ClubCreateCommand implements PlayerCommand, ParameterizedCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {
        Logmcclubs.getInstance().getClubFacade().createClub(source, args.<String>getOne("name").get());

        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.string(Text.of("name"))
        };
    }
}
