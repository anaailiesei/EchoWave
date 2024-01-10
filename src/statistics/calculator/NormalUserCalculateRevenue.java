package statistics.calculator;

import entities.audio.Song;
import entities.user.NormalUser;
import entities.user.User;

import java.util.Map;
import java.util.TreeMap;

public class NormalUserCalculateRevenue implements CalculateRevenueStrategy<NormalUser> {
    @Override
    public void calculateRevenue(NormalUser user) {
        TreeMap<Song, Integer> premiumListens = user.getPremiumSongs();
        if (premiumListens.isEmpty()) {
            return;
        }
        int totalListens = premiumListens.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<Song, Integer> entry : premiumListens.entrySet()) {
            Song song = entry.getKey();
            Integer listens = entry.getValue();

            float revenue = revenueForSong(totalListens, listens);
            song.addRevenue(revenue);
        }
    }

    private float revenueForSong(Integer totalListens, Integer songListens) {
        Integer balance = 1000000;
        return (float) (balance * songListens) / totalListens;
    }
}
