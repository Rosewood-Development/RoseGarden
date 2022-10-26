package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface RoseSetting {

    /**
     * @return the configuration key of this setting
     */
    String getKey();

    /**
     * @return the unparsed default value of this setting
     */
    Object getDefaultValue();

    /**
     * @return the comments detailing this setting
     */
    String[] getComments();

    /**
     * @return the cached value of this setting, or null if not loaded
     */
    Object getCachedValue();

    /**
     * @return the base CommentedFileConfiguration of the config.yml
     */
    CommentedFileConfiguration getBaseConfig();

    /**
     * Sets the cached value for this setting
     *
     * @param value The value
     */
    void setCachedValue(Object value);

    /**
     * @return the setting as a boolean
     */
    default boolean getBoolean() {
        this.loadValue();
        return (boolean) this.getCachedValue();
    }

    /**
     * @return the setting as an int
     */
    default int getInt() {
        this.loadValue();
        return (int) RoseGardenUtils.getNumber(this.getCachedValue());
    }

    /**
     * @return the setting as a long
     */
    default long getLong() {
        this.loadValue();
        return (long) RoseGardenUtils.getNumber(this.getCachedValue());
    }

    /**
     * @return the setting as a double
     */
    default double getDouble() {
        this.loadValue();
        return RoseGardenUtils.getNumber(this.getCachedValue());
    }

    /**
     * @return the setting as a float
     */
    default float getFloat() {
        this.loadValue();
        return (float) RoseGardenUtils.getNumber(this.getCachedValue());
    }

    /**
     * @return the setting as a String
     */
    default String getString() {
        this.loadValue();
        return String.valueOf(this.getCachedValue());
    }

    /**
     * @return the setting as a string list
     */
    @SuppressWarnings("unchecked")
    default List<String> getStringList() {
        this.loadValue();
        return (List<String>) this.getCachedValue();
    }

    /**
     * @return the setting as a CommentedConfigurationSection
     */
    default CommentedConfigurationSection getSection() {
        this.loadValue();
        return (CommentedConfigurationSection) this.getCachedValue();
    }

    /**
     * Loads the value from the config and caches it if it isn't set yet
     */
    default void loadValue() {
        if (this.getCachedValue() != null)
            return;

        String key = this.getKey();

        CommentedFileConfiguration config = this.getBaseConfig();
        if (config.isConfigurationSection(key)) {
            this.setCachedValue(config.getConfigurationSection(key));
        } else {
            this.setCachedValue(config.get(key));
        }
    }

    /**
     * Resets the cached value
     */
    default void reset() {
        this.setCachedValue(null);
    }

    /**
     * Sets the value in the given CommentedFileConfiguration if the setting does not already exist
     *
     * @param fileConfiguration The CommentedFileConfiguration to write to
     * @return true if the setting was written, otherwise false
     */
    default boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
        this.loadValue();

        String key = this.getKey();
        Object defaultValue = this.getDefaultValue();

        if (this.getCachedValue() == null) {
            List<String> comments = new ArrayList<>(Arrays.asList(this.getComments()));
            if (!(defaultValue instanceof RoseSettingValue || defaultValue instanceof RoseSettingSection) && !(defaultValue instanceof List) && defaultValue != null) {
                String defaultComment = "Default: ";
                if (defaultValue instanceof String) {
                    if (RoseGardenUtils.containsConfigSpecialCharacters((String) defaultValue)) {
                        defaultComment += "'" + defaultValue + "'";
                    } else {
                        defaultComment += defaultValue;
                    }
                } else {
                    defaultComment += defaultValue;
                }
                comments.add(defaultComment);
            }

            String[] commentsArray = comments.toArray(new String[0]);
            if (defaultValue != null) {
                RoseSettingValue value = new RoseSettingValue(key, defaultValue, commentsArray);
                RoseGardenUtils.recursivelyWriteRoseSettingValues(fileConfiguration, value);
            } else {
                fileConfiguration.addPathedComments(key, commentsArray);
            }

            return true;
        }

        return false;
    }

}
