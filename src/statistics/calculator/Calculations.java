package statistics.calculator;

import entities.audio.Song;
import entities.user.Artist;
import libraries.audio.SongsLibrary;
import libraries.users.ArtistsLibrary;

import java.util.Map;
import java.util.TreeMap;

public final class Calculations {
    private Calculations() {
    }

    /**
     * Calculate the revenue that belongs to each song
     *
     * @param totalValue The total value that's being divided between songs
     * @param songs      The songs and their respective number of listens
     */
    public static void addSongsRevenue(final Integer totalValue,
                                       final TreeMap<Song, Integer> songs) {
        if (songs.isEmpty()) {
            return;
        }
        int totalListens = songs.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<Song, Integer> entry : songs.entrySet()) {
            Song song = entry.getKey();
            Integer listens = entry.getValue();
            double revenue = revenueForSong(totalValue, totalListens, listens);
            if (!SongsLibrary.getInstance().getItems().contains(song)) {
                String owner = song.getOwner();
                Artist artist = ArtistsLibrary.getInstance().getArtistByName(owner);
                artist.addSongRevenue(revenue);
            }
            song.addRevenue(revenue);
        }
    }

    /**
     * Calculate the revenue for a certain song for a certain user
     *
     * @param totalListens The total song listens for a certain user
     * @param songListens  The total song listen from the user for a specific song
     * @param totalValue   The total value that's being divided between songs
     * @return The value for the revenue the song's artist will get from this user
     */
    private static double revenueForSong(final Integer totalValue,
                                           final Integer totalListens,
                                           final Integer songListens) {
        return (double) (totalValue * songListens) / totalListens;
    }
}
