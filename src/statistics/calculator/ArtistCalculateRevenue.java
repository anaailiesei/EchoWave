package statistics.calculator;

import entities.audio.Song;
import entities.audio.collections.Album;
import entities.user.Artist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class ArtistCalculateRevenue implements CalculateRevenueStrategy {
    private final Artist artist;

    public ArtistCalculateRevenue(final Artist artist) {
        this.artist = artist;
    }

    @Override
    public void calculateRevenue() {
        TreeMap<String, Double> songsRevenueList = new TreeMap<>();
        HashSet<Album> albums = artist.getAlbums();
        for (Album album : albums) {
            ArrayList<Song> songs = album.getCollection();
            for (Song song : songs) {
                double revenue = song.getRevenue();
                if (revenue > 0) {
                    songsRevenueList.merge(song.getName(), revenue, Double::sum);
                    artist.addSongRevenue(revenue);
                }
            }
        }
        LinkedHashMap<String, Double> songsRevenueMap = songsRevenueList.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        artist.setSongsRevenueList(songsRevenueMap);
    }
}
