package recommendation;

import entities.audio.Song;
import entities.audio.collections.Playlist;
import entities.user.Artist;
import entities.user.NormalUser;
import libraries.users.ArtistsLibrary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class Recommendation {
    private static final int FAN_SONGS_COUNT = 5;

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
        // TODO: make it so this doesnt have duplicates
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
                                    .limit(FAN_SONGS_COUNT)
                                    .toList())
                    .orElse(List.of());

            for (Song song : likedSongs) {
                fansPlaylist.addItem(song);
            }
        }
        return fansPlaylist;
    }
}
