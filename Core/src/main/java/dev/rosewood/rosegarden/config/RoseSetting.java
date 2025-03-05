package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface RoseSetting<T> {

    /**
     * @return the configuration key of this setting
     */
    String getKey();

    /**
     * @return the serializer for reading/writing values to/from the config
     */
    RoseSettingSerializer<T> getSerializer();

    /**
     * @return the default value of this setting
     */
    T getDefaultValue();

    /**
     * @return the comments detailing this setting
     */
    String[] getComments();

    /**
     * @return the value of this setting from the backing {@link RoseConfig}
     * @throws UnsupportedOperationException if this setting is not backed by a config
     */
    default T get() {
        throw new UnsupportedOperationException("get() is not supported for this setting, missing backing config");
    }

    /**
     * @return the value of this setting from the given RoseConfig
     */
    default T get(RoseConfig config) {
        return config.get(this);
    }

    /**
     * @return true if this setting is backed by a config and {@link #get()} can be called without a {@link RoseConfig}.
     */
    default boolean isBacked() {
        return false;
    }

    default void writeDefault(CommentedConfigurationSection config, boolean writeDefaultValueComment) {
        if (!writeDefaultValueComment) {
            this.getSerializer().write(config, this, this.getDefaultValue());
            return;
        }

        T defaultValue = this.getDefaultValue();
        List<String> comments = new ArrayList<>(Arrays.asList(this.getComments()));
        if (defaultValue != null && !(defaultValue instanceof Collection)) {
            String defaultValueString = defaultValue.toString();
            String defaultComment = "Default: ";
            if (RoseGardenUtils.containsConfigSpecialCharacters(defaultValueString)) {
                defaultComment += "'" + defaultValueString + "'";
            } else if (defaultValueString.trim().isEmpty()) {
                defaultComment += "''";
            } else {
                defaultComment += defaultValueString;
            }
            comments.add(defaultComment);
        }

        String[] commentsArray = comments.toArray(new String[0]);
        this.getSerializer().write(config, this.getKey(), this.getDefaultValue(), commentsArray);
    }

    static <T> RoseSetting<T> of(String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        return of(key, serializer, (Supplier<T>) () -> defaultValue, comments);
    }

    static <T> RoseSetting<T> of(String key, RoseSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier, String... comments) {
        return new BasicRoseSetting<>(key, serializer, defaultValueSupplier, comments);
    }

    static RoseSetting<CommentedConfigurationSection> ofSection(String key, String... comments) {
        return new BasicRoseSetting<>(key, RoseSettingSerializers.SECTION, (CommentedConfigurationSection) null, comments);
    }

    static <T> RoseSetting<T> backed(RosePlugin rosePlugin, String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        return backed(rosePlugin, key, serializer, (Supplier<T>) () -> defaultValue, comments);
    }

    static <T> RoseSetting<T> backed(RosePlugin rosePlugin, String key, RoseSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier, String... comments) {
        return new BackedRoseSetting<>(rosePlugin, key, serializer, defaultValueSupplier, comments);
    }

    static RoseSetting<CommentedConfigurationSection> backedSection(RosePlugin rosePlugin, String key, String... comments) {
        return new BackedRoseSetting<>(rosePlugin, key, RoseSettingSerializers.SECTION, (CommentedConfigurationSection) null, comments);
    }

}
