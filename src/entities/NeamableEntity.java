package entities;

public class NeamableEntity implements Entity {
    private final String name;
    public NeamableEntity(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
