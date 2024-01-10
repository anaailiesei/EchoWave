package commands.normalUser.player;

import entities.audio.Audio;
import commands.ActionCommand;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;

/**
 * Implements the playPause operation
 */
public final class PlayPause extends ActionCommand {
    private final PlayerManager playerManager;

    public PlayPause(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Executes the play pause operation
     * If track was previously paused, it resumes the track
     * Otherwise, if the track is playing, pause the track
     */
    public void execute() {
        PlayingAudio<? extends Audio> playingAudio = playerManager.getPlayingAudio();
        if (playingAudio == null) {
            setMessage(toString());
            return;
        }
        boolean isPaused = playingAudio.isPaused();
        if (isPaused) {
            playingAudio.resume();
            playerManager.resumePlayer();
        } else {
            playingAudio.pause();
            playerManager.pausePlayer();
        }
        setMessage(toString());
    }

    /**
     * Creates the message for the command {@code playPause}
     * This should be used only after the method execute from the {@code PlayPause} class
     * and after player's manager resume/pause was set properly
     *
     * @return the message of the command {@code playPause}
     */
    @Override
    public String toString() {
        if (playerManager.getPlayingAudio() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        } else if (playerManager.getStatus() == PlayerManager.PlayerStatus.paused) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }
}
