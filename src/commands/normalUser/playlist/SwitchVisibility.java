package commands.normalUser.playlist;

import audio.collections.Playlist;
import commands.ActionCommand;

public final class SwitchVisibility extends ActionCommand {
    private Playlist playlist;

    /**
     * Executes the switch visibility operation
     * if playlist was public before it switches it to private,
     * and vice-versa
     */
    public void execute() {
        if (playlist == null) {
            setMessage(toString());
            return;
        }
        playlist.switchVisibility();
        setMessage(toString());
    }

    @Override
    public String toString() {
        if (playlist == null) {
            return "The specified playlist ID is too high.";
        }
        return "Visibility status updated successfully to " + playlist.getVisibility() + ".";
    }

    /**
     * Sets the playlist for which we want to change the visibility
     * @param newPlaylist The playlist to be set
     * @return the current instance
     */
    public SwitchVisibility setPlaylist(final Playlist newPlaylist) {
        this.playlist = newPlaylist;
        return this;
    }
}
