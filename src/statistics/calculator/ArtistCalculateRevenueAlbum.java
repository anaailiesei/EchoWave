package statistics.calculator;

import entities.audio.Song;
import entities.audio.collections.Album;
import entities.user.Artist;

import java.util.ArrayList;
import java.util.TreeMap;

public final class ArtistCalculateRevenueAlbum implements CalculateRevenueStrategy {
    private final Artist artist;
    private final Album album;

    public ArtistCalculateRevenueAlbum(final Artist artist, final Album album) {
        this.artist = artist;
        this.album = album;
    }

    @Override
    public void calculateRevenue() {
        TreeMap<String, Double> songsRevenueList = new TreeMap<>();
        ArrayList<Song> songs = album.getCollection();
        for (Song song : songs) {
            double revenue = song.getRevenue();
            if (revenue > 0) {
                songsRevenueList.put(song.getName(), revenue);
                artist.addSongRevenue(revenue);
            }
        }

//        LinkedHashMap<String, Double> songsRevenueMap = songsRevenueList.entrySet()
//                .stream()
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));
//        artist.setSongsRevenueList(songsRevenueMap);
    }
}
