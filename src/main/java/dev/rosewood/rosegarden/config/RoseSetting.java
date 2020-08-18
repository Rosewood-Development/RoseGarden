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
     * @return the setting as a boolean
     */
    boolean getBoolean();

    /**
     * @return the setting as an int
     */
    int getInt();

    /**
     * @return the setting as a long
     */
    long getLong();

    /**
     * @return the setting as a double
     */
    double getDouble();

    /**
     * @return the setting as a float
     */
    float getFloat();

    /**
     * @return the setting as a String
     */
    String getString();

    /**
     * @return the setting as a string list
     */
    List<String> getStringList();

    /**
     * @return true if this setting is only a section and doesn't contain an actual value
     */
    boolean isSection();

    /**
     * Loads the value from the config and caches it if it isn't set yet
     */
    void loadValue();

    /**
     * Resets the cached value
     */
    void reset();

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

        if (fileConfiguration.get(key) == null) {
            List<String> comments = new ArrayList<>(Arrays.asList(this.getComments()));
            if (!(defaultValue instanceof List) && defaultValue != null) {
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

            if (defaultValue != null) {
                fileConfiguration.set(key, defaultValue, comments.toArray(new String[0]));
            } else {
                fileConfiguration.addComments(comments.toArray(new String[0]));
            }

            return true;
        }

        return false;
    }

}
