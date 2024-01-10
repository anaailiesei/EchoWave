package statistics.calculator;

import entities.audio.Song;

import java.util.Map;
import java.util.TreeMap;

public class FreeSongCalculateRevenue implements CalculateRevenueStrategy {
    private final TreeMap<Song, Integer> freeSongs;
    private final Integer adPrice;
    public FreeSongCalculateRevenue(final TreeMap<Song, Integer> songs, Integer adPrice) {
        this.freeSongs = songs;
        this.adPrice = adPrice;
    }
    @Override
    public void calculateRevenue() {
        Calculations.addSongsRevenue(adPrice, freeSongs);
    }
}
