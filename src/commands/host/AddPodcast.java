package commands.host;

import entities.audio.Episode;
import entities.audio.collections.Podcast;
import libraries.audio.PodcastsLibrary;
import libraries.users.HostsLibrary;
import libraries.users.UsersLibrariesStats;
import entities.user.Host;
import notifications.Notification;
import notifications.NotificationType;

import java.util.ArrayList;
import java.util.HashMap;

public final class AddPodcast {
    private static State state;

    private AddPodcast() {
    }

    /**
     * Execute the add podcast command
     *
     * @param podcastName The name of the podcast that should be added
     * @param username    The name of th entities.user that want to add a podcast
     * @param episodes    The episodes in the podcast
     */
    public static void execute(final String podcastName,
                               final String username,
                               final ArrayList<Episode> episodes) {
        Host host = HostsLibrary.getInstance().getHostByName(username);
        checkConditions(podcastName, username, host, episodes);
        if (!state.equals(State.successfullyAddedPodcast)) {
            return;
        }
        Podcast podcast = new Podcast(podcastName, username, episodes);
        assert host != null;
        host.addPodcast(podcast);
        PodcastsLibrary.getInstance().addPodcast(podcast);
        HashMap<String, String> notification = Notification
                .getNotification(NotificationType.Announcement, username);
        host.notifyObservers(notification);
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the entities.user should exist, and they should be a host,
     * there shouldn't be another podcast with the same name among their podcast collection,
     * and the new added podcast shouldn't have duplicated episodes
     *
     * @param podcastName The name of the podcast to be checked
     * @param username    The name of the entities.user
     * @param host        The host (this should be non-null for a successful operation)
     * @param episodes    The episodes in the podcast (checks if there are duplicates)
     */
    private static void checkConditions(final String podcastName,
                                        final String username,
                                        final Host host,
                                        final ArrayList<Episode> episodes) {
        if (!UsersLibrariesStats.userExists(username)) {
            state = State.noUser;
        } else if (host == null) {
            state = State.notHost;
        } else if (host.podcastExists(podcastName)) {
            state = State.duplicatedPodcast;
        } else if (hasDuplicates(episodes)) {
            state = State.repeatedEpisode;
        } else {
            state = State.successfullyAddedPodcast;
        }
    }

    /**
     * Checks if the list of episodes has duplicates
     *
     * @param episodes The list of episodes to be checked
     * @return {@code true} if the list has duplicates, {@code false} otherwise
     */
    private static boolean hasDuplicates(final ArrayList<Episode> episodes) {
        long distinctCount = episodes.stream().map(Episode::getName).distinct().count();
        return distinctCount < episodes.size();
    }

    /**
     * Function for the output message of this command
     *
     * @param username The entities.user that adds the podcast
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case notHost -> username + " is not a host.";
            case duplicatedPodcast -> username + " has another podcast with the same name.";
            case repeatedEpisode -> username + " has the same episode in this podcast.";
            default -> username + " has added new podcast successfully.";
        };
    }

    private enum State {
        noUser, successfullyAddedPodcast, notHost, repeatedEpisode, duplicatedPodcast
    }
}
