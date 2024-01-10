package statistics;

import audio.Song;
import audio.collections.Album;
import user.NormalUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListenTrackerArtist {
    private final ListenTracker albumsListenTracker = new ListenTracker();
    private final ListenTracker songsListenTracker = new ListenTracker();
    private final ListenTracker fansListenTracker = new ListenTracker();

    public void addListen(Album album) {
        albumsListenTracker.addListen(album.getName());
    }

    public void addListen(Song song) {
        songsListenTracker.addListen(song.getName());
    }

    public void addListen(NormalUser user) {
        fansListenTracker.addListen(user.getUsername());
    }

    public void addListen(Album album, int count) {
        albumsListenTracker.addListen(album.getName(), count);
    }

    public void addListen(Song song, int count) {
        songsListenTracker.addListen(song.getName(), count);
    }

    public void addListen(NormalUser user, int count) {
        fansListenTracker.addListen(user.getUsername(), count);
    }

    public HashMap<String, Object> topListensForEach() {
        HashMap<String, Object> result = new LinkedHashMap<>();

        result.put("topAlbums", albumsListenTracker.getTopFiveListens());
        result.put("topSongs", songsListenTracker.getTopFiveListens());
        result.put("topFans", fansListenTracker.getTopFiveListens().keySet().stream().toList());
        result.put("listeners", fansListenTracker.getSize());

        return result;
    }

    public void addListenAll(Album album, Song song, NormalUser user) {
        addListen(album);
        addListen(song);
        addListen(user);
    }

    public void addListenAll(String albumName, Song song, NormalUser user) {
        albumsListenTracker.addListen(albumName);
        addListenAll(song, user);
    }

    public void addListenAll(Song song, NormalUser user) {
        addListen(song);
        addListen(user);
    }

    public void addListenAll(Song song, NormalUser user, int count) {
        addListen(song, count);
        addListen(user, count);
    }

    public boolean wasListened() {
        return (!albumsListenTracker.isEmpty()
        || !songsListenTracker.isEmpty()
        || !fansListenTracker.isEmpty());
    }
}
