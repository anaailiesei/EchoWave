package commands.normalUser.player;

import audio.Audio;
import audio.Song;
import audio.collections.Collection;
import audio.collections.Playlist;
import commands.ActionCommand;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;

/**
 * Implements the repeat operation
 */
public final class Repeat extends ActionCommand {
    private final PlayerManager playerManager;
    private RepeatType repeatState = RepeatType.noRepeat;

    public Repeat(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Execute the repeat operation according to the repeat type
     *
     * @see RepeatType
     */
    public void execute() {
        repeatState = RepeatType.noRepeat;
        if (playerManager.getPlayingAudio() == null
                || (playerManager.getPlayingAudio().getRemainedTime() == 0
                && playerManager.getPlayingAudio().isPaused())) {
            setMessage(toString());
            return;
        }
        PlayingAudioCollection<? extends Collection<? extends Audio>> playingCollection
                = playerManager.getPlayingCollection();
        if (playingCollection != null
                && CheckClass.isPlaylist(playingCollection.getPlayingCollection().getClass())) {
            repeatPlaylist((PlayingAudioCollection<Playlist>) playingCollection);
            setMessage(toString());
            return;
        }
        PlayingAudio<? extends Audio> playingAudio = playerManager.getPlayingAudio();
        repeatSongEpisode(playingAudio);
        setMessage(toString());

    }

    @Override
    public String toString() {
        if (playerManager.getPlayingAudio() == null
                || (playerManager.getPlayingAudio().getRemainedTime() == 0
                && playerManager.getPlayingAudio().isPaused())) {
            return "Please load a source before setting the repeat status.";
        }
        return "Repeat mode changed to " + repeatState.getValue().toLowerCase() + ".";
    }

    /**
     * Repeat implementation for a playlist
     * It repeats the tracks in the playlist based on the repeat type
     *
     * @param playingPlaylist The playing playlist
     * @see RepeatType
     */
    private void repeatPlaylist(final PlayingAudioCollection<Playlist> playingPlaylist) {
        PlayingAudio<Song> playingSong = (PlayingAudio<Song>) playingPlaylist.getPlayingNowObject();
        String repeatValue = playingSong.getRepeatValue();
        if (repeatValue.equals(RepeatType.noRepeat.getValue())) {
            playingPlaylist.setPlaylistToRepeatAll();
            repeatState = RepeatType.repeatAll;
        } else if (repeatValue.equals(RepeatType.repeatAll.getValue())) {
//            playingPlaylist.setPlaylistToNoRepeat();
//            playingSong.setRepeatValue(RepeatType.repeatCurrent);
            playingPlaylist.setPlaylistToRepeat(RepeatType.repeatCurrent);
            repeatState = RepeatType.repeatCurrent;
        } else if (repeatValue.equals(RepeatType.repeatCurrent.getValue())) {
//            playingSong.setRepeatValue(RepeatType.noRepeat);
            playingPlaylist.setPlaylistToNoRepeat();
            repeatState = RepeatType.noRepeat;
        }
    }

    /**
     * Repeat implementation for a song or an episode
     * It repeats the track based on the repeat type
     *
     * @param playing The playing track
     * @see RepeatType
     */
    private void repeatSongEpisode(final PlayingAudio<? extends Audio> playing) {
        String repeatValue = playing.getRepeatValue();
        if (repeatValue.equals(RepeatType.noRepeat.getValue())) {
            playing.setRepeatValue(RepeatType.repeatOnce);
            repeatState = RepeatType.repeatOnce;
        } else if (repeatValue.equals(RepeatType.repeatOnce.getValue())) {
            playing.setRepeatValue(RepeatType.repeatInfinite);
            repeatState = RepeatType.repeatInfinite;
        } else if (repeatValue.equals(RepeatType.repeatInfinite.getValue())) {
            playing.setRepeatValue(RepeatType.noRepeat);
            repeatState = RepeatType.noRepeat;
        }
    }
}
