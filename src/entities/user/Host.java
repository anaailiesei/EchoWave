package entities.user;

import entities.audio.collections.Podcast;
import fileio.input.UserInput;
import lombok.Getter;
import profile.host.Announcement;

import java.util.HashMap;
import java.util.LinkedHashSet;

public final class Host extends User {
    @Getter
    private final LinkedHashSet<Podcast> podcasts = new LinkedHashSet<>();
    @Getter
    private final LinkedHashSet<Announcement> announcements = new LinkedHashSet<>();
    private int pageViewersCount = 0;

    public Host(final String username, final int age, final String city) {
        UserInput userInput = new UserInput(username, age, city);
        setUserInput(userInput);
    }

    /**
     * Adds a podcast to the host's collection of podcasts.
     *
     * @param podcast The podcast to be added.
     */
    public void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Adds an announcement to the host's collection of announcements.
     *
     * @param announcement The announcement to be added.
     */
    public void addAnnouncement(final Announcement announcement) {
        announcements.add(announcement);
    }

    /**
     * Checks if an announcement with the given name exists in the host's announcements.
     *
     * @param announcementName The name of the announcement to check.
     * @return true if the announcement exists, false otherwise.
     */
    public boolean announcementExists(final String announcementName) {
        return announcements.stream().
                anyMatch(announcement -> announcement.getName().equals(announcementName));
    }

    /**
     * Retrieves an announcement by its name from the host's announcements.
     *
     * @param announcementName The name of the announcement to retrieve.
     * @return The Announcement object if found, or null if not found.
     */
    public Announcement getAnnouncementByName(final String announcementName) {
        return announcements.stream()
                .filter(announcement -> announcementName.equals(announcement.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes the specified announcement from the host's announcements.
     *
     * @param announcement The announcement to be removed.
     */
    public void removeAnnouncement(final Announcement announcement) {
        announcements.remove(announcement);
    }

    /**
     * Removes the announcement with the given name from the host's announcements.
     * If the announcement is not found, no action is taken.
     *
     * @param announcementName The name of the announcement to be removed.
     */
    public void removeAnnouncement(final String announcementName) {
        Announcement announcement = getAnnouncementByName(announcementName);
        if (announcement != null) {
            removeAnnouncement(announcement);
        }
    }

    /**
     * Checks if a podcast with the given name exists in the host's podcasts.
     *
     * @param podcastName The name of the podcast to check.
     * @return true if the podcast exists, false otherwise.
     */
    public boolean podcastExists(final String podcastName) {
        return podcasts.stream().anyMatch(podcast -> podcast.getName().equals(podcastName));
    }

    /**
     * Increments the counter for the number of users that are on this host's page
     */
    public void incrementPageViewersCount() {
        pageViewersCount++;
    }

    /**
     * Decrements the counter for the number of users that are on this host's page
     */
    public void decrementPageViewersCount() {
        pageViewersCount--;
    }

    /**
     * Retrieves a podcast by its name from the host's podcasts.
     *
     * @param podcastName The name of the podcast to retrieve.
     * @return The Podcast object if found, or null if not found.
     */
    public Podcast getPodcastByName(final String podcastName) {
        return podcasts.stream()
                .filter(podcast -> podcast.getName().equals(podcastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes the specified podcast from the host's podcasts.
     *
     * @param podcast The podcast to be removed.
     */
    public void removePodcast(final Podcast podcast) {
        podcasts.remove(podcast);
    }

    @Override
    public boolean isDeletable() {
        if (pageViewersCount > 0) {
            return false;
        }
        if (podcasts == null) {
            return true;
        }
        for (Podcast podcast : podcasts) {
            if (!podcast.isDeletable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public HashMap<String, Object> wrapped() {
        return new HashMap<>();
    }
}
