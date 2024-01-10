package commands.normalUser.searchBar.filter.filterAudio;

import entities.audio.Song;
import commands.normalUser.searchBar.filter.Filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For filtering songs
 */
public final class FilterSongs extends FilterAudio<Song> {
    public FilterSongs(final ArrayList<Song> songs) {
        this.filteredObjects = new ArrayList<>(songs);
        initAllFilterMethods();
    }

    @Override
    public void initAllFilterMethods() {
        filterMethods = new HashMap<>();
        filterMethods.put(Filters.name, this::byName);
        filterMethods.put(Filters.album, this::byAlbum);
        filterMethods.put(Filters.lyrics, this::byLyrics);
        filterMethods.put(Filters.genre, this::byGenre);
        filterMethods.put(Filters.artist, this::byArtist);
        filterMethods.put(Filters.releaseYear, this::byReleaseYear);
        filterMethods.put(Filters.tags, this::byTags);
    }

    /**
     * Filters songs that contain the specified album
     *
     * @param albumObj The album in which the songs should be part of
     * @return current instance
     */
    public FilterSongs byAlbum(final Object albumObj) {
        String album = (String) albumObj;
        filteredObjects.removeIf(song -> !song.inAlbum(album));
        return this;
    }

    /**
     * Filters songs that contain at least one of the specified tags
     *
     * @param tagsObj The tags we check for
     * @return current instance
     */
    public FilterSongs byTags(final Object tagsObj) {
        ArrayList<String> tags = (ArrayList<String>) tagsObj;
        filteredObjects.removeIf(song -> !song.containsTags(tags));
        return this;
    }

    /**
     * Filters songs that contain the lyrics specified
     *
     * @param lyricsObj The lyrics we search for in the song
     * @return current instance
     */
    public FilterSongs byLyrics(final Object lyricsObj) {
        String lyrics = (String) lyricsObj;
        filteredObjects.removeIf(song -> !song.containsLyrics(lyrics));
        return this;
    }

    /**
     * Filters songs that are part of the specified genre
     *
     * @param genreObj The genre the songs should be part of
     * @return current instance
     */
    public FilterSongs byGenre(final Object genreObj) {
        String genre = (String) genreObj;
        filteredObjects.removeIf(song -> !song.isGenre(genre));
        return this;
    }

    /**
     * Filters songs that were released before the specified year
     *
     * @param year The year we should compare the release year to
     */
    private void filterSongsBeforeYear(final Integer year) {
        filteredObjects.removeIf(song -> !song.releasedBeforeYear(year));
    }

    /**
     * Filters songs that were released after the specified year
     *
     * @param year The year we should compare the release year to
     */
    private void filterSongsAfterYear(final Integer year) {
        filteredObjects.removeIf(song -> !song.releasedAfterYear(year));
    }

    /**
     * Parses the input string into an integer for the year and a symbol (lesser or greater)
     *
     * @param yearQuery The search string for the year
     * @return the matched groups (symbol + integer)
     */
    private Matcher parseYearString(final String yearQuery) {
        Pattern yearPattern = Pattern.compile("([<>])(\\d+)");
        return yearPattern.matcher(yearQuery);
    }

    /**
     * Gets the year (value) from the specified year query
     *
     * @param yearQuery The string we search in for the year
     * @return the year
     */
    private Integer getYearFromString(final String yearQuery) {
        Matcher matcher = parseYearString(yearQuery);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(2));
        }
        return null;
    }

    /**
     * Gets the comparison operator from the specified year query
     *
     * @param yearQuery The string we search in for the comparison symbol
     * @return the comparison operator (lesser or greater)
     */
    private Comparison getComparisonFromString(final String yearQuery) {
        Comparison lesser = Comparison.lesser;
        Comparison greater = Comparison.greater;
        Matcher matcher = parseYearString(yearQuery);
        boolean ret = matcher.find();
        if (ret) {
            if (matcher.group(1).equals(lesser.getSymbol())) {
                return lesser;
            } else if (matcher.group(1).equals(greater.getSymbol())) {
                return greater;
            }
        }
        return null;
    }

    /**
     * Filters songs by their release year as specified by the year query
     *
     * @param yearQueryObj The year we search the songs by
     * @return current instance
     */
    public FilterSongs byReleaseYear(final Object yearQueryObj) {
        String yearQuery = (String) yearQueryObj;
        Integer year = getYearFromString(yearQuery);
        if (Objects.equals(getComparisonFromString(yearQuery), Comparison.lesser)) {
            filterSongsBeforeYear(year);
        } else if (Objects.equals(getComparisonFromString(yearQuery), Comparison.greater)) {
            filterSongsAfterYear(year);
        } else {
            filteredObjects.clear();
        }
        return this;
    }

    /**
     * Filters songs that were created by the specified artist
     *
     * @param artistObj The creator of the songs
     * @return a list of filtered songs
     */
    public FilterSongs byArtist(final Object artistObj) {
        String artist = (String) artistObj;
        filteredObjects.removeIf(song -> !song.createdBy(artist));
        return this;
    }

    /**
     * For comparisons between integers
     */
    enum Comparison {
        greater(">"), lesser("<");
        private final String symbol;

        Comparison(final String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
