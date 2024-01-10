package user;

public enum UserType {
    user("user"), artist("artist"), host("host");
    private final String type;

    UserType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Transforms the type given as an input from {@code String} to
     * {@code UserType}
     *
     * @param type the type as a string
     * @return The type as an UserType
     */
    public static UserType fromString(final String type) {
        for (UserType userType : UserType.values()) {
            if (userType.type.equals(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("No UserType with type " + type + " found");
    }
}
