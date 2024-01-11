package statistics.listenTrackers;

import entities.Entity;
import lombok.Getter;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Getter
public class ListenTracker<E extends Entity> {
    private static final int NUMBER_TOP_RESULTS = 5;
    private final TreeMap<E, Integer> listens = new TreeMap<>(Comparator.comparing(E::getName));


    /**
     * Adds a listen to the specified entity
     *
     * @param entity The name fo the entity for which we add listens
     */
    public void addListen(final E entity) {
        listens.put(entity, listens.getOrDefault(entity, 0) + 1);
    }

    /**
     * Adds the specified number of listens to the specified entity
     *
     * @param entity The name fo the entity for which we add listens
     * @param count  The number of listens to add
     */
    public void addListen(final E entity, final int count) {
        listens.put(entity, listens.getOrDefault(entity, 0) + count);
    }

    /**
     * Gets the listen count for the specified entity
     *
     * @param entity The entity name
     * @return The number of listens
     */
    public int getListenCount(final E entity) {
        return listens.getOrDefault(entity, 0);
    }

    /**
     * Gets a map with the top five listens for the tracker
     * (the name of the entity and the number of listens)
     *
     * @return The map
     */
    public LinkedHashMap<String, Integer> getTopFiveListens() {
        return listens.entrySet().stream()
                .sorted(Map.Entry
                        .<E, Integer>comparingByValue()
                        .reversed()
                        .thenComparing(entry -> entry.getKey().getName()))
                .limit(NUMBER_TOP_RESULTS)
                .collect(Collectors
                        .toMap(entry -> entry.getKey().getName(),
                                Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Gets the size of the listen tracker
     *
     * @return The size
     */
    public int getSize() {
        return listens.size();
    }

    /**
     * Checks if the listen tracker is empty (no listens were registered)
     *
     * @return {@code true} if the tracker is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return listens.isEmpty();
    }

    /**
     * Empties the tracker
     */
    public void clear() {
        listens.clear();
    }
}
