package logmc.logmcclubs.commands.helpers;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;

public interface ParameterizedCommand extends CommandExecutor {

    CommandElement[] getArguments();
}
