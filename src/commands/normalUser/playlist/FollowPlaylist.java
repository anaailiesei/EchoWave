package commands.normalUser.playlist;

import entities.audio.Audio;
import entities.audio.collections.Collection;
import entities.audio.collections.Playlist;
import commands.ActionCommand;
import commands.normalUser.searchBar.audio.SelectAudio;
import libraries.users.NormalUsersLibrary;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import notifications.Notification;
import notifications.NotificationType;
import playables.PlayingAudioCollection;
import entities.user.NormalUser;

import java.util.HashMap;

/**
 * Implementation for the followPlaylist operation
 */
public final class FollowPlaylist extends ActionCommand {
    private final PlayerManager playerManager;
    private final SelectAudio select;
    private boolean followedPlaylist = false;
    private boolean sameUser = false;
    private NormalUser user;

    public FollowPlaylist(final PlayerManager playerManager, final SelectAudio select) {
        this.playerManager = playerManager;
        this.select = select;
    }

    /**
     * Executes the follow playlist operation
     * If the playlist was previously followed, it unfollows it (removes the
     * playlist from the users list of followed playlist)
     * Otherwise it follows the playlist and adds it to the followed playlists list
     * for the specified entities.user
     */
    public void execute() {
        sameUser = false;
        Audio selectedAudio = select.getSelectedObject();
        PlayingAudioCollection<? extends Collection<? extends Audio>> playingAudioCollection =
                playerManager.getPlayingCollection();
        if ((playerManager.getPlayingAudio() == null && selectedAudio == null)
                || ((playerManager.getPlayingCollection() == null
                || !CheckClass.isPlaylist(playerManager.getPlayingCollection()
                .getPlayingCollection().getClass()))
                && (selectedAudio == null
                || !CheckClass.isPlaylist(selectedAudio.getClass())))) {
            setMessage(toString());
            return;
        }
        Playlist playlist;
        if (playingAudioCollection != null) {
            playlist = (Playlist) playingAudioCollection.getPlayingCollection();
        } else {
            playlist = (Playlist) selectedAudio;
        }
        String playlistOwner = playlist.getOwner();
        String username = user.getName();
        if (playlistOwner.equals(username)) {
            sameUser = true;
            setMessage(toString());
            return;
        }
        if (!user.isPlaylistFollowed(playlist)) {
            playlist.addFollowFrom(user);
            followedPlaylist = true;
            NormalUser owner = NormalUsersLibrary.getInstance().getUserByName(playlistOwner);
            HashMap<String, String> notification = Notification
                    .getNotification(NotificationType.Follower, playlistOwner);
            assert owner != null;
            owner.update(notification);
        } else {
            playlist.removeFollowFrom(user);
            followedPlaylist = false;
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        Audio selectedAudio = select.getSelectedObject();
        if (playerManager.getPlayingAudio() == null && selectedAudio == null) {
            return "Please select a source before following or unfollowing.";
        } else if ((playerManager.getPlayingCollection() == null
                || !CheckClass.isPlaylist(playerManager
                .getPlayingCollection().getPlayingCollection().getClass()))
                && (selectedAudio == null
                || !CheckClass.isPlaylist(selectedAudio.getClass()))) {
            return "The selected source is not a playlist.";
        } else if (sameUser) {
            return "You cannot follow or unfollow your own playlist.";
        } else if (followedPlaylist) {
            return "Playlist followed successfully.";
        }
        return "Playlist unfollowed successfully.";
    }

    /**
     * Sets the user for the follow playlist operation (the user that performs the operation)
     *
     * @param newUser the user to be set
     * @return the current instance
     */
    public FollowPlaylist setUser(final NormalUser newUser) {
        this.user = newUser;
        return this;
    }
}
