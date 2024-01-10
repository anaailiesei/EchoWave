package fileio.output;

import audio.collections.Visibility;

import java.util.List;

/**
 * Class for printing the playlist output
 */
public final class PlaylistOutput {
    private String name;
    private List<String> songs;
    private String visibility = Visibility.PUBLIC.getValue();
    private int followers = 0;

    public PlaylistOutput(final String name, final List<String> songs) {
        setName(name);
        setSongs(songs);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(final List<String> songs) {
        this.songs = songs;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(final String visibility) {
        this.visibility = visibility;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(final int followers) {
        this.followers = followers;
    }
}
