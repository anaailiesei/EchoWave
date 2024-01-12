package playables;

import entities.audio.Audio;
import entities.audio.Episode;
import entities.audio.Song;
import commands.normalUser.player.RepeatType;
import commands.normalUser.player.StatusFields;
import entities.audio.collections.Album;
import entities.user.Host;
import libraries.audio.AlbumsLibrary;
import libraries.users.ArtistsLibrary;
import entities.user.Artist;
import entities.user.NormalUser;
import libraries.users.HostsLibrary;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PlayingAudio<T extends Audio> implements Playing {
    private T playingObject;
    /**
     * -- GETTER --
     *  Gets the stats for the playing object
     *
     * @return the stats
     */
    @Getter
    private Map<StatusFields, Object> stats;
    private final Artist artist;
    private final Host host;
    private NormalUser user;
    private final Album album;
    public PlayingAudio(final T playingObject, final NormalUser user) {
        setPlayingObject(playingObject);
        initStatsDefault();
        this.user = user;
        this.artist = ArtistsLibrary.getInstance().getArtistByName(playingObject.getOwner());
        if (artist != null) {
            Song song = (Song) playingObject;
            String albumName = song.getAlbum();
            // TODO: problem with method get album by name, as there can be duplicate albums!
            // TODO: better use a method like get album by name adn username!
            album = AlbumsLibrary.getInstance().getAlbumByName(albumName);
        } else {
            album = null;
        }
        this.host = HostsLibrary.getInstance().getHostByName(playingObject.getOwner());
    }

    /**
     * Gets the current playing entities.audio
     *
     * @return The playing entities.audio
     */
    public Audio getPlayingObject() {
        return playingObject;
    }

    /**
     * Sets the current playing entities.audio
     *
     * @param playingObject The entities.audio object to be set
     */
    public void setPlayingObject(final T playingObject) {
        this.playingObject = playingObject;
    }

    /**
     * Sets the stats for the playing object
     *
     * @param stats the stats to be set
     */
    public void setStats(final Map<StatusFields, Object> stats) {
        this.stats = stats;
    }

    /**
     * Initialize the stats for a playing entities.audio to the default values
     */
    public void initStatsDefault() {
        stats = new LinkedHashMap<>();
        stats.put(StatusFields.name, playingObject.getName());
        stats.put(StatusFields.remainedTime, playingObject.getDuration());
        stats.put(StatusFields.repeat, RepeatType.noRepeat.getValue());
        stats.put(StatusFields.shuffle, false);
        stats.put(StatusFields.paused, false);
    }

    /**
     * Adds the time passed for the current track depending on the repeat value
     * If no repeat is set, finish the track
     * If repeat once is set, play the track again and set the repeat to no repeat
     * If repeat infinite is set, just replay the track
     *
     * @param timePassed the time that has passed
     */
    @Override
    public void addTimePassed(final int timePassed) {
        if ((boolean) stats.get(StatusFields.paused)) {
            return;
        }
        int remainedTime = (int) stats.get(StatusFields.remainedTime);
        int newRemainedTime = remainedTime - timePassed;
        int duration = playingObject.getDuration();
        if (getRepeatValue().equals(RepeatType.repeatOnce.getValue())) {
            if (newRemainedTime < 0) {
                newRemainedTime = Math.max(newRemainedTime + duration, 0);
                setRepeatValue(RepeatType.noRepeat);

                // TODO: this doesnt add the album
                playingObject.addListen(user.getApp().getListenTracker());
                if (artist != null) {
                    Song song = (Song) playingObject;
                    String albumName = song.getAlbum();
                    Album album = AlbumsLibrary.getInstance().getAlbumByName(albumName);
                    artist.getListenTracker().addListenAll(album, song, user);
                } else if (host != null) {
                    host.getListenTracker().addListenAll((Episode) playingObject, user);
                }
            }
        } else if (getRepeatValue().equals(RepeatType.repeatInfinite.getValue())
                || getRepeatValue().equals(RepeatType.repeatCurrent.getValue())) {
            if (newRemainedTime < 0) {
                // TODO: this doesnt add the album
                playingObject.addListen(user.getApp().getListenTracker(),
                        (newRemainedTime / duration));
                if (artist != null) {
                    artist.getListenTracker()
                            .addListenAll(album, (Song) playingObject, user, newRemainedTime / duration);
                } if (host != null) {
                    host.getListenTracker()
                            .addListenAll((Episode) playingObject, user);
                }
                newRemainedTime = (newRemainedTime % duration + duration) % duration;

            }
        } else {
            newRemainedTime = Math.max(newRemainedTime, 0);
        }
        stats.put(StatusFields.remainedTime, newRemainedTime);
        checkIfTrackFinished();
    }

    /**
     * Checks if the track finished playing
     * If the track finished playing, pause it
     * Must be used after the remaining time is updated
     */
    private void checkIfTrackFinished() {
        if ((int) stats.get(StatusFields.remainedTime) == 0) {
            stats.put(StatusFields.paused, true);
        }
    }

    /**
     * Skips a track to the end (remained time is equal to 0,
     * no repeat for the repeat value)
     */
    public void skipToEnd() {
        stats.put(StatusFields.repeat, RepeatType.noRepeat.getValue());
        stats.put(StatusFields.remainedTime, 0);
        checkIfTrackFinished();
    }

    /**
     * Pauses the track
     */
    public void pause() {
        stats.put(StatusFields.paused, true);
    }

    /**
     * Resumes the track
     */
    public void resume() {
        stats.put(StatusFields.paused, false);
    }

    /**
     * Gets the repeat value for the current track
     *
     * @return the repeat value
     */
    public String getRepeatValue() {
        return (String) stats.get(StatusFields.repeat);
    }

    /**
     * Sets the repeat value for the current track
     *
     * @param repeatType The repeat type to be set (that's converted to String)
     * @see RepeatType
     */
    public void setRepeatValue(final RepeatType repeatType) {
        stats.put(StatusFields.repeat, repeatType.getValue());
    }

    /**
     * Gets the remained time for the current track
     *
     * @return the remained time
     */
    public int getRemainedTime() {
        return (int) stats.get(StatusFields.remainedTime);
    }

    /**
     * Gets the duration of the track
     *
     * @return the duration (seconds)
     */
    public int getDuration() {
        return playingObject.getDuration();
    }

    /**
     * Reset the remained time for the current playing entities.audio
     * (sets it to the value of the total duration of the track)
     */
    public void resetRemainedTime() {
        int duration = getDuration();
        stats.put(StatusFields.remainedTime, duration);
    }

    /**
     * Sets the shuffle parameter (that specifies if the playing entities.audio
     * is part of a collection that's shuffled)
     *
     * @param shuffle the shuffle value to be set (true or false)
     */
    public void setShuffle(final boolean shuffle) {
        stats.put(StatusFields.shuffle, shuffle);
    }

    /**
     * Gets the name for the current playing entities.audio
     *
     * @return the name
     */
    public String getName() {
        return playingObject.getName();
    }

    /**
     * Gets the time elapsed for the current playing entities.audio
     *
     * @return the elapsed time
     */
    public int getElapsedTime() {
        return getDuration() - getRemainedTime();
    }

    /**
     * Must only be used if we are sure that the track wouldn't finish after the specified time
     * Plays the track forward by the specified time
     *
     * @param forwardTime
     */
    public void addForwardTime(final int forwardTime) {
        int remainedTime = (int) stats.get(StatusFields.remainedTime);
        stats.put(StatusFields.remainedTime, remainedTime - forwardTime);
    }

    /**
     * Must only be used if we are sure that at least the specified time has elapsed
     * Plays the track backward by the specified time
     *
     * @param backwardTime
     */
    public void addBackwardTime(final int backwardTime) {
        int remainedTime = (int) stats.get(StatusFields.remainedTime);
        stats.put(StatusFields.remainedTime, remainedTime + backwardTime);
    }

    /**
     * Checks if the track is paused or not
     *
     * @return {@code true} if the track is paused, {@code false} otherwise
     */
    public boolean isPaused() {
        return (boolean) stats.get(StatusFields.paused);
    }

    public void setUser(final NormalUser user) {
        this.user = user;
    }
}
