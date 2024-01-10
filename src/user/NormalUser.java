package user;

import audio.Song;
import audio.collections.Collection;
import audio.collections.Playlist;
import audio.collections.Podcast;
import fileio.input.CommandInput;
import fileio.input.UserInput;
import fileio.output.PageOutput;
import libraries.users.ArtistsLibrary;
import libraries.users.HostsLibrary;
import lombok.Getter;
import managers.normalUser.AppManager;
import managers.normalUser.ProgressManager;
import profile.artist.Event;
import profile.artist.Merch;
import profile.host.Announcement;

import java.util.*;

public final class NormalUser extends User {
    private static final long MAX_SIZE = 5;
    private ArrayList<Playlist> playlists;
    private Playlist liked;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    private ProgressManager progressManager;
    private AppManager app;

    public NormalUser(final UserInput userInput) {
        super(userInput);
    }

    public NormalUser(final String username, final int age, final String city) {
        UserInput userInput = new UserInput(username, age, city);
        setUserInput(userInput);
    }

    /**
     * Gets the progress manager for the current user that tracks podcasts
     * That were not finished adn can be continued (saves the progress)
     * If it's not initialized yet, it initializes it too
     *
     * @return
     */
    public synchronized ProgressManager getProgressManager() {
        if (progressManager == null) {
            progressManager = new ProgressManager(this);
        }
        return progressManager;
    }

    /**
     * Gets the app manager for the current user
     * If it's not initialized yet, it initializes it too
     *
     * @return
     */
    public synchronized AppManager getApp() {
        if (app == null) {
            app = new AppManager(getUsername());
        }
        return app;
    }

    /**
     * Initialize the playlists array for the current user
     */
    private synchronized void initPlaylists() {
        playlists = new ArrayList<>();
    }

    /**
     * Initialize the liked playlist for the current user
     */
    public synchronized void initLikedPlaylist() {
        liked = new Playlist("Liked", getUsername(), new ArrayList<>());
    }

    /**
     * Initialize the followed playlists list
     */
    public synchronized void initFollowedPlaylists() {
        followedPlaylists = new ArrayList<>();
    }

    /**
     * Gets the playlists for this user
     *
     * @return a list of playlists
     */
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Sets the playlists for this user
     *
     * @param playlists The list of playlists to be set
     */
    public void setPlaylists(final ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public Playlist getLiked() {
        return liked;
    }

    /**
     * Adds a new playlist into the user's list of playlists
     *
     * @param newPlaylist the playlist to be added
     */
    public synchronized void addPlaylist(final Playlist newPlaylist) {
        if (playlists == null) {
            initPlaylists();
        }
        playlists.add(newPlaylist);
    }

    /**
     * Likes a song
     * Adds a song to the liked songs list for this user
     *
     * @param song The song to be added to the list
     */
    public void addLikedSong(final Song song) {
        if (liked == null) {
            initLikedPlaylist();
        }
        if (!isSongLiked(song)) {
            liked.addItem(song);
        }
    }

    /**
     * Follows the playlist
     * Adds a playlist to the followed playlists list for this user
     *
     * @param playlist The playlist to be added
     */
    public void addFollowedPlaylist(final Playlist playlist) {
        if (followedPlaylists == null) {
            initFollowedPlaylists();
        }
        if (!isPlaylistFollowed(playlist)) {
            followedPlaylists.add(playlist);
        }
    }

    /**
     * Unfollows the playlist
     * Removes the specified playlist from the followed playlists
     *
     * @param followedPlaylist
     */
    public void removeFollowedPlaylist(final Playlist followedPlaylist) {
        if (followedPlaylists == null) {
            return;
        }
        if (isPlaylistFollowed(followedPlaylist)) {
            followedPlaylists.remove(followedPlaylist);
        }
    }

    /**
     * Checks if a playlist is followed (is part of the list with followed playlists)
     *
     * @param playlist The playlist we check for
     * @return {@code true} if the playlist is followed, {@code false} otherwise
     */
    public boolean isPlaylistFollowed(final Playlist playlist) {
        if (followedPlaylists == null || followedPlaylists.isEmpty()) {
            return false;
        }
        return followedPlaylists.contains(playlist);
    }

    /**
     * Checks if a song is liked (is part of the liked playlist)
     *
     * @param song The song we check for
     * @return {@code true} if the song is liked, {@code false} otherwise
     */
    public boolean isSongLiked(final Song song) {
        if (liked == null || liked.isEmpty()) {
            return false;
        }
        return liked.containsItem(song);
    }

    /**
     * Unlikes a song
     * Removes a song from the liked songs playlist
     *
     * @param song The song to be removed
     */
    public void removeLikedSong(final Song song) {
        if (liked == null || liked.isEmpty() || !liked.containsItem(song)) {
            return;
        }
        liked.removeItem(song);
    }

    public boolean isOnline() {
        return getApp().isOnline();
    }

    /**
     * Retrieves the names of playlists from the given list of followed playlists.
     *
     * @param playlistsToName The list of followed playlists.
     * @return A list containing the names of the playlists.
     */
    private List<String> getPlaylistNames(final ArrayList<Playlist> playlistsToName) {
        return Optional.ofNullable(playlistsToName)
                .map(playlist -> playlist.stream().map(Collection::getName).toList())
                .orElse(new ArrayList<>());
    }

    /**
     * Prints information about the home page, including liked songs and followed playlists.
     *
     * @return A formatted string containing information about liked songs and followed playlists.
     */
    private String printHomePage() {
        Optional<Playlist> likedSongs = Optional.ofNullable(getLiked());
        List<String> likedSongsNames = likedSongs.map(likedObj ->
                        likedObj.getCollection().stream()
                                .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                                .map(Song::getName)
                                .limit(MAX_SIZE)
                                .toList())
                .orElse(List.of());

        List<String> followedPlaylistsNames = getPlaylistNames(getFollowedPlaylists());
        return "Liked songs:\n\t" + likedSongsNames + "\n\nFollowed playlists:\n\t"
                + followedPlaylistsNames;
    }

    /**
     * Prints information about the liked content page,
     * including liked songs and followed playlists.
     *
     * @return A formatted string containing information about liked songs and followed playlists.
     */
    private String printLikedContentPage() {
        ArrayList<Song> likedSongs = getLiked() != null ? getLiked().getCollection()
                : new ArrayList<>();
        ArrayList<Playlist> followed
                = getFollowedPlaylists() != null ? getFollowedPlaylists() : new ArrayList<>();
        return "Liked songs:\n\t" + songListToString(likedSongs)
                + "\n\nFollowed playlists:\n\t" + playlistListToString(followed);
    }

    /**
     * Prints information about the artists page, including albums, merch, and events.
     *
     * @return A formatted string containing information about albums, merch, and events.
     */
    private String printArtistsPage() {
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(app.getPageOwner());
        List<String> albumNames = artist.getAlbums().stream().map(Collection::getName).toList();
        LinkedHashSet<Merch> merch = artist.getMerchandise();
        LinkedHashSet<Event> events = artist.getEvents();
        return "Albums:\n\t" + albumNames + "\n\nMerch:\n\t" + merch + "\n\nEvents:\n\t" + events;
    }

    /**
     * Converts a LinkedHashSet of Podcast objects to a formatted string representation.
     *
     * @param podcasts The set of podcasts to be converted to a string.
     * @return A formatted string representation of the podcasts.
     */
    private String podcastListToString(final LinkedHashSet<Podcast> podcasts) {
        StringBuilder result = new StringBuilder("[");
        Iterator<Podcast> iterator = podcasts.iterator();
        while (iterator.hasNext()) {
            Podcast podcast = iterator.next();
            result.append(podcast);
            result.append("\n");
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Converts an ArrayList of Song objects to a formatted string representation.
     *
     * @param songs The list of songs to be converted to a string.
     * @return A formatted string representation of the songs.
     */
    private String songListToString(final ArrayList<Song> songs) {
        StringBuilder result = new StringBuilder("[");
        Iterator<Song> iterator = songs.iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            result.append(song);
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
    /**
     * Converts an ArrayList of Playlist objects to a formatted string representation.
     *
     * @param playlistsToString The ArrayList of playlists to be converted to a string.
     * @return A formatted string containing information about the playlists.
     */
    private String playlistListToString(final ArrayList<Playlist> playlistsToString) {
        StringBuilder result = new StringBuilder("[");
        Iterator<Playlist> iterator = playlistsToString.iterator();
        while (iterator.hasNext()) {
            Playlist playlist = iterator.next();
            result.append(playlist);
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Prints information about a host's page, including podcasts and announcements.
     * Retrieves the host from HostsLibrary based on the application's current page owner.
     *
     * @return A formatted string containing information about podcasts and announcements.
     */
    private String printHostsPage() {
        Host host = HostsLibrary.getInstance().getHostByName(app.getPageOwner());
        LinkedHashSet<Podcast> podcasts = host.getPodcasts();
        LinkedHashSet<Announcement> announcements = host.getAnnouncements();
        return "Podcasts:\n\t" + podcastListToString(podcasts)
                + "\n\nAnnouncements:\n\t" + announcements;
    }

    /**
     * Performs printing for the current page based on the application state.
     *
     * @param command The input command for printing the current page.
     * @return A PageOutput containing the result of the print operation.
     */
    public PageOutput performPrintCurrentPage(final CommandInput command) {
        if (!app.isOnline()) {
            return new PageOutput(command, app.getUserOfflineMessage());
        }
        String message = switch (getApp().getPage()) {
            case homePage -> printHomePage();
            case artistPage -> printArtistsPage();
            case hostPage -> printHostsPage();
            case likedContentPage -> printLikedContentPage();
        };
        return new PageOutput(command, message);
    }

    /**
     * Checks if the user is deletable
     * The user is not deletable if other user is playing one of their playlists
     *
     * @return {@code true} if the user ise deletable, {@code false} otherwise
     */
    @Override
    public boolean isDeletable() {
        if (playlists == null) {
            return true;
        }
        for (Playlist playlist : playlists) {
            if (!playlist.isDeletable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public HashMap<String, Object> wrapped() {
        return getApp().getListenTracker().topListensForEach();
    }
}
