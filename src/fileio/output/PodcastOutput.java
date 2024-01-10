package fileio.output;

import audio.Episode;
import audio.collections.Podcast;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the podcast output
 */
public final class PodcastOutput {
    @Getter
    private String name;
    private List<String> episodeNames;

    private PodcastOutput(final String name,
                          final ArrayList<Episode> episodes) {

        this.name = name;
        this.episodeNames = episodes.stream().map(Episode::getName).toList();
    }

    public PodcastOutput(final Podcast podcast) {
        this(podcast.getName(), podcast.getCollection());
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getEpisodes() {
        return episodeNames;
    }

    public void setEpisodes(final List<String> otherEpisodeNames) {
        this.episodeNames = otherEpisodeNames;
    }
}
