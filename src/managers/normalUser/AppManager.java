package managers.normalUser;

import lombok.Getter;
import statistics.listenTrackers.ListenTrackerNormalUser;

@Getter
public final class AppManager {
    private final String userOfflineMessage;
    private final PlaylistManager playlistManager = new PlaylistManager(this);
    private final CommandManager commandManager = new CommandManager(this);
    private final PlayerManager playerManager = new PlayerManager(this);
    private final SearchBarManager searchBarManager = new SearchBarManager(this);
    private final ListenTrackerNormalUser listenTracker = new ListenTrackerNormalUser();
    private String pageOwner;
    private Page page = Page.homePage;
    private AppStatus status = AppStatus.online;
    private boolean premium;

    public AppManager(final String username) {
        this.userOfflineMessage = username + " is offline.";
    }

    public void setStatus(final AppStatus status) {
        this.status = status;
    }

    /**
     * Toggle the status of the app (entities.user) between online and offline
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

    public void setPremium(final boolean premium) {
        this.premium = premium;
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
