package managers.commands;

import fileio.input.CommandInput;
import fileio.output.Output;

/**
 * An interface representing a command handler.
 * Classes implementing this interface are responsible for processing commands
 * and producing corresponding output.
 */
public interface CommandHandler {
    /**
     * Performs the specified command and returns the output.
     *
     * @param command The input command to be processed.
     * @return The output produced as a result of processing the command.
     * @throws IllegalStateException if the manager doesn't recognize the
     * command type
     * @see commands.CommandType
     */
    Output performCommand(CommandInput command);
}
