package statistics.calculator;

import entities.audio.Song;
import entities.user.NormalUser;

import java.util.TreeMap;

public final class PremiumSongCalculateRevenue implements CalculateRevenueStrategy {
    private static final int BALANCE = 1000000;
    private final NormalUser user;

    public PremiumSongCalculateRevenue(final NormalUser user) {
        this.user = user;
    }

    @Override
    public void calculateRevenue() {
        TreeMap<Song, Integer> premiumListens = user.getPremiumSongs();
        Calculations.addSongsRevenue(BALANCE, premiumListens);
    }
}
