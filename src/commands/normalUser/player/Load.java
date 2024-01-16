package commands.normalUser.player;

import commands.ActionCommand;
import entities.audio.Audio;
import entities.audio.Episode;
import entities.audio.Song;
import entities.audio.collections.Album;
import entities.audio.collections.Collection;
import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import lombok.Getter;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import managers.normalUser.ProgressManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;
import statistics.listenTrackers.ListenTrackerArtist;
import statistics.listenTrackers.ListenTrackerHost;
import statistics.listenTrackers.ListenTrackerNormalUser;

public class Load extends ActionCommand {
    private final PlayerManager playerManager;
    private final Shuffle shuffle;
    private final ListenTrackerNormalUser listenTracker;
    @Getter
    private boolean successfullyLoaded = false;
    /**
     * -- GETTER --
     *  Checks if source was successfully loaded
     */
    private NormalUser user;

    public Load(final PlayerManager playerManager,
                     final Shuffle shuffle,
                     final ListenTrackerNormalUser listenTracker) {
        this.playerManager = playerManager;
        this.shuffle = shuffle;
        this.listenTracker = listenTracker;
    }

    /**
     * Sets the user for the load operation
     *
     * @param newUser The user to be set
     * @return the current instance
     * @see NormalUser
     */
    public Load setUser(final NormalUser newUser) {
        this.user = newUser;
        return this;
    }

    /**
     * Executes the load operation if the source was selected
     */
    public boolean execute(final Audio selection) {
         successfullyLoaded = false;
        if (selection != null) {
            playerManager.setLoadedObject(selection);
            ProgressManager progressManager = user.getProgressManager();
            if (CheckClass.extendsCollection(selection.getClass())) {
                PlayingAudioCollection<? extends Collection<? extends Audio>> playingCollection =
                        progressManager.findInProgressCollections(selection);
                if (playingCollection == null) {
                    Collection<? extends Audio> selectedCollection
                            = (Collection<? extends Audio>) selection;
                    playingCollection = new PlayingAudioCollection<>(selectedCollection, user);
                }
                playerManager.setPlayingCollection(playingCollection);

                playerManager.setPlayingAudio(playingCollection.getPlayingNowObject());
                Audio playingAudio = playingCollection.getPlayingNowObject().getPlayingObject();

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
                Host host = HostsLibrary.getInstance()
                        .getHostByName(playingCollection.getPlayingCollection().getOwner());
                if (host != null) {
                    ListenTrackerHost listenTrackerHost = host.getListenTracker();
                    listenTrackerHost
                            .addListenAll((Episode) playingAudio,
                                    user);
                }
            } else {
                playerManager.setPlayingCollection(null);
                PlayingAudio<?> playingAudio = new PlayingAudio<>(selection, user);
                playerManager.setPlayingAudio(playingAudio);
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
                Host host = HostsLibrary.getInstance()
                        .getHostByName(playingAudio.getPlayingObject().getOwner());
                if (host != null) {
                    ListenTrackerHost listenTrackerHost = host.getListenTracker();
                    listenTrackerHost
                            .addListenAll((Episode) playingAudio.getPlayingObject(),
                                    user);
                }
            }
            playerManager.getPlayingAudio().resume();
            playerManager.removeAd();
            successfullyLoaded = true;
            shuffle.setShuffledIndexes(null);
        }
        return successfullyLoaded;
    }
    /**
     * Executes the load operation if the source was selected
     */
    public void execute() {
    }
}
