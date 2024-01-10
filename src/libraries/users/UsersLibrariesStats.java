package libraries.users;

import managers.CheckClass;
import user.Artist;
import user.Host;
import user.NormalUser;
import user.User;

import java.util.ArrayList;

public final class UsersLibrariesStats {
    private UsersLibrariesStats() {
    }

    /**
     * Checks if the specified user exists (in any user library aka hosts, normal suers, artists)
     *
     * @param username The name of the user to search for
     * @return The user or {@code null} if it wasn't found
     */
    public static boolean userExists(final String username) {
        for (NormalUser normalUser : NormalUsersLibrary.getInstance().getItems()) {
            if (normalUser.getUsername().equals(username)) {
                return true;
            }
        }
        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        if (artists != null) {
            for (Artist artist : artists) {
                if (artist.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        ArrayList<Host> hosts = HostsLibrary.getInstance().getItems();
        if (hosts != null) {
            for (Host host : hosts) {
                if (host.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get a list with all the users (no matter the type) in the order: normal users, artists, hosts
     *
     * @return The list of users
     */
    public static ArrayList<Object> getAllUsers() {
        ArrayList<Object> userNames = new ArrayList<>();
        userNames.addAll(NormalUsersLibrary.getInstance()
                .getItems()
                .stream()
                .map(User::getUsername)
                .toList());
        userNames.addAll(ArtistsLibrary.getInstance()
                .getItems()
                .stream()
                .map(Artist::getUsername)
                .toList());
        userNames.addAll(HostsLibrary.getInstance()
                .getItems()
                .stream()
                .map(Host::getUsername)
                .toList());
        return userNames;
    }

    /**
     * Retrieves a user by their username.
     * This method searches for the user in different user libraries,
     * including normal users, artists, and hosts.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username, or null if not found.
     */
    public static User getUserByName(final String username) {
        User normalUser = NormalUsersLibrary.getInstance().getUserByName(username);
        if (normalUser != null) {
            return normalUser;
        }
        User artist = ArtistsLibrary.getInstance().getArtistByName(username);
        if (artist != null) {
            return artist;
        }
        return HostsLibrary.getInstance().getHostByName(username);
    }

    /**
     * Deletes the specified user from the appropriate user library.
     * This method identifies the type of the user (artist, host, or normal user)
     * using the {@link CheckClass} utility and deletes the user from the
     * corresponding library.
     *
     * @param user The user to be deleted.
     */
    public static void deleteUser(final User user) {
        if (CheckClass.isArtist(user.getClass())) {
            ArtistsLibrary.getInstance().deleteArtist((Artist) user);
        } else if (CheckClass.isHost(user.getClass())) {
            HostsLibrary.getInstance().deleteHost((Host) user);
        } else if (CheckClass.isNormalUser(user.getClass())) {
            NormalUsersLibrary.getInstance().deleteUser((NormalUser) user);
        }
    }
}
