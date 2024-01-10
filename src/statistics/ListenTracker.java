package statistics;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ListenTracker {
    private TreeMap<String, Integer> listens;

    public ListenTracker() {
        this.listens = new TreeMap<>();
    }

    public void addListen(String entity) {
        listens.put(entity, listens.getOrDefault(entity, 0) + 1);
    }

    public void addListen(String entity, int count) {
        listens.put(entity, listens.getOrDefault(entity, 0) + count);
    }

    public int getListenCount(String entity) {
        return listens.getOrDefault(entity, 0);
    }

    public LinkedHashMap<String, Integer> getTopFiveListens() {
        return listens.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
                .limit(5)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public int getSize() {
        return listens.size();
    }

    public boolean isEmpty() {
        return listens.isEmpty();
    }
}
