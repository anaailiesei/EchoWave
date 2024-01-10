package libraries.users;

import audio.collections.Podcast;
import libraries.GenericLibrary;
import libraries.audio.PodcastsLibrary;
import user.Host;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public final class HostsLibrary extends GenericLibrary<Host> {
    private static HostsLibrary instance = null;

    private HostsLibrary() {
    }

    /**
     * Gets the instance for the hosts' library class (singleton pattern)
     * Initialize it if needed
     *
     * @return The library instance
     */
    public static synchronized HostsLibrary getInstance() {
        if (instance == null) {
            instance = new HostsLibrary();
        }
        return instance;
    }

    /**
     * Checks if the host exists
     *
     * @param username The username of the host we search for
     * @return {@code true} if the host exists, {@code false} otherwise
     */
    public boolean hostExists(final String username) {
        ArrayList<Host> hosts = getItems();
        if (hosts == null || hosts.isEmpty()) {
            return false;
        }
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the host by their name
     *
     * @param username The username of the host we search for
     * @return The host
     */
    public Host getHostByName(final String username) {
        ArrayList<Host> hosts = getItems();
        if (hosts == null || hosts.isEmpty()) {
            return null;
        }
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }

    /**
     * Deletes the given host
     * When deleting the host, it deletes their podcast from the podcasts lib too
     *
     * @param host The host to be deleted
     */
    public void deleteHost(final Host host) {
        LinkedHashSet<Podcast> podcasts = host.getPodcasts();
        for (Podcast podcast : podcasts) {
            PodcastsLibrary.getInstance().removeItem(podcast);
        }
        removeItem(host);
    }
}
