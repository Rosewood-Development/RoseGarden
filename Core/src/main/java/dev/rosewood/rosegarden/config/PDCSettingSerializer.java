package dev.rosewood.rosegarden.config;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public interface PDCSettingSerializer<T> extends SettingSerializer<T> {

    /**
     * Writes an object to a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to write to
     * @param key The key path to save in the config, if not namespaced, will be prefixed with the plugin path
     * @param value The value to save
     */
    void write(PersistentDataContainer container, String key, T value);

    /**
     * Writes an object to a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to write to
     * @param setting The setting to save as in the config, if not namespaced, will be prefixed with the plugin path
     * @param value The value to save
     */
    default void write(PersistentDataContainer container, RoseSetting<T> setting, T value) {
        this.write(container, setting.getKey(), value);
    }

    /**
     * Reads an object from a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to read from
     * @param key The key of the value in the container
     * @return the value read
     */
    T read(PersistentDataContainer container, String key);

    /**
     * Reads an object from a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to read from
     * @param setting The setting to save as in the config, if not namespaced, will be prefixed with the plugin path
     * @return the value read
     */
    default T read(PersistentDataContainer container, RoseSetting<T> setting) {
        return this.read(container, setting.getKey());
    }

    PersistentDataType<?, T> getPDCType();

}

