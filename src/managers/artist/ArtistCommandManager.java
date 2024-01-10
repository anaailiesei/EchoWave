package managers.artist;

import entities.audio.Song;
import commands.CommandType;
import commands.artist.*;
import fileio.input.CommandInput;
import fileio.output.Output;
import managers.commands.CommandHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class ArtistCommandManager implements CommandHandler {
    private static ArtistCommandManager instance;

    private ArtistCommandManager() {
    }

    /**
     * Gets the instance for this class
     *
     * @return the instance
     */
    public static synchronized ArtistCommandManager getInstance() {
        if (instance == null) {
            instance = new ArtistCommandManager();
        }
        return instance;
    }

    /**
     * Perform the add album operation
     *
     * @param command the command that specifies the add album command parameters
     * @return an {@code Output} object with the command and message info
     * @see AddAlbum
     */
    public static Output performAddAlbum(final CommandInput command) {
        ArrayList<Song> songs = command.getSongs().stream()
                .map(Song::new).collect(Collectors.toCollection(ArrayList::new));
        AddAlbum.execute(command.getName(),
                command.getUsername(),
                command.getReleaseYear(),
                command.getDescription(),
                songs);
        String message = AddAlbum.toString(command.getUsername());
        return new Output(command, message);
    }

    /**
     * Perform the add event operation
     *
     * @param command the command that specifies the add event command parameters
     * @return an {@code Output} object with the command and message info
     * @see AddEvent
     */
    public static Output performAddEvent(final CommandInput command) {
        AddEvent.execute(command.getName(),
                command.getUsername(),
                command.getDescription(),
                command.getDate());
        String message = AddEvent.toString(command.getUsername());
        return new Output(command, message);
    }

    /**
     * Perform the add merch operation
     *
     * @param command the command that specifies the add merch command parameters
     * @return an {@code Output} object with the command and message info
     * @see AddMerch
     */
    public static Output performAddMerch(final CommandInput command) {
        AddMerch.execute(command.getName(),
                command.getUsername(),
                command.getDescription(),
                command.getPrice());
        String message = AddMerch.toString(command.getUsername());
        return new Output(command, message);
    }

    /**
     * Perform the remove album operation
     *
     * @param command the command that specifies the remove album command parameters
     * @return an {@code Output} object with the command and message info
     * @see commands.artist.RemoveAlbum
     */
    public static Output performRemoveAlbum(final CommandInput command) {
        RemoveAlbum.execute(command.getName(), command.getUsername());
        String message = RemoveAlbum.toString(command.getUsername());
        return new Output(command, message);
    }

    /**
     * Performs the remove event command based on the provided input.
     *
     * @param command The input containing the event name.
     * @return An Output object with the result of the operation.
     */
    public static Output performRemoveEvent(final CommandInput command) {
        RemoveEvent.execute(command.getName(), command.getUsername());
        String message = RemoveEvent.toString(command.getUsername());
        return new Output(command, message);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case addAlbum -> performAddAlbum(command);
            case addEvent -> performAddEvent(command);
            case addMerch -> performAddMerch(command);
            case removeAlbum -> performRemoveAlbum(command);
            case removeEvent -> performRemoveEvent(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }
}
