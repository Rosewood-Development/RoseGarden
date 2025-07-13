package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.utils.KeyHelper;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCDelegatingSettingSerializer<T> extends BaseSettingSerializer<T> implements PDCSettingSerializer<T> {

    private final SettingSerializer<T> serializer;
    private final PersistentDataType<?, T> persistentDataType;

    public PDCDelegatingSettingSerializer(SettingSerializer<T> serializer, PersistentDataType<?, T> persistentDataType) {
        super(serializer.getType(), serializer::asStringKey, serializer::fromStringKey);
        this.serializer = serializer;
        this.persistentDataType = persistentDataType;
    }

    /**
     * Writes an object to a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to write to
     * @param key The key path to save in the config, if not namespaced, will be prefixed with the plugin path
     * @param value The value to save
     */
    public void write(PersistentDataContainer container, String key, T value) {
        container.set(KeyHelper.get(key), this.persistentDataType, value);
    }

    /**
     * Writes an object to a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to write to
     * @param setting The setting to save as in the config, if not namespaced, will be prefixed with the plugin path
     * @param value The value to save
     */
    public final void write(PersistentDataContainer container, RoseSetting<T> setting, T value) {
        this.write(container, setting.getKey(), value);
    }

    /**
     * Reads an object from a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to read from
     * @param key The key of the value in the container
     * @return the value read
     */
    public T read(PersistentDataContainer container, String key) {
        return container.get(KeyHelper.get(key), this.persistentDataType);
    }

    /**
     * Reads an object from a PersistentDataContainer
     *
     * @param container The PersistentDataContainer to read from
     * @param setting The setting to save as in the config, if not namespaced, will be prefixed with the plugin path
     * @return the value read
     */
    public final T read(PersistentDataContainer container, RoseSetting<T> setting) {
        return this.read(container, setting.getKey());
    }

    @Override
    public PersistentDataType<?, T> getPDCType() {
        return this.persistentDataType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PDCDelegatingSettingSerializer)) return false;
        PDCDelegatingSettingSerializer<?> that = (PDCDelegatingSettingSerializer<?>) o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.persistentDataType, that.persistentDataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.persistentDataType);
    }

    @Override
    public void write(ConfigurationSection config, String key, T value, String... comments) {
        this.serializer.write(config, key, value, comments);
    }

    @Override
    public void writeWithDefault(ConfigurationSection config, String key, T value, String... comments) {
        this.serializer.writeWithDefault(config, key, value, comments);
    }

    @Override
    public String getDefaultCommentText(T value) {
        return this.serializer.getDefaultCommentText(value);
    }

    @Override
    public T read(ConfigurationSection config, String key) {
        return this.serializer.read(config, key);
    }

    @Override
    public boolean readIsValid(ConfigurationSection config, String key) {
        return this.serializer.readIsValid(config, key);
    }

}
