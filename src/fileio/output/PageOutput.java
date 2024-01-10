package fileio.output;

import commands.CommandType;
import fileio.input.CommandInput;

public final class PageOutput {
    private String user;
    private CommandType command;
    private int timestamp;
    private String message;

    private PageOutput(final CommandInput command) {
        setUser(command.getUsername());
        setCommand(command.getCommand());
        setTimestamp(command.getTimestamp());
    }

    public PageOutput(final CommandInput command, final String message) {
        this(command);
        setMessage(message);
    }
    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public CommandType getCommand() {
        return command;
    }

    public void setCommand(final CommandType command) {
        this.command = command;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
