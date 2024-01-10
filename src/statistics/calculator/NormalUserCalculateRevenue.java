package statistics.calculator;

import entities.audio.Song;
import entities.user.NormalUser;

import java.util.Map;
import java.util.TreeMap;

public class NormalUserCalculateRevenue implements CalculateRevenueStrategy {
    private final NormalUser user;
    public NormalUserCalculateRevenue(final NormalUser user) {
        this.user = user;
    }
    @Override
    public void calculateRevenue() {
        TreeMap<Song, Integer> premiumListens = user.getPremiumSongs();
        Integer balance = 1000000;
        Calculations.addSongsRevenue(balance, premiumListens);
    }
}
