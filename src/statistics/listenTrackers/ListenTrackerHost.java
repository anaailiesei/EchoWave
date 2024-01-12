package statistics.listenTrackers;

import entities.audio.Episode;
import entities.user.NormalUser;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ListenTrackerHost {
    private final ListenTracker<Episode> episodesListenTracker = new ListenTracker<>();
    private final ListenTracker<NormalUser> fansListenTracker = new ListenTracker<>();

    /**
     * Adds a listen for the specified episode
     *
     * @param episode The episode for which we add a listen
     * @see Episode
     */
    public void addListen(final Episode episode) {
        episodesListenTracker.addListen(episode);
    }

    /**
     * Adds a listen for the specified user
     *
     * @param user The user for which we add a listen
     * @see NormalUser
     */
    public void addListen(final NormalUser user) {
        fansListenTracker.addListen(user);
    }

    /**
     * Adds the specified number of listens for the specified song
     *
     * @param episode The song for which we add a listen
     * @param count The number of listens that should be added
     * @see Episode
     */
    public void addListen(final Episode episode, final int count) {
        episodesListenTracker.addListen(episode, count);
    }

    /**
     * Adds the specified number of listens for the specified user
     *
     * @param user  The user for which we add a listen
     * @param count The number of listens that should be added
     * @see NormalUser
     */
    public void addListen(final NormalUser user, final int count) {
        fansListenTracker.addListen(user, count);
    }

    /**
     * Get the top listens for each category (albums, songs, fans, listeners)
     * This is used with the wrapped command
     *
     * @return A map with the top results for each category
     */
    public HashMap<String, Object> topListensForEach() {
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put("topEpisodes", episodesListenTracker.getTopFiveListensNames());
        result.put("listeners", fansListenTracker.getSize());

        return result;
    }

    /**
     * Adds a listen for the following categories: Episode and User
     *
     * @param episode  The episode for which we add a listen
     * @param user  The user for which we add a listen
     * @see Episode
     * @see NormalUser
     */
    public void addListenAll(final Episode episode, final NormalUser user) {
        addListen(episode);
        addListen(user);
    }


    /**
     * Adds the specified number of listens for the following categories: Episode and User
     *
     * @param episode  The episode for which we add a listen
     * @param user  The user for which we add a listen
     * @param count The number of listens we add
     * @see Episode
     * @see NormalUser
     */
    public void addListenAll(final Episode episode, final NormalUser user, final int count) {
        addListen(episode, count);
        addListen(user, count);
    }
}
