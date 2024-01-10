package audio.collections;

import audio.Episode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;

/**
 * Implementation for a podcast object
 */
public final class Podcast extends Collection<Episode> {
    public Podcast(final PodcastInput podcast) {
        setName(podcast.getName());
        setOwner(podcast.getOwner());
        collection = new ArrayList<>();
        for (EpisodeInput episodeInput : podcast.getEpisodes()) {
            Episode episode = new Episode(episodeInput);
            collection.add(episode);
        }
    }

    public Podcast(final String podcastName,
                   final String owner,
                   final ArrayList<Episode> episodes) {
        setName(podcastName);
        setOwner(owner);
        setCollection(episodes);
    }

    @Override
    public String toString() {
        return this.getName() + ":\n\t" + this.getCollection();
    }
}
