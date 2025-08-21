package dev.rosewood.rosegarden.gui.provider;

import dev.rosewood.rosegarden.gui.parameter.Context;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A serializable object that provides information for an icon.
 */
public interface Provider<T> {

    /**
     * Serializes a {@link Provider} to a {@link ConfigurationSection}.
     * @param section The {@link ConfigurationSection} to serialize to.
     */
    void write(ConfigurationSection section);

    /**
     * @return Whatever the provider desires.
     */
    T get(Context context);

    /**
     * @return The type of this provider.
     *          Only one of each provider type is allowed per icon.
     */
    String getType();

    /**
     * @return The configuration key for this provider.
     */
    String getKey();

}
