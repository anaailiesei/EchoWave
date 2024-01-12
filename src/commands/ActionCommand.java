package commands;

public abstract class ActionCommand {
    private String message;

    /**
     * Set the action's message to the specified string
     * @param message the message to be set
     */
    protected void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get the action's message
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Executes the command
     */
    public void execute() {
    }

    /**
     * Undo the command
     */
    public void undo() {
    }

    /**
     * Redo the command
     */
    public void redo() {
    }
}
