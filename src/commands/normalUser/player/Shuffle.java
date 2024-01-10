package commands.normalUser.player;

import commands.ActionCommand;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Shuffle extends ActionCommand {
    private final PlayerManager playerManager;
    private int seed;
    private List<Integer> shuffledIndexes = null;

    public Shuffle(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Execute the shuffle operation
     * If shuffle was previously disabled, it enables shuffling and sets the shuffled
     * indexes list
     * Otherwise, it disables shuffling
     */
    public void execute() {
        PlayingAudioCollection<?> playingAudioCollection = playerManager.getPlayingCollection();
        if (playerManager.getPlayingAudio() == null
                || playingAudioCollection == null
                || (!CheckClass.isPlaylist(playingAudioCollection
                .getPlayingCollection().getClass())
                && !CheckClass.isAlbum(playingAudioCollection.getPlayingCollection().getClass()))) {
            setMessage(toString());
            return;
        }
        int collectionSize = playingAudioCollection.getSize();
        playingAudioCollection.setShuffle();
        if (playingAudioCollection.isShuffled()) {
            if (shuffledIndexes == null) {
                shuffledIndexes = generateShuffledIndexList(collectionSize);
                playingAudioCollection.setShuffledIndexes(shuffledIndexes);
            }
            PlayingAudio<?> playingAudio = playingAudioCollection.getPlayingNowObject();
            int remainedTime = playingAudio.getRemainedTime();
            if (remainedTime == 0) {
                playingAudio.resetRemainedTime();
                playingAudio.resume();
            }
        } else {
            shuffledIndexes = null;
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudioCollection<?> playingAudioCollection = playerManager.getPlayingCollection();
        if (playerManager.getPlayingAudio() == null) {
            return "Please load a source before using the shuffle function.";
        } else if (playingAudioCollection == null
                || (!CheckClass.isPlaylist(playingAudioCollection
                .getPlayingCollection().getClass())
                && !CheckClass.isAlbum(playingAudioCollection.getPlayingCollection().getClass()))) {
            return "The loaded source is not a playlist or an album.";
        } else if (playingAudioCollection.isShuffled()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Generated an ordered list with indexes from 0 to the specified number
     *
     * @param num The number of indexes (the size of the list)
     * @return The list of ordered indexes
     */
    private ArrayList<Integer> generateOrderedIndexList(final int num) {
        ArrayList<Integer> orderedIndexList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            orderedIndexList.add(i);
        }
        return orderedIndexList;
    }

    /**
     * Generates a shuffled list of indexes based on the specified seed
     * using the {@code Random} class
     *
     * @param num the number of indexes (length of the list)
     * @return a list of shuffled indexes
     * @see Random
     * @see Collections
     */
    private ArrayList<Integer> generateShuffledIndexList(final int num) {
        ArrayList<Integer> shuffledIndexList = generateOrderedIndexList(num);
        Collections.shuffle(shuffledIndexList, new Random(seed));
        return shuffledIndexList;
    }

    /**
     * Sets the seed for the shuffling command
     * Must be used before the execute command
     *
     * @param newSeed The seed to be set
     * @return the current instance
     */
    public Shuffle setSeed(final int newSeed) {
        this.seed = newSeed;
        return this;
    }

    /**
     * Sets the shuffled indexes list
     *
     * @param shuffledIndexes The list of indexes to be set
     */
    public void setShuffledIndexes(final List<Integer> shuffledIndexes) {
        this.shuffledIndexes = shuffledIndexes;
    }
}
