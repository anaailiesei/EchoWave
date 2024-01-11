package profile.artist;

import lombok.Getter;

public final class Merch {
    @Getter
    private final String name;
    @Getter
    private final int price;
    private final String description;

    public Merch(final String name, final int price, final String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - " + price + ":\n\t" + description;
    }
}
