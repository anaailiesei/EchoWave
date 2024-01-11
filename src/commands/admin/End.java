package commands.admin;

import entities.user.NormalUser;
import fileio.output.Output;
import libraries.users.ArtistsLibrary;
import entities.user.Artist;
import libraries.users.NormalUsersLibrary;
import statistics.calculator.ArtistCalculateRevenue;
import statistics.calculator.PremiumSongCalculateRevenue;
import statistics.calculator.RevenueCalculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static commands.CommandType.endProgram;

public final class End {
    private static final double PERCENT = 100;
    private End() {
    }

    /**
     * Perform the endProgram command (show stats for listened artists)
     *
     * @return an Output object with the command output
     */
    public static Output performEndProgram() {
        RevenueCalculator calculator = new RevenueCalculator();
        ArrayList<NormalUser> users = NormalUsersLibrary.getInstance().getItems();
        for (NormalUser user : users) {
            calculator.calculateRevenue(new PremiumSongCalculateRevenue(user));
        }

        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        for (Artist artist : artists) {
            calculator.calculateRevenue(new ArtistCalculateRevenue(artist));
        }

        artists.sort(Comparator.comparing(Artist::getTotalRevenue).reversed()
                .thenComparing(Artist::getName));
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Artist artist : artists) {
            if (!artist.getListenTracker().wasListened() && artist.getMerchRevenue() == 0) {
                continue;
            }
            LinkedHashMap<String, Object> stats = new LinkedHashMap<>();
            stats.put("merchRevenue", artist.getMerchRevenue());
            double songsRevenue = roundTwoDecimals(artist.getSongsRevenue());
            stats.put("songRevenue", songsRevenue);
//            stats.put("songRevenue", artist.getSongsRevenueList());

            stats.put("ranking", result.size() + 1);
            String mostProfitableSong = artist.getMostProfitableSong();
            stats.put("mostProfitableSong", mostProfitableSong);
            result.put(artist.getName(), stats);
        }
        return new Output(endProgram, result);
    }

    /**
     * Round a number to 2 decimals places
     * @param number The number to be rounded
     *
     * @return The rounded number
     */
    private static double roundTwoDecimals(final double number) {
        return Math.round(number * PERCENT) / PERCENT;
    }
}
