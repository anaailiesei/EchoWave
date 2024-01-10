package playables;

import audio.Audio;
import audio.Song;
import audio.collections.Album;
import audio.collections.Collection;
import commands.normalUser.player.RepeatType;
import commands.normalUser.player.StatusFields;
import libraries.users.ArtistsLibrary;
import user.Artist;
import user.NormalUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PlayingAudioCollection<T extends Collection<? extends Audio>>
        implements Playing {
    private T playingCollection;
    private ArrayList<PlayingAudio<? extends Audio>> playableObjects;
    private int playingNowIndex = 0;
    private boolean repeatCollection = false;
    private boolean shuffleCollection = false;
    private List<Integer> shuffledIndexes = null;
    private boolean finished = false;
    private final NormalUser user;
    private Artist artist;

    public PlayingAudioCollection(final Collection<? extends Audio> collection, final NormalUser user) {
        playingCollection = (T) collection;
        playableObjects = new ArrayList<>();
        for (Audio item : collection.getCollection()) {
            playableObjects.add(new PlayingAudio<>(item, user));
        }
        this.user = user;
        artist = ArtistsLibrary.getInstance().getArtistByName(collection.getOwner());
    }

    /**
     * Plays the next song or episode if possible, else it finished the current playing collection
     */
    public void playNext() {
        if (!isNextPlayable()) {
            if (!repeatCollection) {
                finished = true;
                return;
            }
            finished = false;
            replay(0);
            return;
        }
        playableObjects.get(playingNowIndex).pause();
        int oldPlayingNowIndex = playingNowIndex;
        if (!isShuffled()) {
            playingNowIndex++;
        } else {
            int currentShuffledIndex = getCurrentShuffledIndex();
            if (currentShuffledIndex >= playableObjects.size() - 1) {
                if (!repeatCollection) {
                    finished = true;
                    return;
                }
                finished = false;
                replay(shuffledIndexes.get(0));
                return;
            }
            playingNowIndex = getNextShuffledIndex(currentShuffledIndex);
        }
        playableObjects.get(oldPlayingNowIndex).resetRemainedTime();
        playableObjects.get(playingNowIndex).resume();
    }

    private boolean isNextPlayable() {
        return !(!isShuffled() && playingNowIndex >= playableObjects.size() - 1);
    }

    /**
     * Must be used only after isShufled is set
     *
     * @return
     */
    private int getCurrentShuffledIndex() {
        if (!isShuffled()) {
            return playingNowIndex;
        }
        return shuffledIndexes.indexOf(playingNowIndex);
    }

    /**
     * Must be checked beforehand if it's possible, meaning that current shuffled index is
     * not out of bound
     *
     * @param currentShuffledIndex
     * @return
     */
    private int getNextShuffledIndex(final int currentShuffledIndex) {
        if (!isShuffled()) {
            return playingNowIndex++;
        }
        return shuffledIndexes.get(currentShuffledIndex + 1);
    }

    /**
     * Must be checked beforehand if it's possible, meaning that current shuffled index is
     * not out of bound
     *
     * @param currentShuffledIndex
     * @return
     */
    private int getPrevShuffledIndex(final int currentShuffledIndex) {
        if (!isShuffled()) {
            return playingNowIndex--;
        }
        return shuffledIndexes.get(currentShuffledIndex - 1);
    }

    private boolean isPrevPlayable() {
        return playingNowIndex > 0;
    }

    /**
     * Plays the previous episode or song
     */
    public void playPrevious() {
        if (!isPrevPlayable()) {
            getPlayingNowObject().resetRemainedTime();
            if (!isShuffled()) {
                getPlayingNowObject().resume();
                return;
            }
            getPlayingNowObject().pause();
            int shuffledIndex = getCurrentShuffledIndex();
            if (shuffledIndex != 0) {
                playingNowIndex = shuffledIndexes.get(shuffledIndex - 1);
            }
            getPlayingNowObject().resume();
            return;
        }

        if (isShuffled()) {
            int currentShuffledIndex = getCurrentShuffledIndex();
            if (currentShuffledIndex == 0) {
                getPlayingNowObject().resetRemainedTime();
                getPlayingNowObject().resume();
                return;
            }
        }
        int oldPlayingNowIndex = playingNowIndex;
        playableObjects.get(playingNowIndex).pause();
        if (!isShuffled()) {
            playingNowIndex--;
        } else {
            int currentShuffledIndex = getCurrentShuffledIndex();
            playingNowIndex = getPrevShuffledIndex(currentShuffledIndex);
        }
        playableObjects.get(oldPlayingNowIndex).resetRemainedTime();
        playableObjects.get(playingNowIndex).resume();
    }

    /**
     * Gets the playing now Audio file as a playable object
     *
     * @return The playable object
     */
    public PlayingAudio<? extends Audio> getPlayingNowObject() {
        if (playableObjects != null) {
            return playableObjects.get(playingNowIndex);
        }
        return null;
    }

    /**
     * Adds time passed for the collection
     * Adds the time passed for the current playing track and plays
     * the next track if necessary or stops the track if it's finished
     *
     * @param timePassed the time that has passed
     */
    public void addTimePassed(final int timePassed) {
        if (playableObjects == null) {
            return;
        }
        PlayingAudio<? extends Audio> playingNowObject = playableObjects.get(playingNowIndex);
        Map<StatusFields, Object> stats = playingNowObject.getStats();
        if ((boolean) stats.get(StatusFields.paused)) {
            return;
        }
        int oldRemainedTime = (int) stats.get(StatusFields.remainedTime);
        String oldRepeatValue = playingNowObject.getRepeatValue();
        // TODO: Add to artists album count when a song is on repeat
        playingNowObject.addTimePassed(timePassed);
        if (oldRepeatValue.equals(RepeatType.repeatInfinite.getValue())
                || oldRepeatValue.equals(RepeatType.repeatCurrent.getValue())) {
            return;
        }

        int remainder = oldRemainedTime - timePassed;
        if (oldRepeatValue.equals(RepeatType.repeatOnce.getValue()) && remainder < 0) {
            remainder += playingNowObject.getDuration();
        }

        int oldPlayingNowIndex = playingNowIndex;
        if (remainder <= 0) {
            if (isNextPlayable()) {
                playNext();
            } else {
                if (repeatCollection) {
                    setFinished(false);
                    if (isShuffled()) {
                        replay(shuffledIndexes.get(0));
                    } else {
                        replay(0);
                    }
                } else {
                    setFinished(true);
                    pause();
                }
            }
            if (!finished) {
                int newPlayingNowIndex = playingNowIndex;
                // TODO: add listends here
                if (oldPlayingNowIndex != newPlayingNowIndex) {
                    getPlayingNowObject().getPlayingObject().addListen(user.getApp().getListenTracker());
                    playingCollection.addListen(user.getApp().getListenTracker());
                    if (artist != null) {
                        artist.getListenTracker().addListenAll((Album) playingCollection,
                                (Song) getPlayingNowObject().getPlayingObject(),
                                user);
                    }
                }
            }
            addTimePassed(-remainder);
        }
    }

    /**
     * Sets repeat type of all playable songs in a playlist to the indicated repeat type
     * Must be used only for playlists
     *
     * @param repeatType The repeat type to be set
     */
    public void setPlaylistToRepeat(final RepeatType repeatType) {
        for (PlayingAudio<?> playingSong : playableObjects) {
            playingSong.setRepeatValue(repeatType);
        }
    }

    /**
     * Sets all playable songs in the playlist to repeat all
     * Must be used only for playlists
     */
    public void setPlaylistToRepeatAll() {
        repeatCollection = true;
        setPlaylistToRepeat(RepeatType.repeatAll);
    }

    /**
     * Sets all playable songs in the playlist to no repeat
     * Must be used only for playlists
     */
    public void setPlaylistToNoRepeat() {
        repeatCollection = false;
        setPlaylistToRepeat(RepeatType.noRepeat);
    }

    /**
     * replays the collection starting from the specified index
     */
    public void replay(final int index) {
        playingNowIndex = index;
        for (PlayingAudio<?> playingAudio : playableObjects) {
            playingAudio.resetRemainedTime();
        }
        playableObjects.get(playingNowIndex).resume();
        setFinished(false);
    }

    @Override
    public void pause() {
        playableObjects.get(playingNowIndex).pause();
    }

    @Override
    public void resume() {
        playableObjects.get(playingNowIndex).resume();
    }

    public int getPlayingNowIndex() {
        return playingNowIndex;
    }

    public T getPlayingCollection() {
        return playingCollection;
    }

    public boolean isRepeatCollection() {
        return repeatCollection;
    }

    public void setRepeatCollection(final boolean repeatCollection) {
        this.repeatCollection = repeatCollection;
    }

    /**
     * Gets the size of the collection (number of items)
     *
     * @return the size
     */
    public int getSize() {
        return playingCollection.getSize();
    }

    /**
     * Checks if the collection is shuffled
     *
     * @return {@code true} if it's shuffled, {@code false} otherwise
     */
    public boolean isShuffled() {
        return shuffleCollection;
    }

    /**
     * Sets shuffle to true for the collection as well as for every item in the collection
     */
    private void setShuffleTrue() {
        shuffleCollection = true;
        setShuffleAll(true);
    }

    /**
     * Sets shuffle to false for the collection as well as for every item in the collection
     */
    public void setShuffleFalse() {
        shuffleCollection = false;
        setShuffleAll(false);
    }

    /**
     * Set the shuffle flag for the collection
     * If it was previously set to {@code true}, set it to false
     * If it was previously set to {@code false}, set it to true
     */
    public void setShuffle() {
        if (!shuffleCollection) {
            setShuffleTrue();
            return;
        }
        setShuffleFalse();
    }

    /**
     * Sets all items in the collection to the value indicated by shuffle (true or false)
     *
     * @param shuffle the value to be set
     */
    private void setShuffleAll(final boolean shuffle) {
        for (PlayingAudio<?> playingAudio : playableObjects) {
            playingAudio.setShuffle(shuffle);
        }
    }

    /**
     * Checks if track is finished
     *
     * @return {@code true} if the track is finished, {@code false} otherwise
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets the finished flag to the specified value
     *
     * @param finished the value to be set (true or false)
     */
    public void setFinished(final boolean finished) {
        this.finished = finished;
    }

    /**
     * Must only be used if we are sure that the track wouldn't finish after the specified time
     * Plays the track forward by the specified time
     *
     * @param forwardTime
     */
    public void addForwardTime(final int forwardTime) {
        getPlayingNowObject().addForwardTime(forwardTime);
    }

    /**
     * Must only be used if we are sure that at least the specified time has elapsed
     * Plays the track backward by the specified time
     *
     * @param backwardTime
     */
    public void addBackwardTime(final int backwardTime) {
        getPlayingNowObject().addBackwardTime(backwardTime);
    }

    /**
     * Sets the shuffled indexes list after the shuffle is enabled
     *
     * @param shuffledIndexes the list to be set
     */
    public void setShuffledIndexes(final List<Integer> shuffledIndexes) {
        this.shuffledIndexes = shuffledIndexes;
    }
}
