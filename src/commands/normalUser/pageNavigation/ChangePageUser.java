package commands.normalUser.pageNavigation;

import commands.ActionCommand;
import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import managers.normalUser.AppManager;

public final class ChangePageUser extends ActionCommand {
    private static final String HOME = "Home";
    private static final String LIKED = "LikedContent";
    private static final String ARTIST = "Artist";
    private static final String HOST = "Host";
    private final NormalUser user;
    private final String pageName;
    private Page previousPage;
    private Page page;

    public ChangePageUser(final NormalUser user, final String pageName) {
        this.user = user;
        this.pageName = pageName;
    }

    /**
     * Executes the change page operation for a user
     */
    public void execute() {
        AppManager app = user.getApp();
        String owner = app.getPageOwner();
        previousPage = app.getPage();
        if (previousPage.pageType().equals(PageType.artistPage)) {
            Artist artist = ArtistsLibrary.getInstance().getArtistByName(owner);
            artist.decrementPageViewersCount();
        } else if (previousPage.pageType().equals(PageType.hostPage)) {
            Host host = HostsLibrary.getInstance().getHostByName(owner);
            assert host != null;
            host.decrementPageViewersCount();
        }
        if (pageName.equals(HOME)) {
            page = new Page(PageType.homePage, null);
        } else if (pageName.equals(LIKED)) {
            page = new Page(PageType.likedContentPage, null);
        } else if (pageName.equals(ARTIST)) {
            String artistName = user.getApp().getPlayerManager()
                    .getPlayingAudio()
                    .getPlayingObject()
                    .getOwner();
            Artist artist = ArtistsLibrary.getInstance().getArtistByName(artistName);
            artist.incrementPageViewersCount();
            page = new Page(PageType.artistPage, artistName);
        } else if (pageName.equals(HOST)) {
            String hostName = user.getApp().getPlayerManager()
                    .getPlayingCollection()
                    .getPlayingCollection()
                    .getOwner();
            Host host = HostsLibrary.getInstance().getHostByName(hostName);
            if (host == null) {
                setMessage("ce drq");
                page = new Page(PageType.homePage, null);
                app.setPage(page);
                return;
            }
            assert host != null;
            host.incrementPageViewersCount();
            page = new Page(PageType.hostPage, hostName);
        }
        app.setPage(page);
        setMessage(user.getName() + " accessed " + pageName + " successfully.");
    }

    @Override
    public void redo() {
        user.getApp().setPage(page);
        setMessage("The user "
                + user.getName()
                + " has navigated successfully to the next page.");
    }

    @Override
    public void undo() {
        user.getApp().setPage(previousPage);
        setMessage("The user "
                + user.getName()
                + " has navigated successfully to the previous page.");
    }
}
