package recommendation;

import commands.normalUser.searchBar.filter.filterAudio.FilterSongs;
import entities.audio.Song;
import entities.audio.collections.Playlist;
import entities.user.Artist;
import entities.user.NormalUser;
import libraries.audio.SongsLibrary;
import libraries.users.ArtistsLibrary;
import playables.PlayingAudio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Recommendation {
    private static final int FAN_SONGS_COUNT = 5;
    private static final int TOP_GENRES_COUNT = 3;
    private static final int SONGS_FROM_FIRST_GENRE = 5;
    private static final int SONGS_FROM_SECOND_GENRE = 3;
    private static final int SONGS_FROM_THIRD_GENRE = 2;
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;
    private static final int TIME = 30;

    private Recommendation() {
    }

    /**
     * Gets the top fans recommendations in a playlist
     * The artist for which we select the fans is the artist of the current
     * playing song in the specified user's player
     *
     * @param user The user for which we make the recommendation
     * @return A playlist with the recommended songs
     */
    public static Playlist fansRecommendations(final NormalUser user) {
        String artistName = user.getApp()
                .getPlayerManager()
                .getPlayingAudio()
                .getPlayingObject()
                .getOwner();
        String playlistName = artistName + " Fan Club recommendations";
        Playlist fansPlaylist = new Playlist(playlistName,
                artistName,
                new ArrayList<>());
        Artist artist = ArtistsLibrary.getInstance().getArtistByName(artistName);
        List<NormalUser> topFans = artist.getListenTracker().topFans();
        for (NormalUser fan : topFans) {
            Optional<Playlist> liked = Optional.ofNullable(fan.getLiked());
            List<Song> likedSongs = liked.map(likedObj ->
                            likedObj.getCollection().stream()
                                    .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                                    .filter(song -> !fansPlaylist.contains(song.getName()))
                                    .limit(FAN_SONGS_COUNT)
                                    .toList())
                    .orElse(List.of());

            for (Song song : likedSongs) {
                fansPlaylist.addItem(song);
            }
        }
        return fansPlaylist;
    }

    /**
     * Gets a random playlist recommendation
     *
     * @param user The user for which we make the recommendation
     * @return A playlist with the recommended songs
     */
    public static Playlist randomPlaylist(final NormalUser user) {
        String username = user.getName();
        Playlist playlist = new Playlist(username + "'s recommendations",
                username,
                new ArrayList<>());
        List<String> topGenres = getTopGenres(user);
        for (int i = 0; i < topGenres.size(); i++) {
            String genre = topGenres.get(i);

            FilterSongs filterSongs = new FilterSongs(SongsLibrary.getInstance().getItems());
            ArrayList<Song> songsForGenre = filterSongs.byGenre(genre).getFilteredObjects();
            List<Song> sortedSongsForGenre = songsForGenre.stream()
                    .sorted(Comparator.comparing(Song::getLikes))
                    .toList();

            int limit = switch (i) {
                case FIRST -> SONGS_FROM_FIRST_GENRE;
                case SECOND -> SONGS_FROM_SECOND_GENRE;
                case THIRD -> SONGS_FROM_THIRD_GENRE;
                default -> 0;
            };
            sortedSongsForGenre.stream()
                    .filter(song -> !playlist.contains(song.getName()))
                    .limit(limit)
                    .forEach(playlist::addItem);
        }
        return playlist;
    }

    /**
     * Gets the top 3 genres for the specified user
     * It searches through all the songs in: liked songs, followed playlists and own playlists
     * And counts how many times each genre occurs
     *
     * @param user The user we calculate the top genres for
     * @return A list with the top genres names
     */
    private static List<String> getTopGenres(final NormalUser user) {
        Playlist liked = user.getLiked();
        HashMap<String, Long> genres;
        if (liked != null) {
            ArrayList<Song> likedSongs = user.getLiked().getCollection();
            genres = likedSongs.stream()
                    .map(Song::getGenre)
                    .collect(Collectors.groupingBy(Function.identity(),
                            HashMap<String, Long>::new,
                            Collectors.counting()));
        } else {
            genres = new HashMap<>();
        }

        ArrayList<Playlist> playlists = user.getPlaylists();
        if (playlists != null) {
            for (Playlist playlist : playlists) {
                merge(genres, playlist);
            }
        }

        ArrayList<Playlist> followedPlaylists = user.getFollowedPlaylists();
        if (followedPlaylists != null) {
            for (Playlist playlist : followedPlaylists) {
                merge(genres, playlist);
            }
        }

        List<String> topGenres = genres.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        return topGenres.subList(0, Math.min(topGenres.size(), TOP_GENRES_COUNT));
    }

    /**
     * Merge a map with kept genres count with the genres count calculated for a playlist
     *
     * @param mainMap The map to merge to
     * @param playlistToMerge The playlist to merge from
     */
    private static void merge(final HashMap<String, Long> mainMap,
                              final Playlist playlistToMerge) {
        HashMap<String, Long> genresInPlaylist = playlistToMerge.getCollection()
                .stream()
                .map(Song::getGenre)
                .collect(Collectors.groupingBy(Function.identity(),
                        HashMap::new,
                        Collectors.counting()));
        genresInPlaylist.forEach((genre, count) -> mainMap.merge(genre, count, Long::sum));
    }

    /**
     * Gets a random song recommendation
     *
     * @param user The user for which we make the recommendation
     * @return A random song from the same genre as the one listened by the user
     * at the moment
     */
    public static Song randomSong(final NormalUser user) {
        PlayingAudio<?> playingAudio = user.getApp()
                .getPlayerManager()
                .getPlayingAudio();

        if (playingAudio == null) {
            return null;
        }

        Song song = (Song) user.getApp()
                .getPlayerManager()
                .getPlayingAudio()
                .getPlayingObject();

        if (song == null) {
            return null;
        }

        int remainedTime = playingAudio.getRemainedTime();
        int elapsedTime = song.getDuration() - remainedTime;
        if (elapsedTime < TIME) {
            return null;
        }

        String genre = song.getGenre();

        FilterSongs filterSongs = new FilterSongs(SongsLibrary.getInstance().getItems());
        ArrayList<Song> songsForGenre = filterSongs.byGenre(genre).getFilteredObjects();

        Random random = new Random(elapsedTime);
        int index = random.nextInt(songsForGenre.size());
        return songsForGenre.get(index);
    }
}
