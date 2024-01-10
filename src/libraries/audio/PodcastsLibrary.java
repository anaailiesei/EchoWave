package libraries.audio;

import entities.audio.collections.Podcast;
import libraries.GenericLibrary;

public final class PodcastsLibrary extends GenericLibrary<Podcast> {
    private static PodcastsLibrary instance = null;

    private PodcastsLibrary() {
    }

    /**
     * Gets the instance for the podcasts' library class (singleton pattern)
     * Initialize it if needed
     *
     * @return The library instance
     */
    public static synchronized PodcastsLibrary getInstance() {
        if (instance == null) {
            instance = new PodcastsLibrary();
        }
        return instance;
    }

    /**
     * Adds the specified podcast to the library
     *
     * @param podcast The podcast to be added
     */
    public void addPodcast(final Podcast podcast) {
        addItem(podcast);
    }
}
