package managers.commands;

import commands.CommandType;
import libraries.users.NormalUsersLibrary;
import managers.GeneralStatisticsManager;
import managers.UserCommandManager;
import managers.admin.AdminCommandManager;
import managers.artist.ArtistCommandManager;
import managers.host.HostCommandManager;
import managers.normalUser.AppManager;
import managers.normalUser.ConnectionStatusManager;
import managers.normalUser.NotificationsManager;
import managers.normalUser.PageSystemManager;
import entities.user.NormalUser;
import managers.normalUser.PurchaseManager;

public final class CommandManagerFactory {
    private CommandManagerFactory() {
    }

    /**
     * Creates a CommandHandler based on the specified CommandType and username.
     *
     * @param commandType The type of command to be handled.
     * @param username    The username associated with the command.
     * @return A CommandHandler instance based on the provided commandType.
     * @throws IllegalArgumentException If the commandType is not supported.
     * @see CommandType
     */
    public static CommandHandler createManager(final CommandType commandType,
                                               final String username) {
        return switch (commandType) {
            case getTop5Playlists, getTop5Songs, getOnlineUsers, getAllUsers, getTop5Albums,
                    getTop5Artists
                    -> GeneralStatisticsManager.getInstance();
            case addUser, deleteUser, showPodcasts, showAlbums, buyPremium, cancelPremium,
                    adBreak
                    -> AdminCommandManager.getInstance();
            case addAlbum, addEvent, addMerch, removeAlbum, removeEvent
                    -> ArtistCommandManager.getInstance();
            case switchConnectionStatus
                    -> ConnectionStatusManager.getInstance();
            case changePage, subscribe
                    -> PageSystemManager.getInstance();
            case addAnnouncement, addPodcast, removeAnnouncement,
                    removePodcast
                    -> HostCommandManager.getInstance();
            case wrapped -> UserCommandManager.getInstance();
            case getNotifications -> NotificationsManager.getInstance();
            case buyMerch, seeMerch -> PurchaseManager.getInstance();
            case updateRecommendations, previousPage, topAlbums, listeners,
                    topSongs, topFans, topEpisodes, songRevenue, merchRevenue, ranking,
                    mostProfitableSong, loadRecommendations, nextPage -> null;
            default -> {
                NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
                assert user != null;
                AppManager app = user.getApp();
                yield switch (commandType) {
                    case search, select
                            -> app.getSearchBarManager();
                    case addRemoveInPlaylist, backward, forward, like, load, next, prev,
                            repeat, status, shuffle, playPause, switchVisibility
                            -> app.getPlayerManager();
                    case createPlaylist, showPlaylists, follow, showPreferredSongs
                            -> app.getPlaylistManager();
                    default -> throw new IllegalArgumentException("Unknown command type: "
                            + commandType);
                };
            }
        };
    }
}
