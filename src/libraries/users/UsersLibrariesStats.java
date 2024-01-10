package libraries.users;

import managers.CheckClass;
import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import entities.user.User;

import java.util.ArrayList;

public final class UsersLibrariesStats {
    private UsersLibrariesStats() {
    }

    /**
     * Checks if the specified entities.user exists (in any entities.user library aka hosts, normal suers, artists)
     *
     * @param username The name of the entities.user to search for
     * @return The entities.user or {@code null} if it wasn't found
     */
    public static boolean userExists(final String username) {
        for (NormalUser normalUser : NormalUsersLibrary.getInstance().getItems()) {
            if (normalUser.getName().equals(username)) {
                return true;
            }
        }
        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        if (artists != null) {
            for (Artist artist : artists) {
                if (artist.getName().equals(username)) {
                    return true;
                }
            }
        }
        ArrayList<Host> hosts = HostsLibrary.getInstance().getItems();
        if (hosts != null) {
            for (Host host : hosts) {
                if (host.getName().equals(username)) {
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
                .map(User::getName)
                .toList());
        userNames.addAll(ArtistsLibrary.getInstance()
                .getItems()
                .stream()
                .map(Artist::getName)
                .toList());
        userNames.addAll(HostsLibrary.getInstance()
                .getItems()
                .stream()
                .map(Host::getName)
                .toList());
        return userNames;
    }

    /**
     * Retrieves a entities.user by their username.
     * This method searches for the entities.user in different entities.user libraries,
     * including normal users, artists, and hosts.
     *
     * @param username The username of the entities.user to retrieve.
     * @return The entities.user with the specified username, or null if not found.
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
     * Deletes the specified entities.user from the appropriate entities.user library.
     * This method identifies the type of the entities.user (artist, host, or normal entities.user)
     * using the {@link CheckClass} utility and deletes the entities.user from the
     * corresponding library.
     *
     * @param user The entities.user to be deleted.
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
