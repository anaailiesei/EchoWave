package entities.audio.collections;

import lombok.Getter;

/**
 * Enum for playlist visibility. Possible values: public and private
 */
@Getter
public enum Visibility {
    PUBLIC("public"),
    PRIVATE("private");
    private final String value;

    Visibility(final String visibility) {
        this.value = visibility;
    }
}
