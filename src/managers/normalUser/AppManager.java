package managers.normalUser;

import commands.normalUser.pageNavigation.Page;
import commands.normalUser.pageNavigation.PageType;
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
    private Page page = new Page(PageType.homePage, null);
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

    public void setPremium(final boolean premium) {
        this.premium = premium;
    }

    /**
     * Gets the page owner of the page the user is at
     * (returns null if the user is on the home page or liked content page)
     *
     * @return The name
     */
    public String getPageOwner() {
        return page.ownerName();
    }
    public enum AppStatus {
        online, offline;

        private AppStatus toggle() {
            return this == online ? offline : online;
        }
    }

}
