package dev.rosewood.rosegarden.utils;

import dev.rosewood.rosegarden.RosePlugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;

/**
 * Maintains a cache of NamespacedKeys.
 * Automatically sets the RoseGarden plugin name as the namespace if one is not provided.
 */
public final class KeyHelper {

    private static final RosePlugin PLUGIN = RosePlugin.instance();
    private static final Map<String, NamespacedKey> CACHE = new HashMap<>();

    private KeyHelper() {

    }

    /**
     * Return a NamespacedKey from the given string.
     * If a namespace is not provided, the RoseGarden plugin name will be used.
     *
     * @param key The string key
     * @return the created NamespacedKey
     */
    public static NamespacedKey get(String key) {
        if (key.indexOf(':') != -1)
            return CACHE.computeIfAbsent(key, x -> NamespacedKey.fromString(key));
        return CACHE.computeIfAbsent(key, x -> new NamespacedKey(PLUGIN, x));
    }

}
