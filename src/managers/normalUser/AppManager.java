package managers.normalUser;

import audio.Audio;
import audio.Song;
import audio.collections.Album;
import libraries.audio.AlbumsLibrary;
import lombok.Getter;
import managers.CheckClass;
import statistics.ListenTracker;
import statistics.ListenTrackerNormalUser;

import java.util.HashMap;
import java.util.Map;
@Getter
public final class AppManager {
    private String pageOwner;
    private final String userOfflineMessage;
    private Page page = Page.homePage;
    private final PlaylistManager playlistManager = new PlaylistManager(this);
    private final CommandManager commandManager = new CommandManager(this);
    private final PlayerManager playerManager = new PlayerManager(this);
    private final SearchBarManager searchBarManager = new SearchBarManager(this);
    private AppStatus status = AppStatus.online;
    private final ListenTrackerNormalUser listenTracker = new ListenTrackerNormalUser();
    public AppManager(final String username) {
        this.userOfflineMessage = username + " is offline.";
    }

    public void setStatus(final AppStatus status) {
        this.status = status;
    }

    /**
     * Toggle the status of the app (user) between online and offline
     */
    public void toggleStatus() {
        this.status = status.toggle();
    }

    public boolean isOnline() {
        return status.equals(AppStatus.online);
    }

    public void setPage(final Page page) {
        this.page = page;
    }

    public void setPageOwner(final String pageOwner) {
        this.pageOwner = pageOwner;
    }

    public enum Page {
        homePage, likedContentPage, artistPage, hostPage
    }

    public enum AppStatus {
        online, offline;

        private AppStatus toggle() {
            return this == online ? offline : online;
        }
    }
}
