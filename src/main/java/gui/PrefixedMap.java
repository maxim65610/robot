package gui;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * Реализация Map с префиксом, которая хранит ключи с определённым префиксом.
 * Все операции с Map, такие, как вставка и извлечение значений, автоматически
 * добавляют или удаляют префикс к ключу.
 */
public class PrefixedMap extends AbstractMap<String, String> {
    private final Map<String, String> baseMap;
    private final String prefix;

    public PrefixedMap(Map<String, String> baseMap, String prefix) {
        this.baseMap = baseMap;
        this.prefix = prefix + ".";
    }
    /**
     * Префикс добавляется перед ключом при вставке значения.
     */
    @Override
    public String put(String key, String value) {
        return baseMap.put(prefix + key, value);
    }
    /**
     * Извлекает значение из Map, используя ключ с добавленным префиксом.
     */
    @Override
    public String get(Object key) {
        return baseMap.get(prefix + key);
    }
    /**
     * Возвращает набор всех записей карты, исключая префикс в ключах.
     */
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