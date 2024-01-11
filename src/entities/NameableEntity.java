package entities;

public final class NameableEntity implements Entity {
    private final String name;
    public NameableEntity(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
