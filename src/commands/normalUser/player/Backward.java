package commands.normalUser.player;

import audio.collections.Collection;
import commands.ActionCommand;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;

/**
 * Implementation of the Backward operation
 */
public final class Backward extends ActionCommand {
    private final PlayerManager playerManager;
    private static final int REWIND_DURATION = 90;
    public Backward(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Executes the backward operation based on the current playing podcast
     * It rewinds the current episode with 90 seconds
     */
    public void execute() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        PlayingAudioCollection<?> playingAudioCollection = playerManager.getPlayingCollection();
        if (playingAudio == null
                || playingAudioCollection == null
                || !CheckClass.isPodcast(playingAudioCollection
                .getPlayingCollection()
                .getClass())) {
            setMessage(toString());
            return;
        }
        if (playingAudio.getElapsedTime() < REWIND_DURATION) {
            playingAudio.resetRemainedTime();
        } else {
            playingAudioCollection.addBackwardTime(REWIND_DURATION);
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        PlayingAudioCollection<? extends Collection<?>> playingAudioCollection =
                playerManager.getPlayingCollection();
        if (playingAudio == null) {
            return "Please select a source before rewinding.";
        } else if (playingAudioCollection == null
                || !CheckClass.isPodcast(playingAudioCollection.getPlayingCollection()
                .getClass())) {
            return "The loaded source is not a podcast.";
        }
        return "Rewound successfully.";
    }
}
