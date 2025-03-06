package gui;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrefixedMap extends AbstractMap<String, String> {
    private final Map<String, String> baseMap;
    private final String prefix;

    public PrefixedMap(Map<String, String> baseMap, String prefix) {
        this.baseMap = baseMap;
        this.prefix = prefix + ".";
    }

    @Override
    public String put(String key, String value) {
        return baseMap.put(prefix + key, value);
    }

    @Override
    public String get(Object key) {
        return baseMap.get(prefix + key);
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
        Set<Map.Entry<String, String>> entries = new HashSet<>();
        for (Map.Entry<String, String> entry : baseMap.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String keyWithoutPrefix = entry.getKey().substring(prefix.length());
                entries.add(new AbstractMap.SimpleEntry<>(keyWithoutPrefix, entry.getValue()));
            }
        }
        return entries;
    }
}