package fileio.input;

import commands.CommandType;
import commands.normalUser.searchBar.filter.Filters;

import java.util.ArrayList;
import java.util.Map;

public final class CommandInput {
    private CommandType command;
    private String username;
    private int timestamp;
    private String type;
    private Map<Filters, Object> filters;
    private int itemNumber;
    private int playlistId;
    private String playlistName;
    private int seed;
    private int age;
    private String city;
    private String name;
    private int releaseYear;
    private String description;
    private ArrayList<SongInput> songs;
    private ArrayList<EpisodeInput> episodes;
    private String date;
    private int price;
    private String nextPage;

    /**
     * Get the filters from the command input
     *
     * @return the filters
     */
    public Map<Filters, Object> getFilters() {
        return filters;
    }

    /**
     * Set the filters
     *
     * @param filters The filters to be set
     */
    public void setFilters(final Map<Filters, Object> filters) {
        this.filters = filters;
    }

    /**
     * Get the type from the command input
     *
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type
     *
     * @param type The type to be set
     */

    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Get the type of command form the command input
     *
     * @return The command type
     */
    public CommandType getCommand() {
        return command;
    }

    /**
     * Set the type of command
     *
     * @param command The command type to be set
     */
    public void setCommand(final CommandType command) {
        this.command = command;
    }

    /**
     * Get the username of the entities.user that used the command
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the entities.user that used the command
     *
     * @param username The username to be set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Get the time at which the command was performed
     *
     * @return The timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Set the time at which the command was performed
     *
     * @param timestamp The timestamp to be set
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the item number for the selection
     *
     * @return The item number
     */
    public int getItemNumber() {
        return itemNumber;
    }

    /**
     * Set the item number for the selection
     *
     * @param itemNumber The item number to be set
     */
    public void setItemNumber(final int itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * Get the playlist ID
     *
     * @return the id
     */
    public int getPlaylistId() {
        return playlistId;
    }

    /**
     * Set the playlist id
     *
     * @param playlistId the playlist id to be set
     */
    public void setPlaylistId(final int playlistId) {
        this.playlistId = playlistId;
    }

    /**
     * Get the new playlist's name
     *
     * @return the playlist name
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Sets the name of the new playlist that should be created
     *
     * @param playlistName the name that should be set
     */
    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    /**
     * Gets the seed used for shuffling
     * @return the seed
     */
    public int getSeed() {
        return seed;
    }

    /**
     * Sets the seed used for shuffling
     * @param seed the seed to be set
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<SongInput> getSongs() {
        return songs;
    }

    public void setSongs(final ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public ArrayList<EpisodeInput> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(final String nextPage) {
        this.nextPage = nextPage;
    }
}
