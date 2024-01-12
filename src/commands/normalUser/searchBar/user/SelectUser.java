package commands.normalUser.searchBar.user;

import commands.normalUser.pageNavigation.Page;
import commands.normalUser.pageNavigation.PageType;
import commands.normalUser.searchBar.Select;
import libraries.users.NormalUsersLibrary;
import managers.CheckClass;
import managers.normalUser.SearchBarManager;
import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import entities.user.User;

import java.util.ArrayList;

public final class SelectUser extends Select<User> {
    public SelectUser(final SearchBarManager searchBarManager) {
        super.searchBarManager = searchBarManager;
    }

    @Override
    public String toString(final int itemNumber, final ArrayList<? extends User> selectFrom) {
        if (!(searchBarManager.getStatus().equals(SearchBarManager.SearchBarStatus.searching))) {
            return "Please conduct a search before making a selection.";
        } else if (!isValid(itemNumber, selectFrom)) {
            return "The selected ID is too high.";
        } else {
            return "Successfully selected " + selectedObject.getName() + "'s page.";
        }
    }

    @Override
    public void updateUser(final String username) {
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        if (selectedObject != null && CheckClass.isArtist(selectedObject.getClass())) {
            Artist artist = (Artist) selectedObject;
            artist.incrementPageViewersCount();
            assert user != null;
            Page page = new Page(PageType.artistPage, artist.getName());
            user.getApp().setPage(page);
        } else if (selectedObject != null && CheckClass.isHost(selectedObject.getClass())) {
            Host host = (Host) selectedObject;
            host.incrementPageViewersCount();
            assert user != null;
            Page page = new Page(PageType.hostPage, host.getName());
            user.getApp().setPage(page);
        }
    }
}
