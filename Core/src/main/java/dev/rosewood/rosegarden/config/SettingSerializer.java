package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataType;

/**
 * Allows reading and writing an object type to/from YAML.
 * Supports comments.
 * @param <T> The serialized type
 */
public interface SettingSerializer<T> {

    /**
     * Writes an object to a ConfigurationSection, including comments
     *
     * @param config The ConfigurationSection to write to
     * @param key The key path to save in the config
     * @param value The value to save
     * @param comments Comments to write above the setting in the config
     */
    void write(ConfigurationSection config, String key, T value, String... comments);

    /**
     * Writes an object to a ConfigurationSection, including comments
     *
     * @param config The ConfigurationSection to write to
     * @param setting The setting to save as in the config
     * @param value The value to save
     */
    default void write(ConfigurationSection config, RoseSetting<T> setting, T value) {
        this.write(config, setting.getKey(), value, setting.getComments());
    }

    /**
     * Writes an object to a ConfigurationSection, including comments with a default value if this serializer has
     * a string key value.
     *
     * @param config The ConfigurationSection to write to
     * @param key The key path to save in the config
     * @param value The value to save
     * @param comments Comments to write above the setting in the config
     */
    default void writeWithDefault(ConfigurationSection config, String key, T value, String... comments) {
        String[] newComments = appendDefaultComment(this, value, comments);
        this.write(config, key, value, newComments);
    }

    /**
     * Writes an object to a ConfigurationSection, including comments with a default value if this serializer has
     * a string key value.
     *
     * @param config The ConfigurationSection to write to
     * @param setting The setting to save as in the config
     * @param value The value to save
     */
    default void writeWithDefault(ConfigurationSection config, RoseSetting<T> setting, T value) {
        this.writeWithDefault(config, setting.getKey(), value, setting.getComments());
    }

    default String getDefaultCommentText(T value) {
        if (!this.isStringKey())
            return null;

        String defaultValueStringKey = this.asStringKey(value);
        String defaultComment = "Default: ";
        if (RoseGardenUtils.containsConfigSpecialCharacters(defaultValueStringKey)) {
            defaultComment += '\'' + defaultValueStringKey + '\'';
        } else if (defaultValueStringKey.isEmpty()) {
            defaultComment += "''";
        } else {
            defaultComment += defaultValueStringKey;
        }
        return defaultComment;
    }

    static <T> String[] appendDefaultComment(SettingSerializer<T> serializer, T value, String[] comments) {
        String defaultComment = serializer.getDefaultCommentText(value);
        if (defaultComment == null)
            return comments;

        String[] newComments = new String[comments.length + 1];
        System.arraycopy(comments, 0, newComments, 0, comments.length);
        newComments[comments.length] = defaultComment;
        return newComments;
    }

    /**
     * Reads an object from a ConfigurationSection
     *
     * @param config The ConfigurationSection to read from
     * @param key The path to the value in the config
     * @return the value read
     */
    T read(ConfigurationSection config, String key);

    /**
     * Reads an object from a ConfigurationSection
     *
     * @param config The ConfigurationSection to read from
     * @param setting The setting to read from in the config
     * @return the value read
     */
    default T read(ConfigurationSection config, RoseSetting<T> setting) {
        return this.read(config, setting.getKey());
    }

    /**
     * Checks if the setting exists in the given config and has all properties written to
     *
     * @param config The ConfigurationSection to read from
     * @param key The path to the value in the config
     * @return true if the setting exists in the given config
     */
    default boolean readIsValid(ConfigurationSection config, String key) {
        return config.contains(key);
    }

    /**
     * Checks if the setting exists in the given config and has all properties written to
     *
     * @param config The ConfigurationSection to read from
     * @param setting The setting to read from in the config
     * @return true if the setting exists in the given config
     */
    default boolean readIsValid(ConfigurationSection config, RoseSetting<T> setting) {
        return this.readIsValid(config, setting.getKey());
    }

    boolean isStringKey();

    String asStringKey(T key);

    T fromStringKey(String key);

    Class<T> getType();

    PDCAdapter<T> pdc();

    interface PDCAdapter<T> {
        PDCDelegatingSettingSerializer<T> adapt(PersistentDataType<?, T> pdc);
    }

}
