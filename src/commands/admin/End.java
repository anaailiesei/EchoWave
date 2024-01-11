package commands.admin;

import entities.user.NormalUser;
import fileio.output.Output;
import libraries.users.ArtistsLibrary;
import entities.user.Artist;
import libraries.users.NormalUsersLibrary;
import statistics.calculator.ArtistCalculateRevenue;
import statistics.calculator.NormalUserCalculateRevenue;
import statistics.calculator.RevenueCalculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static commands.CommandType.endProgram;

public final class End {
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
            calculator.calculateRevenue(new NormalUserCalculateRevenue(user));
        }

        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        for (Artist artist : artists) {
            calculator.calculateRevenue(new ArtistCalculateRevenue(artist));
        }

        artists.sort(Comparator.comparing(Artist::getSongsRevenue).reversed()
                .thenComparing(Artist::getName));
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Artist artist : artists) {
            if (!artist.getListenTracker().wasListened()) {
                continue;
            }
            LinkedHashMap<String, Object> stats = new LinkedHashMap<>();
            stats.put("merchRevenue", artist.getMerchRevenue());
            double songsRevenue = Math.round(artist.getSongsRevenue() * 100.0) / 100.0;
            stats.put("songRevenue", songsRevenue);
//            stats.put("songRevenue", artist.getSongsRevenueList());

            stats.put("ranking", result.size() + 1);
            String mostProfitableSong = artist.getMostProfitableSong();
            stats.put("mostProfitableSong", mostProfitableSong);
            result.put(artist.getName(), stats);
        }
        return new Output(endProgram, result);
    }
}
