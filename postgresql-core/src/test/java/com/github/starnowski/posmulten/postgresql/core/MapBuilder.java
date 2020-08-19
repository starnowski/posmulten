package com.github.starnowski.posmulten.postgresql.core;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder <K, V>
{
    private Map<K, V> map = new HashMap<>();

    public MapBuilder put(K key, V value)
    {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build()
    {
        return map;
    }

    public static <K, V> MapBuilder<K, V> mapBuilder()
    {
        return new MapBuilder<>();
    }
}

