package managers.normalUser;

import commands.CommandType;
import commands.normalUser.player.AddRemoveInPlaylist;
import commands.normalUser.player.Backward;
import commands.normalUser.player.Forward;
import commands.normalUser.player.Like;
import commands.normalUser.player.Load;
import commands.normalUser.player.LoadAudio;
import commands.normalUser.player.LoadRecommendation;
import commands.normalUser.player.Next;
import commands.normalUser.player.PlayPause;
import commands.normalUser.player.Prev;
import commands.normalUser.player.Repeat;
import commands.normalUser.player.RepeatType;
import commands.normalUser.player.Shuffle;
import commands.normalUser.player.StatusFields;
import commands.normalUser.playlist.SwitchVisibility;
import entities.audio.Audio;
import entities.audio.Song;
import entities.audio.collections.Collection;
import entities.audio.collections.Playlist;
import entities.user.NormalUser;
import fileio.input.CommandInput;
import fileio.output.Output;
import libraries.audio.SongsLibrary;
import libraries.users.NormalUsersLibrary;
import lombok.Getter;
import managers.CheckClass;
import managers.TimeChangeListener;
import managers.TimeManager;
import managers.commands.CommandHandler;
import playables.PlayingAudio;
import playables.PlayingAudioCollection;
import statistics.calculator.FreeSongCalculateRevenue;
import statistics.calculator.RevenueCalculator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for managing the player
 */
public final class PlayerManager implements TimeChangeListener, CommandHandler {
    private static Map<StatusFields, Object> emptyStats = null;
    private final AppManager app;
    private final CommandManager commandManager;
    private final int adDuration = SongsLibrary.getAdDuration();
    private final RevenueCalculator calculator;
    private boolean adBreak = false;
    @Getter
    private Integer adPrice = 0;
    private int adRemainedTime = 0;
    /**
     * -- GETTER --
     * Gets the current status of player
     */
    @Getter
    private PlayerStatus status = PlayerStatus.idle;
    @Getter
    private Audio loadedObject;
    /**
     * -- GETTER --
     * Get the current playing audio file
     */
    @Getter
    private PlayingAudio<? extends Audio> playingAudio;
    @Getter
    private PlayingAudioCollection<? extends Collection<? extends Audio>> playingCollection;

    public PlayerManager(final AppManager parentApp) {
        this.app = parentApp;
        this.commandManager = app.getCommandManager();
        TimeManager.getInstance().addTimeChangeListener(this);
        calculator = new RevenueCalculator();
    }

    /**
     * Method for deciding what happens when the time changes
     *
     * @param timeDifference the time difference between the old time and the new current time
     *                       that the player gets notified with
     */
    @Override
    public void onTimeChanged(final int timeDifference) {
        if (!app.isOnline()) {
            return;
        }

        if (playingAudio == null) {
            return;
        }
        Map<StatusFields, Object> stats = getPlayingAudio().getStats();
        int remainedTime = (int) stats.get(StatusFields.remainedTime);

        if (!adBreak || timeDifference < remainedTime) {
            onTimeChangedHelper(timeDifference);
            return;
        }

        onTimeChangedHelper(remainedTime - 1);
        if (adRemainedTime == adDuration) {
            TreeMap<Song, Integer> freeSongs = app.getListenTracker().getFreeSongs();
            calculator.calculateRevenue(new FreeSongCalculateRevenue(freeSongs, adPrice));
            app.getListenTracker().emptyFreeSongs();
        }

        int timeDiffRemainedTime = timeDifference - remainedTime;
        if (timeDiffRemainedTime < adRemainedTime) {
            adRemainedTime -= timeDiffRemainedTime;
            return;
        }
        adBreak = false;
        adPrice = 0;
        adRemainedTime = 0;
        onTimeChangedHelper(timeDiffRemainedTime - adDuration + 1);
    }

    /**
     * Method that's used for implementing some of timeChanged logic
     * (to avoid recursive method)
     *
     * @param timeDifference The time passed that should be added
     */
    public void onTimeChangedHelper(final int timeDifference) {
        if (playingAudio != null) {
            Map<StatusFields, Object> stats = playingAudio.getStats();
            int remainedTime = (int) stats.get(StatusFields.remainedTime);
            if (remainedTime > 0) {
                decrementLoadedCountForAudio();
            }
        }
        if (playingCollection != null) {
            playingCollection.addTimePassed(timeDifference);
            setPlayingAudio(playingCollection.getPlayingNowObject());
            if (!playingCollection.isFinished()) {
                playingAudio = playingCollection.getPlayingNowObject();
                incrementLoadedCountForAudio();
            } else {
                decrementLoadedCountForCollection();
                playingAudio = null;
                playingCollection = null;
                loadedObject = null;
            }
            return;
        }
        if (playingAudio != null) {
            playingAudio.addTimePassed(timeDifference);
            Map<StatusFields, Object> stats = playingAudio.getStats();
            int remainedTime = (int) stats.get(StatusFields.remainedTime);
            if (remainedTime > 0) {
                incrementLoadedCountForAudio();
            } else {
                playingAudio = null;
            }
        }
    }

    /**
     * Set the current playing audio file
     *
     * @param playingAudio the audio file to be set
     */
    public void setPlayingAudio(final PlayingAudio<? extends Audio> playingAudio) {
        this.playingAudio = playingAudio;
    }

    /**
     * Sets the current state of the player
     *
     * @param status the status to be set
     */
    public void setStatus(final PlayerStatus status) {
        this.status = status;
    }

    /**
     * Perform the load operation
     *
     * @param command the command that specifies the load command parameters
     * @return an {@code Output} object with the command and message info
     * @see LoadAudio
     */
    public Output performLoad(final CommandInput command) {
        LoadAudio load = commandManager.getLoad();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(command.getUsername());
        loadHelper(load, user);
        String message = load.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the load recommendation operation
     *
     * @param command the command that specifies the load recommendation command parameters
     * @return an {@code Output} object with the command and message info
     * @see LoadAudio
     */
    public Output performLoadRecommendation(final CommandInput command) {
        LoadRecommendation load = commandManager.getLoadRecommendation();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(command.getUsername());
        loadHelper(load, user);
        String message = load.getMessage();
        return new Output(command, message);
    }

    /**
     * Helps with load execution
     * (either load audio or load recommendation, based on the given argument)
     *
     * @param load The load instance to use for loading
     * @param user The user for which the load is performed
     */
    private void loadHelper(final Load load, final NormalUser user) {
        if (playingAudio != null) {
            decrementLoadedCountForAudio();
            if (playingCollection != null) {
                decrementLoadedCountForCollection();
            }
            setStatus(PlayerStatus.paused);
            playingAudio.pause();
        }
        load.setUser(user).execute();
        if (load.isSuccessfullyLoaded()) {
            setStatus(PlayerStatus.playing);
            app.getSearchBarManager().setSearchBarToIdle();
        } else {
            if (playingAudio != null) {
                playingAudio.resume();
                setStatus(PlayerStatus.playing);
            }
        }
        if (playingCollection != null) {
            incrementLoadedCountForCollection();
        }
        incrementLoadedCountForAudio();
    }

    /**
     * Perform the play / pause command
     *
     * @param command the command that specifies the playPause command parameters
     * @return an {@code Output} object with the command and message info
     * @see PlayPause
     */
    public Output performPlayPause(final CommandInput command) {
        PlayPause playPause = commandManager.getPlayPause();
        playPause.execute();
        String message = playPause.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the status command
     *
     * @param command the command that specifies the status command parameters
     * @return an {@code Output} object with the command and the stats of the playing track
     */
    public Output performStatus(final CommandInput command) {
        Map<StatusFields, Object> stats;
        if (playingAudio == null) {
            stats = getEmptyStats();
        } else {
            stats = playingAudio.getStats();
            int remainedTime = (int) stats.get(StatusFields.remainedTime);
            if (remainedTime == 0) {
                stats = getEmptyStats();
            }
        }
        return new Output(command, stats);
    }

    /**
     * Gets a map with default stats used for when a track finished playing
     *
     * @return The empty stats map
     */
    private Map<StatusFields, Object> getEmptyStats() {
        if (emptyStats == null) {
            emptyStats = new LinkedHashMap<>();
            emptyStats.put(StatusFields.name, "");
            emptyStats.put(StatusFields.remainedTime, 0);
            emptyStats.put(StatusFields.repeat, RepeatType.noRepeat.getValue());
            emptyStats.put(StatusFields.shuffle, false);
            emptyStats.put(StatusFields.paused, true);
        }
        return emptyStats;
    }

    /**
     * Perform the addRemoveInPlaylist command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see AddRemoveInPlaylist
     */
    public Output performAddRemoveInPlaylist(final CommandInput command) {
        AddRemoveInPlaylist addRemoveInPlaylist = commandManager.getAddRemoveInPlaylist();
        Playlist playlist = null;
        int playlistId = command.getPlaylistId();
        ArrayList<Playlist> playlists = NormalUsersLibrary.getInstance()
                .getUserByName(command.getUsername()).getPlaylists();
        if (playlists != null && playlistId <= playlists.size()) {
            playlist = playlists.get(playlistId - 1);
        }
        addRemoveInPlaylist
                .setPlaylist(playlist)
                .execute();
        String message = addRemoveInPlaylist.getMessage();
        addRemoveInPlaylist
                .setPlaylist(null);
        return new Output(command, message);
    }

    /**
     * Perform the like command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Like
     */
    public Output performLike(final CommandInput command) {
        if (!app.isOnline()) {
            return new Output(command, app.getUserOfflineMessage());
        }
        Like like = commandManager.getLike();
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(command.getUsername());
        like.setUser(user).execute();
        String message = like.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the repeat command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Repeat
     */
    public Output performRepeat(final CommandInput command) {
        Repeat repeat = commandManager.getRepeat();
        repeat.execute();
        String message = repeat.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the shuffle command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Shuffle
     */
    public Output performShuffle(final CommandInput command) {
        Shuffle shuffle = commandManager.getShuffle();
        int seed = command.getSeed();
        shuffle.setSeed(seed).execute();
        String message = shuffle.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the next command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Next
     */
    public Output performNext(final CommandInput command) {
        Next next = commandManager.getNext();
        decrementLoadedCountForAudio();
        next.execute();
        incrementLoadedCountForAudio();
        String message = next.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the prev command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Prev
     */
    public Output performPrev(final CommandInput command) {
        Prev prev = commandManager.getPrev();
        decrementLoadedCountForAudio();
        prev.execute();
        incrementLoadedCountForAudio();
        String message = prev.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the forward command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Forward
     */
    public Output performForward(final CommandInput command) {
        Forward forward = commandManager.getForward();
        forward.execute();
        String message = forward.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the backward command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see Backward
     */
    public Output performBackward(final CommandInput command) {
        Backward backward = commandManager.getBackward();
        backward.execute();
        String message = backward.getMessage();
        return new Output(command, message);
    }

    /**
     * Perform the switchVisibility command
     *
     * @param command the command input that specifies the parameters for the command
     * @return an {@code Output} object with the command and the message info
     * @see SwitchVisibility
     */
    public Output performSwitchVisibility(final CommandInput command) {
        SwitchVisibility switchVisibility = commandManager.getSwitchVisibility();
        Playlist playlist = null;
        int playlistId = command.getPlaylistId();
        ArrayList<Playlist> playlists = NormalUsersLibrary.getInstance()
                .getUserByName(command.getUsername()).getPlaylists();
        if (playlists != null && playlistId <= playlists.size()) {
            playlist = playlists.get(playlistId - 1);
        }
        switchVisibility.setPlaylist(playlist).execute();
        String message = switchVisibility.getMessage();
        switchVisibility.setPlaylist(null);
        return new Output(command, message);
    }

    /**
     * For pausing the music player
     */
    public void pausePlayer() {
        status = PlayerStatus.paused;
    }

    /**
     * For resuming the music player
     */
    public void resumePlayer() {
        status = PlayerStatus.playing;
    }

    /**
     * Resets the player to default
     * Playing/loaded entities.audio is set to null
     * Playing/loaded collection is set to null
     * If the playing collection was a podcast, pause it
     */
    public void resetPlayer() {
        if (playingCollection != null) {
            if (CheckClass.isPodcast(playingCollection.getPlayingCollection().getClass())) {
                playingCollection.pause();
            }
            status = PlayerStatus.idle;
        }
        decrementLoadedCountForCollection();
        decrementLoadedCountForAudio();
        playingAudio = null;
        playingCollection = null;
    }

    public void setPlayingCollection(final PlayingAudioCollection<? extends
            Collection<? extends Audio>> playingCollection) {
        this.playingCollection = playingCollection;
    }

    public void setLoadedObject(final Audio loadedObject) {
        this.loadedObject = loadedObject;
    }

    private void decrementLoadedCountForCollection() {
        if (playingCollection == null) {
            return;
        }
        Collection<?> collection = playingCollection.getPlayingCollection();
        collection.decrementLoadedCount();
    }

    private void incrementLoadedCountForCollection() {
        if (playingCollection == null) {
            return;
        }
        Collection<?> collection = playingCollection.getPlayingCollection();
        collection.incrementLoadedCount();
    }

    private void incrementLoadedCountForAudio() {
        if (playingAudio != null && CheckClass.isSong(playingAudio.getPlayingObject().getClass())) {
            Song song = (Song) playingAudio.getPlayingObject();
            song.incrementLoadedCount();
        }
    }

    private void decrementLoadedCountForAudio() {
        if (playingAudio != null && CheckClass.isSong(playingAudio.getPlayingObject().getClass())) {
            Song song = (Song) playingAudio.getPlayingObject();
            song.decrementLoadedCount();
        }
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case load -> performLoad(command);
            case playPause -> performPlayPause(command);
            case status -> performStatus(command);
            case addRemoveInPlaylist -> performAddRemoveInPlaylist(command);
            case like -> performLike(command);
            case repeat -> performRepeat(command);
            case shuffle -> performShuffle(command);
            case next -> performNext(command);
            case prev -> performPrev(command);
            case forward -> performForward(command);
            case backward -> performBackward(command);
            case switchVisibility -> performSwitchVisibility(command);
            case loadRecommendations -> performLoadRecommendation(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }

    /**
     * Inserts an ad
     */
    public void insertAd(final int price) {
        if (!app.isPremium()) {
            adBreak = true;
            this.adPrice = price;
            adRemainedTime = adDuration;
        }
    }

    /**
     * Removes an ad
     */
    public void removeAd() {
        adBreak = false;
        adPrice = 0;
        adRemainedTime = 0;
    }

    /**
     * For the Player status
     */
    public enum PlayerStatus {
        idle, playing, paused
    }
}
