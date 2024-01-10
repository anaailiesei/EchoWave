package managers.host;

import audio.Episode;
import commands.CommandType;
import commands.host.AddAnnouncement;
import commands.host.AddPodcast;
import commands.host.RemoveAnnouncement;
import commands.host.RemovePodcast;
import fileio.input.CommandInput;
import fileio.output.Output;
import managers.commands.CommandHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class HostCommandManager implements CommandHandler {
    private static HostCommandManager instance;
    private HostCommandManager() {
    }

    /**
     * Gets the instance for this class
     * @return the instance
     */
    public static synchronized HostCommandManager getInstance() {
        if (instance == null) {
            instance = new HostCommandManager();
        }
        return instance;
    }
    /**
     * Performs the add podcast command, adding a new podcast with the specified episodes.
     *
     * @param command The input command containing podcast details, including episodes.
     * @return An Output object with the result of the operation.
     */
    public static Output performAddPodcast(final CommandInput command) {
        ArrayList<Episode> episodes = command.getEpisodes().stream()
                .map(Episode::new).collect(Collectors.toCollection(ArrayList::new));
        AddPodcast.execute(command.getName(),
                command.getUsername(),
                episodes);
        String message = AddPodcast.toString(command.getUsername());
        return new Output(command, message);
    }

    /**
     * Performs the add announcement command, adding a new announcement with the specified details.
     *
     * @param command The input command containing announcement details.
     * @return An Output object with the result of the operation.
     */
    public static Output performAddAnnouncement(final CommandInput command) {
        AddAnnouncement.execute(command.getName(),
                command.getUsername(),
                command.getDescription());
        String message = AddAnnouncement.toString(command.getUsername());
        return new Output(command, message);
    }
    /**
     * Performs the remove announcement command, removing an announcement with the specified name.
     *
     * @param command The input command containing the name of the announcement to be removed.
     * @return An Output object with the result of the operation.
     */
    public static Output performRemoveAnnouncement(final CommandInput command) {
        RemoveAnnouncement.execute(command.getName(), command.getUsername());
        String message = RemoveAnnouncement.toString(command.getUsername());
        return new Output(command, message);
    }
    /**
     * Performs the remove podcast command, removing a podcast with the specified name.
     *
     * @param command The input command containing the name of the podcast to be removed.
     * @return An Output object with the result of the operation.
     */
    public static Output performRemovePodcast(final CommandInput command) {
        RemovePodcast.execute(command.getName(), command.getUsername());
        String message = RemovePodcast.toString(command.getUsername());
        return new Output(command, message);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case addPodcast -> performAddPodcast(command);
            case addAnnouncement -> performAddAnnouncement(command);
            case removeAnnouncement -> performRemoveAnnouncement(command);
            case removePodcast -> performRemovePodcast(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }
}
