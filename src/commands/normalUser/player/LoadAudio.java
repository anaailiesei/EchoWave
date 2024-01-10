package commands.normalUser.player;

import entities.audio.Audio;
import entities.audio.Song;
import entities.audio.collections.Album;
import entities.audio.collections.Collection;
import commands.ActionCommand;
import commands.normalUser.searchBar.audio.SelectAudio;
import libraries.users.ArtistsLibrary;
import lombok.Getter;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import managers.normalUser.ProgressManager;
import managers.normalUser.SearchBarManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;
import statistics.listenTrackers.ListenTrackerArtist;
import statistics.listenTrackers.ListenTrackerNormalUser;
import entities.user.Artist;
import entities.user.NormalUser;

/**
 * Implements the load operation
 */
public final class LoadAudio extends ActionCommand {
    private final PlayerManager playerManager;
    private final SearchBarManager searchBarManager;
    private final SelectAudio select;
    private final Shuffle shuffle;
    private final ListenTrackerNormalUser listenTracker;
    /**
     * -- GETTER --
     *  Checks if source was successfully loaded
     *
     * @return {@code true} if it was successfully loaded, {@code false} otherwise
     */
    @Getter
    private boolean successfullyLoaded = false;
    private NormalUser user;

    public LoadAudio(final PlayerManager playerManager,
                     final SearchBarManager searchBarManager,
                     final Shuffle shuffle,
                     final SelectAudio selectAudio,
                     final ListenTrackerNormalUser listenTracker) {
        this.playerManager = playerManager;
        this.searchBarManager = searchBarManager;
        this.select = selectAudio;
        this.shuffle = shuffle;
        this.listenTracker = listenTracker;
    }

    /**
     * Sets the entities.user for the load operation
     *
     * @param newUser The entities.user to be set
     * @return the current instance
     * @see NormalUser
     */
    public LoadAudio setUser(final NormalUser newUser) {
        this.user = newUser;
        return this;
    }

    /**
     * Executes the load operation if the source was selected
     */
    public void execute() {
        successfullyLoaded = false;
        if (searchBarManager.getStatus().equals(SearchBarManager.SearchBarStatus.selecting)
                && select.getSelectedObject() != null) {
            Audio selectedObject = select.getSelectedObject();
            playerManager.setLoadedObject(selectedObject);
            ProgressManager progressManager = user.getProgressManager();
            if (CheckClass.extendsCollection(selectedObject.getClass())) {
                PlayingAudioCollection<? extends Collection<? extends Audio>> playingCollection =
                        progressManager.findInProgressCollections(selectedObject);
                // TODO: remove this
//                if (playingCollection.isFinished()) {
//                    playingCollection.replay(0);
//                    playingCollection.setShuffleFalse();
//                }
                if (playingCollection == null) {
                    Collection<? extends Audio> selectedCollection
                            = (Collection<? extends Audio>) selectedObject;
                    playingCollection = new PlayingAudioCollection<>(selectedCollection, user);
                }
                playerManager.setPlayingCollection(playingCollection);

                playerManager.setPlayingAudio(playingCollection.getPlayingNowObject());
                Audio playingAudio = playingCollection.getPlayingNowObject().getPlayingObject();

                // TODO: add listends here
                playingCollection.getPlayingCollection().addListen(listenTracker);
                playingAudio.addListen(listenTracker);

                Artist artist = ArtistsLibrary.getInstance()
                        .getArtistByName(playingCollection.getPlayingCollection().getOwner());
                if (artist != null) {
                    ListenTrackerArtist listenTrackerArtist = artist.getListenTracker();
                    listenTrackerArtist
                            .addListenAll((Album) playingCollection.getPlayingCollection(),
                            (Song) playingAudio,
                            user);
                }
            } else {
                playerManager.setPlayingCollection(null);
                PlayingAudio<?> playingAudio = new PlayingAudio<>(selectedObject, user);
                playerManager.setPlayingAudio(playingAudio);

                // TODO: add listens here
                playingAudio.getPlayingObject().addListen(listenTracker);
                Artist artist = ArtistsLibrary.getInstance()
                        .getArtistByName(playingAudio.getPlayingObject().getOwner());
                if (artist != null) {
                    String albumName = ((Song) playingAudio.getPlayingObject()).getAlbum();
                    ListenTrackerArtist listenTrackerArtist = artist.getListenTracker();
                    listenTrackerArtist.addListenAll(albumName,
                            (Song) playingAudio.getPlayingObject(),
                            user);
                    user.getApp().getListenTracker().addListen(albumName);
                }
            }
            playerManager.getPlayingAudio().resume();
            playerManager.removeAd();
            successfullyLoaded = true;
            shuffle.setShuffledIndexes(null);
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        if (!successfullyLoaded) {
            return "Please select a source before attempting to load.";
        } else if (CheckClass.extendsCollection(playerManager.getLoadedObject().getClass())) {
            Collection<?> loadedCollection = (Collection<?>) playerManager
                    .getLoadedObject();
            if (loadedCollection.isEmpty()) {
                return "You can't load an empty entities.audio collection!";
            }
        }
        return "Playback loaded successfully.";
    }
}
