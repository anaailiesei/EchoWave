package commands.normalUser.player;

import commands.ActionCommand;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;

/**
 * Implements the next operation
 */
public final class Next extends ActionCommand {
    private final PlayerManager playerManager;

    public Next(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Executes the next operation (plays the next track from the collection)
     */
    public void execute() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            setMessage(toString());
            return;
        }
        PlayingAudioCollection<?> playingAudioCollection = playerManager.getPlayingCollection();
        if (playingAudioCollection != null) {
            String currentTrackRepeatValue = playingAudio.getRepeatValue();
            if (currentTrackRepeatValue.equals(RepeatType.repeatCurrent.getValue())) {
                playingAudio.resetRemainedTime();
                playingAudio.resume();
            } else {
                playingAudioCollection.playNext();
            }
            if (playingAudioCollection.isFinished()) {
                playerManager.setPlayingAudio(null);
                playerManager.setPlayingCollection(null);
            } else {
                playerManager.setPlayingAudio(playingAudioCollection.getPlayingNowObject());
            }
        } else {
            String repeatValue = playingAudio.getRepeatValue();
            if (repeatValue.equals(RepeatType.noRepeat.getValue())) {
                playingAudio.skipToEnd();
                playerManager.setPlayingAudio(null);
            } else {
                playingAudio.resetRemainedTime();
                if (repeatValue.equals(RepeatType.repeatOnce.getValue())) {
                    playingAudio.setRepeatValue(RepeatType.noRepeat);
                }
                playingAudio.resume();
            }
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            return "Please load a source before skipping to the next track.";
        }
        String playingTrackName = playingAudio.getName();
        return "Skipped to next track successfully. The current track is " + playingTrackName + ".";
    }
}
