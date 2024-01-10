package commands.normalUser.searchBar;

/**
 * For search types
 */
public enum SearchType {
    song("song"), podcast("podcast"), playlist("playlist"), nothing("nothing"), artist("artist"),
    album("album"), host("host");
    private final String type;

    SearchType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Transforms the type given as an input from {@code String} to
     * {@code SearchType}
     *
     * @param type the type as a string
     * @return The type as a SearchType
     */
    public static SearchType fromString(final String type) {
        for (SearchType searchType : SearchType.values()) {
            if (searchType.type.equals(type)) {
                return searchType;
            }
        }
        throw new IllegalArgumentException("No SearchType with type " + type + " found");
    }
}
