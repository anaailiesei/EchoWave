package managers.normalUser;

import entities.audio.Audio;
import entities.audio.collections.Collection;
import managers.CheckClass;
import playables.PlayingAudioCollection;
import entities.user.NormalUser;

import java.util.ArrayList;

public final class ProgressManager {
    private final NormalUser user;

    public ProgressManager(final NormalUser user) {
        this.user = user;
    }
    private ArrayList<PlayingAudioCollection<? extends Collection<?>>> inProgressCollections;

    private void initInProgressCollections() {
        inProgressCollections = new ArrayList<>();
    }

    /**
     * Adds a collection to the progress manager
     *
     * @param playingCollection the collection to add
     */
    public void addInProgressCollection(
            final PlayingAudioCollection<? extends Collection<? extends Audio>> playingCollection) {
        inProgressCollections.add(playingCollection);
    }

    /**
     * Searches for a collection (loaded) that's still in progress
     * Must be checked before if the loaded entities.audio is a collection
     *
     * @param loadedAudio The loaded track
     * @return the playable entities.audio collection found
     */
    public PlayingAudioCollection<? extends Collection<? extends Audio>>
    findInProgressCollections(final Audio loadedAudio) {
        if (inProgressCollections == null) {
            initInProgressCollections();
        }

        for (PlayingAudioCollection<? extends Collection<? extends Audio>> playingAudioCollection
                : inProgressCollections) {
            if (playingAudioCollection.getPlayingCollection().equals(loadedAudio)) {
                return playingAudioCollection;
            }
        }
        Collection<? extends Audio> loadedCollection = (Collection<? extends Audio>) loadedAudio;
        if (CheckClass.isPodcast(loadedCollection.getClass())) {
            PlayingAudioCollection<? extends Collection<? extends Audio>> newPlayingCollection =
                    new PlayingAudioCollection<>(loadedCollection, user);
            addInProgressCollection(newPlayingCollection);
            return newPlayingCollection;
        }
        return null;
    }

}
