package managers.admin;

import commands.admin.*;
import entities.audio.Song;
import entities.audio.collections.Album;
import entities.audio.collections.Podcast;
import commands.CommandType;
import entities.user.NormalUser;
import fileio.input.CommandInput;
import fileio.output.Output;
import fileio.output.PodcastOutput;
import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.NormalUsersLibrary;
import managers.commands.CommandHandler;
import entities.user.Artist;
import entities.user.Host;
import entities.user.UserType;

import java.util.*;

public final class AdminCommandManager implements CommandHandler {
    private static AdminCommandManager instance;

    private AdminCommandManager() {
    }

    /**
     * Gets the instance of this class
     *
     * @return the instance
     */
    public static synchronized AdminCommandManager getInstance() {
        if (instance == null) {
            instance = new AdminCommandManager();
        }
        return instance;
    }

    /**
     * Performs the add entities.user command based on the provided input.
     *
     * @param command The input containing entities.user details.
     * @return An Output object with the result of the operation.
     */
    public static Output performAddUser(final CommandInput command) {
        UserType userType = UserType.fromString(command.getType());
        String username = command.getUsername();
        int age = command.getAge();
        String city = command.getCity();
        AddUser.execute(userType, username, age, city);
        String message = AddUser.toString(username);
        return new Output(command, message);
    }

    /**
     * Performs the show albums command for the specified artist.
     *
     * @param command The input containing the artist's username.
     * @return An Output object with the result of the operation.
     */
    public static Output performShowAlbums(final CommandInput command) {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(command.getUsername());
        HashSet<Album> albums = artist.getAlbums();
        ArrayList<Object> result = new ArrayList<>();
        for (Album album : albums) {
            LinkedHashMap<String, Object> albumInfo = new LinkedHashMap<>();
            albumInfo.put("name", album.getName());
            List<String> songNames = album.getCollection().stream().map(Song::getName).toList();
            albumInfo.put("songs", songNames);
            result.add(albumInfo);
        }
        return new Output(command, result);
    }

    /**
     * Performs the delete entities.user command based on the provided input.
     *
     * @param command The input containing the username to be deleted.
     * @return An Output object with the result of the operation.
     */
    public static Output performDeleteUser(final CommandInput command) {
        DeleteUser.execute(command.getUsername());
        String message = DeleteUser.toString(command.getUsername());
        return new Output(command, message);
    }

    /**
     * Performs the show podcasts command for the specified host.
     *
     * @param command The input containing the host's username.
     * @return An Output object with the result of the operation.
     */
    public static Output performShowPodcasts(final CommandInput command) {
        Host host = HostsLibrary.getInstance().getHostByName(command.getUsername());
        assert host != null;
        LinkedHashSet<Podcast> podcasts = host.getPodcasts();
        ArrayList<Object> result = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            result.add(new PodcastOutput(podcast));
        }
        return new Output(command, result);
    }

    /**
     * Performs the buy premium command for the specified user.
     *
     * @param command The input containing the user's username.
     * @return An Output object with the result of the operation.
     */
    public static Output performAddPremiumUser(final CommandInput command) {
        String username = command.getUsername();
        AddPremiumUser.execute(username);
        String message = AddPremiumUser.toString(username);
        return new Output(command, message);
    }

    /**
     * Performs the remove premium command for the specified user.
     *
     * @param command The input containing the user's username.
     * @return An Output object with the result of the operation.
     */
    public static Output performRemovePremiumUser(final CommandInput command) {
        String username = command.getUsername();
        RemovePremiumUser.execute(username);
        String message = RemovePremiumUser.toString(username);
        return new Output(command, message);
    }
    /**
     * Performs the ad break command for the specified user.
     *
     * @param command The input containing the user's username and the ad price
     * @return An Output object with the result of the operation.
     */
    private Output performAdBreak(final CommandInput command) {
        String username = command.getUsername();
        AdBreak.execute(username, command.getPrice());
        String message = AdBreak.toString(username);
        return new Output(command, message);
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case addUser -> performAddUser(command);
            case showAlbums -> performShowAlbums(command);
            case deleteUser -> performDeleteUser(command);
            case showPodcasts -> performShowPodcasts(command);
            case buyPremium -> performAddPremiumUser(command);
            case cancelPremium -> performRemovePremiumUser(command);
            case adBreak -> performAdBreak(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }
}
