package commands.normalUser.playlist;

import entities.audio.Song;
import entities.audio.collections.Playlist;
import commands.ActionCommand;
import libraries.audio.PlaylistsLibrary;
import libraries.users.NormalUsersLibrary;

import java.util.ArrayList;

/**
 * CreatePlaylist operation implementation
 */
public final class CreatePlaylist extends ActionCommand {
    private boolean successfullyCreatedPlaylist = false;

    /**
     * executes the createPlaylist operation
     * @param playlistName The name of the playlist to be created
     * @param username The owner of the new playlist
     */
    public void execute(final String playlistName, final String username) {
        if (!PlaylistsLibrary.getInstance().playlistForUserExists(playlistName, username)) {
            successfullyCreatedPlaylist = false;
        } else {
            ArrayList<Song> songs = new ArrayList<>();
            Playlist newPlaylist = new Playlist(playlistName, username, songs);
            PlaylistsLibrary.getInstance().addItem(newPlaylist);
            NormalUsersLibrary.getInstance().getUserByName(username).addPlaylist(newPlaylist);
            successfullyCreatedPlaylist = true;
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        if (successfullyCreatedPlaylist) {
            return "Playlist created successfully.";
        }
        return "A playlist with the same name already exists.";
    }
}
