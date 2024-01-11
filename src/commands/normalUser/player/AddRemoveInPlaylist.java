package commands.normalUser.player;

import entities.audio.Audio;
import entities.audio.Song;
import entities.audio.collections.Playlist;
import commands.ActionCommand;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;

public final class AddRemoveInPlaylist extends ActionCommand {
    private final PlayerManager playerManager;
    private Playlist playlist;

    public AddRemoveInPlaylist(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Sets the playlist in which we want to add or remove a song
     *
     * @return the instance for this class
     */
    public AddRemoveInPlaylist setPlaylist(final Playlist newPlaylist) {
        this.playlist = newPlaylist;
        return this;
    }

    /**
     * Executes the add/remove operation in the playlist based on the currently playing audio
     * If the source is valid, and it's a playlist, if the song is not in the playlist
     * it adds it, otherwise it removes it
     */
    public void execute() {
        PlayingAudio<? extends Audio> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio != null
                && CheckClass.isSong(playingAudio.getPlayingObject().getClass())
                && playlist != null) {
            Song song = (Song) playingAudio.getPlayingObject();
            if (!playlist.itemExists(song)) {
                playlist.addItem(song);
                song.incrementInPlaylistCount();
            } else {
                playlist.removeItem(song);
                song.decrementInPlaylistCount();
            }
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudio<? extends Audio> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            return "Please load a source before adding to or removing from the playlist.";
        } else if (!CheckClass.isSong(playingAudio.getPlayingObject().getClass())) {
            return "The loaded source is not a song.";
        } else if (playlist == null) {
            return "The specified playlist does not exist.";
        } else if (playlist.itemExists((Song) playingAudio.getPlayingObject())) {
            return "Successfully added to playlist.";
        }
        return "Successfully removed from playlist.";
    }
}
