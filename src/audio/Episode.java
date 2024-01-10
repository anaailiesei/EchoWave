package audio;

import fileio.input.EpisodeInput;
import statistics.ListenTrackerNormalUser;

/**
 * Implementation for an episode object
 */
public final class Episode implements Audio {
    private final EpisodeInput episodeInput;

    public Episode(final EpisodeInput episodeInput) {
        this.episodeInput = episodeInput;
    }

    public Episode(final Episode episode) {
        this(episode.episodeInput);
    }

    /**
     * Gets the name of the episode
     *
     * @return the name
     */
    @Override
    public String getName() {
        return episodeInput.getName();
    }

    /**
     * Gets the duration of the episode
     *
     * @return the duration
     */
    @Override
    public int getDuration() {
        return episodeInput.getDuration();
    }

    /**
     * Checks if the name of the episode starts with the given string
     *
     * @param searchString for the string that's being searched
     * @return {@code true} if the episode's name starts with th specified string,
     * {@code false} otherwise
     */
    @Override
    public boolean nameStartsWith(final String searchString) {
        return episodeInput.getName().startsWith(searchString);
    }

    @Override
    public void addListen(ListenTrackerNormalUser listenTracker) {
        listenTracker.addListen(this);
    }

    @Override
    public String getOwner() {
        return null;
    }

    /**
     * Copies an episode
     *
     * @return an instance of the newly created episode
     */
    @Override
    public Episode copyObject() {
        return new Episode(this);
    }

    @Override
    public String toString() {
        return episodeInput.getName() + " - " + episodeInput.getDescription();
    }
}
