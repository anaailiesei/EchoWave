package commands.normalUser.player;

import audio.Song;
import audio.collections.Album;
import commands.ActionCommand;
import libraries.audio.AlbumsLibrary;
import libraries.users.ArtistsLibrary;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import user.Artist;
import user.NormalUser;

/**
 * Implements the like operation
 */
public final class Like extends ActionCommand {
    private final PlayerManager playerManager;
    private boolean liked = false;
    private NormalUser user;

    public Like(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Set the suer for the Like command
     *
     * @param newUser the user to be set
     * @return the current instance
     */
    public Like setUser(final NormalUser newUser) {
        this.user = newUser;
        return this;
    }

    /**
     * Execute the like operation for the current playing song
     * If song is already liked, unlike it and erase it from the user's list of liked songs
     * Otherwise, like the song and add it to user's list of liked songs
     */
    public void execute() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null
                || !CheckClass.isSong(playingAudio.getPlayingObject().getClass())) {
            setMessage(toString());
            return;
        }
        Song song = (Song) playingAudio.getPlayingObject();
        Album album = AlbumsLibrary.getInstance().getAlbumByName(song.getAlbum());
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(song.getArtistName());
        if (user.isSongLiked((song))) {
            song.removeLikeFrom(user);
            if (album != null) {
                album.removeLike();
            }
            if (artist != null) {
                artist.removeLike();
            }
            liked = false;
        } else {
            song.addLikeFrom(user);
            if (album != null) {
                album.addLike();
            }
            if (artist != null) {
                artist.addLike();
            }
            liked = true;
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            return "Please load a source before liking or unliking.";
        } else if (!CheckClass.isSong(playingAudio.getPlayingObject().getClass())) {
            return "Loaded source is not a song.";
        } else if (liked) {
            return "Like registered successfully.";
        }
        return "Unlike registered successfully.";
    }
}
