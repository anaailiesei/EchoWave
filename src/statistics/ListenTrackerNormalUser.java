package statistics;

import audio.Audio;
import audio.Episode;
import audio.Song;
import audio.collections.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListenTrackerNormalUser {
    private final ListenTracker artistsListenTracker = new ListenTracker();
    private final ListenTracker albumsListenTracker = new ListenTracker();
    private final ListenTracker songsListenTracker = new ListenTracker();
    private final ListenTracker episodesListenTracker = new ListenTracker();
    private final ListenTracker genresListenTracker = new ListenTracker();

    public void addListen(Album album) {
        albumsListenTracker.addListen(album.getName());
    }

    public void addListen(String albumName) {
        albumsListenTracker.addListen(albumName);
    }

    public void addListen(Song song) {
        songsListenTracker.addListen(song.getName());
        genresListenTracker.addListen(song.getGenre());
        artistsListenTracker.addListen(song.getArtist());
    }

    public void addListen(Song song, int count) {
        songsListenTracker.addListen(song.getName(), count);
        genresListenTracker.addListen(song.getGenre(), count);
        artistsListenTracker.addListen(song.getArtist(), count);
    }

    public void addListen(Episode episode) {
        episodesListenTracker.addListen(episode.getName());
    }

    public <E extends Audio> void addListen(E audio) {
        audio.addListen(this);
    }

    public HashMap<String, Object> topListensForEach() {
        HashMap<String, Object> result = new LinkedHashMap<>();

        Map<String, Object> topArtistsEntry = new HashMap<>();
        result.put("topArtists", artistsListenTracker.getTopFiveListens());
        result.put("topGenres", genresListenTracker.getTopFiveListens());
        result.put("topSongs", songsListenTracker.getTopFiveListens());
        result.put("topAlbums", albumsListenTracker.getTopFiveListens());
        result.put("topEpisodes", episodesListenTracker.getTopFiveListens());
        return result;
    }
}
