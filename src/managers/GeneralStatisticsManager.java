package managers;

import audio.Song;
import audio.collections.Album;
import audio.collections.Playlist;
import commands.CommandType;
import fileio.input.CommandInput;
import fileio.output.Output;
import libraries.audio.AlbumsLibrary;
import libraries.audio.PlaylistsLibrary;
import libraries.audio.SongsLibrary;
import libraries.users.ArtistsLibrary;
import libraries.users.NormalUsersLibrary;
import libraries.users.UsersLibrariesStats;
import managers.commands.CommandHandler;
import user.Artist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class GeneralStatisticsManager implements CommandHandler {
    private static final int NUMBER_TOP_RESULTS = 5;
    private static GeneralStatisticsManager instance;

    private GeneralStatisticsManager() {
    }

    /**
     * Get the instance for this class
     *
     * @return the instance
     */
    public static synchronized GeneralStatisticsManager getInstance() {
        if (instance == null) {
            instance = new GeneralStatisticsManager();
        }
        return instance;
    }

    /**
     * Performs the top5Playlists command
     *
     * @param command the command that specifies the top5Playlists command parameters
     * @return an {@code Output} object with the command and result
     * (with top 5 playlists by followers)
     */
    public static Output performTop5Playlists(final CommandInput command) {
        ArrayList<Playlist> top5Playlists = PlaylistsLibrary.getInstance()
                .getTopPlaylists(NUMBER_TOP_RESULTS);
        ArrayList<Object> result = new ArrayList<>();
        if (top5Playlists != null && !top5Playlists.isEmpty()) {
            for (Playlist playlist : top5Playlists) {
                result.add(playlist.getName());
            }
        }
        return new Output(command, result);
    }

    /**
     * Performs the top5Songs command
     *
     * @param command the command that specifies the top5Songs command parameters
     * @return an {@code Output} object with the command and result (with top 5 songs by likes)
     */
    public static Output performTop5Songs(final CommandInput command) {
        ArrayList<Song> top5Songs = SongsLibrary.getInstance().getTopSongs(NUMBER_TOP_RESULTS);
        ArrayList<Object> result = new ArrayList<>();
        if (top5Songs != null && !top5Songs.isEmpty()) {
            for (Song song : top5Songs) {
                result.add(song.getName());
            }
        }
        return new Output(command, result);
    }

    /**
     * Performs the get online users command, retrieving a list of users currently online.
     *
     * @param command The input command, which may contain additional parameters.
     * @return An Output object containing the result of the operation (list of online users).
     */
    public static Output performGetOnlineUsers(final CommandInput command) {
        List<String> onlineUsers = NormalUsersLibrary.getInstance().getOnlineUsers();
        ArrayList<Object> result = new ArrayList<>(onlineUsers);
        return new Output(command, result);
    }

    /**
     * Performs the get all users command, retrieving a list of all users.
     *
     * @param command The input command, which may contain additional parameters.
     * @return An Output object containing the result of the operation (list of all users).
     */
    public static Output performGetAllUsers(final CommandInput command) {
        ArrayList<Object> result = UsersLibrariesStats.getAllUsers();
        return new Output(command, result);
    }

    /**
     * Performs the get top 5 albums command, retrieving a list of the top 5 albums based on likes.
     *
     * @param command The input command, which may contain additional parameters.
     * @return An Output object containing the result of the operation (list of top 5 albums).
     */
    public static Output performGetTop5Albums(final CommandInput command) {
        ArrayList<Album> albums = AlbumsLibrary.getInstance().getItems();
        List<Album> top5Albums = albums.stream()
                .sorted(Comparator.comparingInt(Album::getTotalLikes).reversed()
                        .thenComparing(Album::getName))
                .limit(NUMBER_TOP_RESULTS)
                .toList();
        ArrayList<Object> result = top5Albums.stream()
                .map(Album::getName)
                .collect(Collectors.toCollection(ArrayList::new));
        return new Output(command, result);
    }

    /**
     * Performs the get top 5 artists command,
     * retrieving a list of the top 5 artists based on likes.
     *
     * @param command The input command, which may contain additional parameters.
     * @return An Output object containing the result of the operation (list of top 5 artists).
     */
    public static Output performGetTop5Artists(final CommandInput command) {
        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        List<Artist> top5Artists = artists.stream()
                .sorted(Comparator.comparingInt(Artist::getTotalLikes).reversed()
                        .thenComparing(Artist::getUsername))
                .limit(NUMBER_TOP_RESULTS)
                .toList();
        ArrayList<Object> result = top5Artists.stream()
                .map(Artist::getUsername)
                .collect(Collectors.toCollection(ArrayList::new));
        return new Output(command, result);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case getTop5Playlists -> performTop5Playlists(command);
            case getTop5Songs -> performTop5Songs(command);
            case getOnlineUsers -> performGetOnlineUsers(command);
            case getAllUsers -> performGetAllUsers(command);
            case getTop5Albums -> performGetTop5Albums(command);
            case getTop5Artists -> performGetTop5Artists(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }
}
