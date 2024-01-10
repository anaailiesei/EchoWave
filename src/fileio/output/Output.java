package fileio.output;

import commands.CommandType;
import commands.normalUser.player.StatusFields;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class for printing the output
 */
public final class Output {
    private CommandType command;
    private String user;
    private Integer timestamp;
    private String message;
    private ArrayList<String> results;
    private Map<StatusFields, Object> stats;
    private Object result;

    private Output(final CommandInput command) {
        setUser(command.getUsername());
        setTimestamp(command.getTimestamp());
        setCommand(command.getCommand());
    }

    public Output(final CommandInput command,
                  final ArrayList<String> results,
                  final String message) {
        this(command, message);
        setResults(results);
    }

    public Output(final CommandInput command, final String message) {
        this(command);
        setMessage(message);
    }

    public Output(final CommandInput command, final Map<StatusFields, Object> stats) {
        this(command);
        setStats(stats);
    }

    public Output(final CommandInput command, final Object result) {
        this(command);
        setResult(result);
    }

    public Output(final CommandType type, final Object result) {
        setCommand(type);
        setResult(result);
    }

    /**
     * Gets the type of command performed
     *
     * @return The type of command
     */
    public CommandType getCommand() {
        return command;
    }

    /**
     * Sets the type of command
     *
     * @param command The type command to be set
     */
    public void setCommand(final CommandType command) {
        this.command = command;
    }

    /**
     * Get the username of the entities.user that performed the action
     *
     * @return The username
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the username of the entities.user that performed the action
     *
     * @param user The username to be set
     */
    public void setUser(final String user) {
        this.user = user;
    }

    /**
     * get the time at which the command was performed
     *
     * @return the timestamp
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     * Set the time at which the command was performed
     *
     * @param timestamp The time to be set
     */
    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the result of the command
     *
     * @return the list of results
     */
    public ArrayList<String> getResults() {
        return results;
    }

    /**
     * Set the results of the command
     *
     * @param results The results to be set
     */
    public void setResults(final ArrayList<String> results) {
        this.results = results;
    }

    /**
     * Gets the message associated to the performed command
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message associated with the performed command
     *
     * @param message To be set
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get the stats associated with the performed command (status)
     *
     * @return the stats
     */
    public Map<StatusFields, Object> getStats() {
        return stats;
    }

    /**
     * Set the stats associated with the performed command (status)
     *
     * @param stats the stats to be set
     */
    public void setStats(final Map<StatusFields, Object> stats) {
        this.stats = stats;
    }

    /**
     * Gets the result of the performed operation
     *
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * Sets the result for the preformed operation
     *
     * @param result The result to be set
     */
    public void setResult(final Object result) {
        this.result = result;
    }
}
