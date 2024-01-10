package playables;

public interface Playing {
    /**
     * Changes the remaining time of the track based on the time passed if the track is playing
     *
     * @param timePassed the time that has passed
     */
    void addTimePassed(int timePassed);

    /**
     * Pauses the track
     */
    void pause();

    /**
     * Resumes the track
     */
    void resume();
}
