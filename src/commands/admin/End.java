package commands.admin;

import fileio.output.Output;
import libraries.users.ArtistsLibrary;
import user.Artist;

import java.util.ArrayList;
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
        ArrayList<Artist> artists = ArtistsLibrary.getInstance().getItems();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Artist artist : artists) {
            if (!artist.getListenTracker().wasListened()) {
                continue;
            }
            LinkedHashMap<String, Object> stats = new LinkedHashMap<>();
            stats.put("merchRevenue", artist.getMerchRevenue());
            if (artist.getSongsRevenue().isEmpty()) {
                stats.put("songRevenue", 0.0);
            }

            stats.put("ranking", result.size() + 1);
            if (artist.getSongsRevenue().isEmpty()) {
                stats.put("mostProfitableSong", "N/A");
            }
            result.put(artist.getUsername(), stats);
        }
        return new Output(endProgram, result);
    }
}
