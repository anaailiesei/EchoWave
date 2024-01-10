package commands.normalUser.player;

public enum RepeatType {
    noRepeat("No Repeat"),
    repeatOnce("Repeat Once"),
    repeatInfinite("Repeat Infinite"),
    repeatAll("Repeat All"),
    repeatCurrent("Repeat Current Song");
    private final String value;

    RepeatType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
