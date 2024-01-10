package managers.normalUser;

import entities.audio.collections.Playlist;
import commands.CommandType;
import commands.normalUser.playlist.CreatePlaylist;
import commands.normalUser.playlist.FollowPlaylist;
import fileio.input.CommandInput;
import fileio.output.Output;
import fileio.output.PlaylistOutput;
import libraries.users.NormalUsersLibrary;
import managers.commands.CommandHandler;
import entities.user.NormalUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing playlists commands
 */
public final class PlaylistManager implements CommandHandler {
    private final AppManager app;

    public PlaylistManager(final AppManager parentApp) {
        this.app = parentApp;
    }

    /**
     * Perform the showPreferredSongs command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the result (a list of names with
     * the songs liked by the entities.user)
     */
    public static Output performShowPreferredSongs(final CommandInput command) {
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(command.getUsername());
        ArrayList<Object> result = new ArrayList<>();
        Playlist liked = user.getLiked();
        if (liked != null && !liked.getCollection().isEmpty()) {
            List<String> likedSongs = user.getLiked().getSongNames();
            result.addAll(likedSongs);
        }
        return new Output(command, result);
    }

    /**
     * Perform the createPlaylist command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see CreatePlaylist
     */
    public Output performCreatePlaylist(final CommandInput command) {
        CommandManager commandManager = app.getCommandManager();
        CreatePlaylist createPlaylist = commandManager.getCreatePlaylist();
        String username = command.getUsername();
        String playlistName = command.getPlaylistName();
        createPlaylist.execute(playlistName, username);
        String message = createPlaylist.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the showPlaylists command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and result (a list of owned playlists
     * by the entities.user that made the action)
     */
    public Output performShowPlaylists(final CommandInput command) {
        CommandManager commandManager = app.getCommandManager();
        commandManager.getSelectAudio().resetSelect();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(command.getUsername());
        ArrayList<Playlist> playlists = user.getPlaylists();
        ArrayList<Object> result = new ArrayList<>();
        for (Playlist playlist : playlists) {
            List<String> songNames = playlist.getSongNames();
            PlaylistOutput playlistOutput = new PlaylistOutput(playlist.getName(), songNames);
            playlistOutput.setFollowers(playlist.getFollowers());
            playlistOutput.setVisibility(playlist.getVisibility());
            result.add(playlistOutput);
        }
        return new Output(command, result);
    }

    /**
     * Perform the followPlaylist command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see FollowPlaylist
     */
    public Output performFolllowPlaylist(final CommandInput command) {
        CommandManager commandManager = app.getCommandManager();
        FollowPlaylist followPlaylist = commandManager.getFollowPlaylist();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(command.getUsername());
        followPlaylist.setUser(user).execute();
        String message = followPlaylist.getMessage();
        return new Output(command, message);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case createPlaylist -> performCreatePlaylist(command);
            case follow -> performFolllowPlaylist(command);
            case showPreferredSongs -> performShowPreferredSongs(command);
            case showPlaylists -> performShowPlaylists(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }
}
