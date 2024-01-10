package managers.normalUser;

import commands.CommandType;
import commands.normalUser.searchBar.Search;
import commands.normalUser.searchBar.SearchType;
import commands.normalUser.searchBar.Select;
import commands.normalUser.searchBar.filter.Filters;
import fileio.input.CommandInput;
import fileio.output.Output;
import lombok.Getter;
import managers.commands.CommandHandler;

import java.util.ArrayList;
import java.util.Map;

public final class SearchBarManager implements CommandHandler {
    private final AppManager app;
    private final PlayerManager playerManager;
    private final CommandManager commandManager;
    /**
     * -- GETTER --
     * gets the current status of the search bar
     */
    @Getter
    private SearchBarStatus status = SearchBarStatus.idle;
    private SearchType searchType = SearchType.nothing;

    public SearchBarManager(final AppManager parentApp) {
        this.app = parentApp;
        this.commandManager = app.getCommandManager();
        this.playerManager = app.getPlayerManager();
    }

    /**
     * Sets the current state of the search bar
     *
     * @param status the status to be set
     */
    public void setStatus(final SearchBarStatus status) {
        this.status = status;
    }

    /**
     * Searches in the library, as indicated by the command
     *
     * @param command The command for the search
     * @return {@code Output} object
     */
    public Output performSearch(final CommandInput command) {
        if (!app.isOnline()) {
            return new Output(command, new ArrayList<>(), app.getUserOfflineMessage());
        }
        searchType = SearchType.fromString(command.getType());
        commandManager.getSelectAudio().resetSelect();
        commandManager.getSelectUser().resetSelect();
        playerManager.resetPlayer();
        if (searchType.equals(SearchType.playlist)
                || searchType.equals(SearchType.song)
                || searchType.equals(SearchType.podcast)
                || searchType.equals(SearchType.album)) {
            return performSearchGeneral(command, commandManager.getSearchAudio());
        }
        return performSearchGeneral(command, commandManager.getSearchUser());
    }

    /**
     * Performs a general search using the provided search instance and command input.
     * This method executes a search operation using the given search instance and the provided
     * command input, applying specified filters.
     *
     * @param command The input command containing search parameters and filters.
     * @param search  The search instance responsible for executing the search operation.
     * @return An Output object with the search results, message, and updated search status.
     */
    private Output performSearchGeneral(final CommandInput command, final Search<?> search) {
        Map<Filters, Object> filters = command.getFilters();
        String type = command.getType();
        if (type.equals(SearchType.playlist.getType())) {
            filters.put(Filters.visibilityForUser, command.getUsername());
        }
        search.execute(type, filters);
        ArrayList<String> results = search.getSearchResults();
        String message = search.getMessage();
        setStatus(SearchBarStatus.searching);
        return new Output(command, results, message);
    }

    /**
     * Selects the item from the search results, as indicated by the command
     *
     * @param command The command given
     * @return {@code Output} object
     */
    public Output performSelect(final CommandInput command) {
        if (searchType.equals(SearchType.playlist)
                || searchType.equals(SearchType.song)
                || searchType.equals(SearchType.podcast)
                || searchType.equals(SearchType.album)) {
            searchType = SearchType.nothing;
            return performSelectGeneral(command,
                    commandManager.getSelectAudio(),
                    commandManager.getSearchAudio());
        }
        searchType = SearchType.nothing;
        return performSelectGeneral(command,
                commandManager.getSelectUser(),
                commandManager.getSearchUser());
    }

    private <E> Output performSelectGeneral(final CommandInput command,
                                            final Select<E> select,
                                            final Search<E> search) {
        int itemNumber = command.getItemNumber();
        ArrayList<? extends E> selectFrom = search.getFilteredObjects();
        select.execute(itemNumber, selectFrom, command.getUsername());
        String message = select.getMessage();
        search.resetSearch();
        setStatus(SearchBarStatus.selecting);
        return new Output(command, message);
    }

    /**
     * Sets the search bar to idle and clears any selection or search made
     */
    public void setSearchBarToIdle() {
        status = SearchBarStatus.idle;
        commandManager.getSearchAudio().resetSearch();
        commandManager.getSearchUser().resetSearch();
        commandManager.getSelectAudio().resetSelect();
        commandManager.getSelectUser().resetSelect();
    }

    @Override
    public Output performCommand(final CommandInput command) {
        CommandType commandType = command.getCommand();

        return switch (commandType) {
            case search -> performSearch(command);
            case select -> performSelect(command);
            default -> throw new IllegalStateException("Unexpected command for "
                    + this.getClass().getSimpleName() + ": " + commandType);
        };
    }

    /**
     * Search bar status
     */
    public enum SearchBarStatus {
        searching, selecting, idle;
    }
}
