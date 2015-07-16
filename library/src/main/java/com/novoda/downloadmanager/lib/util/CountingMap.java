package com.novoda.downloadmanager.lib.util;

import java.util.HashMap;
import java.util.Map;

public class CountingMap<T> {
    private final Map<T, Integer> map;

    public CountingMap(int size) {
        this.map = new HashMap<>(size);
    }

    public void incrementOnKey(T key) {
        int currentStatusCount = map.containsKey(key) ? map.get(key) : 0;
        map.put(key, currentStatusCount + 1);
    }

    public boolean hasCountForKey(T key) {
        return map.containsKey(key) && map.get(key) > 0;
    }

    public void clear() {
        map.clear();
    }
}
