package commands.normalUser.player;

import commands.ActionCommand;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;

/**
 * implements the prev operation
 */
public final class Prev extends ActionCommand {
    private final PlayerManager playerManager;

    public Prev(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Executes the prev operation (plays the previous track from the collection)
     */
    public void execute() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            setMessage(toString());
            return;
        }
        int elapsedTime = playingAudio.getElapsedTime();
        PlayingAudioCollection<?> playingAudioCollection = playerManager.getPlayingCollection();
        if (elapsedTime >= 1 || playingAudioCollection == null) {
            playingAudio.resetRemainedTime();
            playingAudio.resume();
        } else {
            playingAudioCollection.playPrevious();
            playerManager.setPlayingAudio(playingAudioCollection.getPlayingNowObject());
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            return "Please load a source before returning to the previous track.";
        }
        String playingTrackName = playingAudio.getName();
        return "Returned to previous track successfully. The current track is "
                + playingTrackName + ".";
    }
}
