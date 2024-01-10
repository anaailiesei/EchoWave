package managers;

import audio.Audio;
import audio.Song;
import audio.collections.Album;
import audio.collections.Collection;
import audio.collections.Playlist;
import audio.collections.Podcast;
import user.Artist;
import user.Host;
import user.NormalUser;
import user.User;

public final class CheckClass {
    private CheckClass() {
    }

    /**
     * Checks if the specified class is a {@code Podcast}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code Podcast}, {@code false} otherwise
     * @see Podcast
     */
    public static boolean isPodcast(final Class<?> unknownClass) {
        return unknownClass == Podcast.class;
    }

    /**
     * Checks if the specified class is a {@code Playlist}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code Playlist}, {@code false} otherwise
     * @see Playlist
     */
    public static boolean isPlaylist(final Class<?> unknownClass) {
        return unknownClass == Playlist.class;
    }

    /**
     * Checks if the specified class is a {@code Song}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code Song}, {@code false} otherwise
     * @see Song
     */
    public static boolean isSong(final Class<?> unknownClass) {
        return unknownClass == Song.class;
    }

    /**
     * Checks if the specified class is a {@code Album}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code Album}, {@code false} otherwise
     * @see Album
     */
    public static boolean isAlbum(final Class<?> unknownClass) {
        return unknownClass == Album.class;
    }
    /**
     * Checks if the specified class is a {@code Artist}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code Artist}, {@code false} otherwise
     * @see Artist
     */
    public static boolean isArtist(final Class<?> unknownClass) {
        return unknownClass == Artist.class;
    }

    /**
     * Checks if a class extends the {@code Collection} class
     *
     * @param unknownClass the class to be checked
     * @return {@code true} if the class extends {@code Collection}, {@code false} otherwise
     * @see Collection
     */
    public static boolean extendsCollection(final Class<?> unknownClass) {
        Class<?> clazz = unknownClass;
        while (clazz != null) {
            if (clazz == Collection.class) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    /**
     * Checks if the specified class is a {@code Host}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code Host}, {@code false} otherwise
     * @see Host
     */
    public static boolean isHost(final Class<?> unknownClass) {
        return unknownClass == Host.class;
    }

    /**
     * Checks if the specified class is a {@code NormalUser}
     *
     * @param unknownClass The class to be checked
     * @return {@code true} if the class is {@code NormalUser}, {@code false} otherwise
     * @see NormalUser
     */
    public static boolean isNormalUser(final Class<? extends User> unknownClass) {
        return unknownClass == NormalUser.class;
    }

    public static boolean isEpisode(Class<? extends Audio> aClass) {
        return aClass == Podcast.class;
    }
}
