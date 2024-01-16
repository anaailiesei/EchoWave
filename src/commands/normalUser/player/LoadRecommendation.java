package commands.normalUser.player;

import entities.audio.Audio;
import entities.audio.collections.Collection;
import entities.user.NormalUser;
import lombok.Getter;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import managers.normalUser.SearchBarManager;
import statistics.listenTrackers.ListenTrackerNormalUser;

public final class LoadRecommendation extends Load {
    private NormalUser user;
    private final PlayerManager playerManager;
    /**
     * -- GETTER --
     *  Checks if source was successfully loaded
     */
    @Getter
    private boolean successfullyLoaded = false;

    public LoadRecommendation(final PlayerManager playerManager,
                     final SearchBarManager searchBarManager,
                     final Shuffle shuffle,
                     final ListenTrackerNormalUser listenTracker) {
        super(playerManager,
                shuffle,
                listenTracker);
        this.playerManager = playerManager;
    }

    /**
     * Sets the user for the load operation
     *
     * @param newUser The user to be set
     * @return the current instance
     * @see NormalUser
     */
    public LoadRecommendation setUser(final NormalUser newUser) {
        super.setUser(newUser);
        this.user = newUser;
        return this;
    }

    /**
     * Executes the load operation if the source was selected
     */
    public void execute() {
        Audio selectedObject = user.getLastRecommendation();
        successfullyLoaded = super.execute(selectedObject);
        setMessage(toString());
    }

    @Override
    public String toString() {
        if (!successfullyLoaded) {
            return "No recommendations available.";
        } else if (CheckClass.extendsCollection(playerManager.getLoadedObject().getClass())) {
            Collection<?> loadedCollection = (Collection<?>) playerManager
                    .getLoadedObject();
            if (loadedCollection.isEmpty()) {
                return "You can't load an empty audio collection!";
            }
        }
        return "Playback loaded successfully.";
    }
}
