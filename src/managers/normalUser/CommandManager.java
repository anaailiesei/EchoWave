package managers.normalUser;

import commands.normalUser.player.*;
import commands.normalUser.playlist.CreatePlaylist;
import commands.normalUser.playlist.FollowPlaylist;
import commands.normalUser.playlist.SwitchVisibility;
import commands.normalUser.searchBar.audio.SearchAudio;
import commands.normalUser.searchBar.audio.SelectAudio;
import commands.normalUser.searchBar.user.SearchUser;
import commands.normalUser.searchBar.user.SelectUser;

public final class CommandManager {
    private final AppManager app;
    private AddRemoveInPlaylist addRemoveInPlaylist = null;
    private Backward backward = null;
    private Forward forward = null;
    private Like like = null;
    private LoadAudio load = null;
    private Next next = null;
    private PlayPause playPause = null;
    private Prev prev = null;
    private Repeat repeat = null;
    private Shuffle shuffle = null;
    private CreatePlaylist createPlaylist = null;
    private FollowPlaylist followPlaylist = null;
    private SwitchVisibility switchVisibility = null;
    private SearchAudio searchAudio = null;
    private SelectAudio selectAudio = null;
    private SearchUser searchUser = null;
    private SelectUser selectUser = null;

    public CommandManager(final AppManager parentApp) {
        this.app = parentApp;
    }

    /**
     * Get an instance for {@code AddRemoveInPlaylist} for the current {@code CommandManager}
     *
     * @return the {@code AddRemoveInPlaylist} instance
     * @see AddRemoveInPlaylist
     */
    public synchronized AddRemoveInPlaylist getAddRemoveInPlaylist() {
        if (addRemoveInPlaylist == null) {
            addRemoveInPlaylist = new AddRemoveInPlaylist(app.getPlayerManager());
        }
        return addRemoveInPlaylist;
    }

    /**
     * Get an instance for {@code Backward} for the current {@code CommandManager}
     *
     * @return the {@code Backward} instance
     * @see Backward
     */
    public synchronized Backward getBackward() {
        if (backward == null) {
            backward = new Backward(app.getPlayerManager());
        }
        return backward;
    }

    /**
     * Get an instance for {@code Forward} for the current {@code CommandManager}
     *
     * @return the {@code Forward} instance
     * @see Forward
     */
    public synchronized Forward getForward() {
        if (forward == null) {
            forward = new Forward(app.getPlayerManager());
        }
        return forward;
    }

    /**
     * Get an instance for {@code Like} for the current {@code CommandManager}
     *
     * @return the {@code Like} instance
     * @see Like
     */
    public synchronized Like getLike() {
        if (like == null) {
            like = new Like(app.getPlayerManager());
        }
        return like;
    }

    /**
     * Get an instance for {@code Load} for the current {@code CommandManager}
     *
     * @return the {@code Load} instance
     * @see LoadAudio
     */
    public synchronized LoadAudio getLoad() {
        if (load == null) {
            load = new LoadAudio(app.getPlayerManager(),
                    app.getSearchBarManager(),
                    getShuffle(),
                    getSelectAudio(),
                    app.getListenTracker());
        }
        return load;
    }

    /**
     * Get an instance for {@code Next} for the current {@code CommandManager}
     *
     * @return the {@code Next} instance
     * @see Next
     */
    public synchronized Next getNext() {
        if (next == null) {
            next = new Next(app.getPlayerManager());
        }
        return next;
    }

    /**
     * Get an instance for {@code PlayPause} for the current {@code CommandManager}
     *
     * @return the {@code PlayPause} instance
     * @see PlayPause
     */
    public synchronized PlayPause getPlayPause() {
        if (playPause == null) {
            playPause = new PlayPause(app.getPlayerManager());
        }
        return playPause;
    }

    /**
     * Get an instance for {@code Prev} for the current {@code CommandManager}
     *
     * @return the {@code Prev} instance
     * @see Prev
     */
    public synchronized Prev getPrev() {
        if (prev == null) {
            prev = new Prev(app.getPlayerManager());
        }
        return prev;
    }

    /**
     * Get an instance for {@code Repeat} for the current {@code CommandManager}
     *
     * @return the {@code Repeat} instance
     * @see Repeat
     */
    public synchronized Repeat getRepeat() {
        if (repeat == null) {
            repeat = new Repeat(app.getPlayerManager());
        }
        return repeat;
    }

    /**
     * Get an instance for {@code Shuffle} for the current {@code CommandManager}
     *
     * @return the {@code Shuffle} instance
     * @see Shuffle
     */
    public synchronized Shuffle getShuffle() {
        if (shuffle == null) {
            shuffle = new Shuffle(app.getPlayerManager());
        }
        return shuffle;
    }

    /**
     * Get an instance for {@code CreatePlaylist} for the current {@code CommandManager}
     *
     * @return the {@code CreatePlaylist} instance
     * @see CreatePlaylist
     */
    public synchronized CreatePlaylist getCreatePlaylist() {
        if (createPlaylist == null) {
            createPlaylist = new CreatePlaylist();
        }
        return createPlaylist;
    }

    /**
     * Get an instance for {@code FollowPlaylist} for the current {@code CommandManager}
     *
     * @return the {@code FollowPlaylist} instance
     * @see FollowPlaylist
     */
    public FollowPlaylist getFollowPlaylist() {
        if (followPlaylist == null) {
            followPlaylist = new FollowPlaylist(app.getPlayerManager(), getSelectAudio());
        }
        return followPlaylist;
    }

    /**
     * Get an instance for {@code SwitchVisibility} for the current {@code CommandManager}
     *
     * @return the {@code SwitchVisibility} instance
     * @see SwitchVisibility
     */
    public SwitchVisibility getSwitchVisibility() {
        if (switchVisibility == null) {
            switchVisibility = new SwitchVisibility();
        }
        return switchVisibility;
    }

    /**
     * Get an instance for {@code SearchAudio} for the current {@code CommandManager}
     *
     * @return the {@code SearchAudio} instance
     * @see SearchAudio
     */
    public SearchAudio getSearchAudio() {
        if (searchAudio == null) {
            searchAudio = new SearchAudio();
        }
        return searchAudio;
    }

    /**
     * Get an instance for {@code SelectAudio} for the current {@code CommandManager}
     *
     * @return the {@code SelectAudio} instance
     * @see SelectAudio
     */
    public SelectAudio getSelectAudio() {
        if (selectAudio == null) {
            selectAudio = new SelectAudio(app.getSearchBarManager());
        }
        return selectAudio;
    }

    /**
     * Get an instance for {@code SearchUser} for the current {@code CommandManager}
     *
     * @return the {@code SearchUser} instance
     * @see SearchUser
     */
    public SearchUser getSearchUser() {
        if (searchUser == null) {
            searchUser = new SearchUser();
        }
        return searchUser;
    }

    /**
     * Get an instance for {@code SelectUser} for the current {@code CommandManager}
     *
     * @return the {@code SelectUser} instance
     * @see SelectUser
     */
    public SelectUser getSelectUser() {
        if (selectUser == null) {
            selectUser = new SelectUser(app.getSearchBarManager());
        }
        return selectUser;
    }
}
