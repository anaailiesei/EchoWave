package commands.normalUser.player;

import entities.audio.collections.Collection;
import commands.ActionCommand;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;

/**
 * Implementation of the forward operation
 */
public final class Forward extends ActionCommand {
    private static final int SKIP_DURATION = 90;
    private final PlayerManager playerManager;

    public Forward(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Executes the forward operation based on the current playing podcast
     * It skips forward the current podcast episode by 90 seconds
     */
    public void execute() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        PlayingAudioCollection<?> playingAudioCollection = playerManager.getPlayingCollection();
        if (playingAudio == null
                || playingAudioCollection == null
                || !CheckClass.isPodcast(playingAudioCollection
                .getPlayingCollection().getClass())) {
            setMessage(toString());
            return;
        }
        if (playingAudio.getRemainedTime() < SKIP_DURATION) {
            playingAudioCollection.playNext();
            playerManager.setPlayingAudio(playingAudioCollection.getPlayingNowObject());
        } else {
            playingAudioCollection.addForwardTime(SKIP_DURATION);
        }
        setMessage(toString());
    }

    @Override
    public String toString() {
        PlayingAudio<?> playingAudio = playerManager.getPlayingAudio();
        PlayingAudioCollection<? extends Collection<?>> playingAudioCollection =
                playerManager.getPlayingCollection();
        if (playingAudio == null) {
            return "Please load a source before attempting to forward.";
        } else if (playingAudioCollection == null
                || !CheckClass.isPodcast(playingAudioCollection
                .getPlayingCollection()
                .getClass())) {
            return "The loaded source is not a podcast.";
        }
        return "Skipped forward successfully.";
    }
}
