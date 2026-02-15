package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.SettingType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.configuration.ConfigurationSection;

public interface RoseSetting<T> {

    /**
     * Writes the setting to the given config.
     *
     * @param config the config to write to
     */
    void write(ConfigurationSection config);

    /**
     * Writes the setting and its default value to the given config.
     *
     * @param config the config to write to
     */
    void writeWithDefault(ConfigurationSection config);

    /**
     * Reads the setting from the given config and returns it.
     *
     * @param config the config to read from
     * @return the setting value
     */
    T read(ConfigurationSection config);

    /**
     * Checks if the setting exists in the given config and has all properties written to
     *
     * @param config the config to read from
     * @return true if the setting exists in the given config
     */
    boolean readIsValid(ConfigurationSection config);

    /**
     * @return the type of this setting
     */
    default SettingType<T> getSettingType() {
        return this.getSettingSerializer().getSettingType();
    }

    /**
     * @return the serializer for this setting
     */
    SettingSerializer<T> getSettingSerializer();

    /**
     * @return the key name of this setting
     */
    String getKey();

    /**
     * @return a new instance of the default value
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

    /**
     * @return true if this setting should not be written to an editable format
     */
    boolean isHidden();

    /**
     * Creates a new RoseSetting as a copy of this RoseSetting but with a different default value and optionally comments.
     * This should only be used for primitives or immutable types. Instances will be shared.
     * Comments can be removed by setting them to null, empty comments will cause them to be copied from the original.
     *
     * @param defaultValue the new default value
     * @return a copy of this RoseSetting with a different default value
     */
    RoseSetting<T> copy(T defaultValue, String... comments);

    /**
     * Creates a new RoseSetting as a copy of this RoseSetting but with a different default value.
     * This should be used for mutable types where the underlying value may be changed during the lifetime of
     * this setting.
     * Comments can be removed by setting them to null, empty comments will cause them to be copied from the original.
     *
     * @param defaultValueSupplier the new default value supplier
     * @return a copy of this RoseSetting with a different default value
     */
    RoseSetting<T> copy(Supplier<T> defaultValueSupplier, String... comments);

    static RoseSetting<Boolean> ofBoolean(String name, boolean defaultValue, String... comments) {
        return of(name, Boolean.class, () -> defaultValue, comments);
    }

    static RoseSetting<Integer> ofInteger(String name, int defaultValue, String... comments) {
        return of(name, Integer.class, () -> defaultValue, comments);
    }

    static RoseSetting<Long> ofLong(String name, long defaultValue, String... comments) {
        return of(name, Long.class, () -> defaultValue, comments);
    }

    static RoseSetting<Short> ofShort(String name, short defaultValue, String... comments) {
        return of(name, Short.class, () -> defaultValue, comments);
    }

    static RoseSetting<Byte> ofByte(String name, byte defaultValue, String... comments) {
        return of(name, Byte.class, () -> defaultValue, comments);
    }

    static RoseSetting<Double> ofDouble(String name, double defaultValue, String... comments) {
        return of(name, Double.class, () -> defaultValue, comments);
    }

    static RoseSetting<Float> ofFloat(String name, float defaultValue, String... comments) {
        return of(name, Float.class, () -> defaultValue, comments);
    }

    static RoseSetting<Character> ofCharacter(String name, char defaultValue, String... comments) {
        return of(name, Character.class, () -> defaultValue, comments);
    }

    static RoseSetting<String> ofString(String name, String defaultValue, String... comments) {
        return of(name, String.class, () -> defaultValue, comments);
    }

    static RoseSetting<List<String>> ofStringList(String name, List<String> defaultValue, String... comments) {
        return of(name, new SettingType<List<String>>() {}, () -> new ArrayList<>(defaultValue), comments);
    }

    static RoseSetting<ConfigurationSection> ofSection(String name, String... comments) {
        return ofValue(name, ConfigurationSection.class, null, comments);
    }

    /**
     * Creates a RoseSetting with a given default value.
     * This should only be used for primitives or immutable types. Instances will be shared.
     *
     * @param name The name of the setting
     * @param settingType The serializer for the setting
     * @param defaultValue The default value, do not use a mutable value
     * @param comments Comments describing the setting for writing to YAML
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> RoseSetting<T> ofValue(String name, Class<T> settingType, T defaultValue, String... comments) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), () -> defaultValue, false, comments != null ? comments : new String[0]);
    }

    static <T> RoseSetting<T> ofValue(String name, SettingType<T> settingType, T defaultValue, String... comments) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), () -> defaultValue, false, comments != null ? comments : new String[0]);
    }

    /**
     * Creates a RoseSetting with a given default value supplier.
     *
     * @param name The name of the setting
     * @param settingType The serializer for the setting
     * @param defaultValueSupplier The default value supplier to return a new default value instance
     * @param comments Comments describing the setting for writing to YAML
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> RoseSetting<T> of(String name, Class<T> settingType, Supplier<T> defaultValueSupplier, String... comments) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), defaultValueSupplier, false, comments != null ? comments : new String[0]);
    }

    static <T> RoseSetting<T> of(String name, SettingType<T> settingType, Supplier<T> defaultValueSupplier, String... comments) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), defaultValueSupplier, false, comments != null ? comments : new String[0]);
    }

    static <T extends Enum<T>> RoseSetting<T> ofEnum(String name, Class<T> settingType, T defaultValue, String... comments) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), () -> defaultValue, false, comments != null ? comments : new String[0]);
    }

    /**
     * Creates a RoseSetting with a given default value that is intended to be hidden.
     * This should only be used for primitives or immutable types. Instances will be shared.
     *
     * @param name The name of the setting
     * @param serializer The serializer for the setting
     * @param defaultValue The default value, do not use a mutable value
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> RoseSetting<T> ofHiddenValue(String name, Class<T> serializer, T defaultValue) {
        return new BasicRoseSetting<>(serializer, name.toLowerCase(), () -> defaultValue, true);
    }

    static <T> RoseSetting<T> ofHiddenValue(String name, SettingType<T> serializer, T defaultValue) {
        return new BasicRoseSetting<>(serializer, name.toLowerCase(), () -> defaultValue, true);
    }

    /**
     * Creates a RoseSetting with a given default value supplier that is intended to be hidden.
     *
     * @param name The name of the setting
     * @param settingType The serializer for the setting
     * @param defaultValueSupplier The default value supplier to return a new default value instance
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> RoseSetting<T> ofHidden(String name, Class<T> settingType, Supplier<T> defaultValueSupplier) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), defaultValueSupplier, true);
    }

    static <T> RoseSetting<T> ofHidden(String name, SettingType<T> settingType, Supplier<T> defaultValueSupplier) {
        return new BasicRoseSetting<>(settingType, name.toLowerCase(), defaultValueSupplier, true);
    }

    static <T> RoseSetting<T> ofBacked(String name, RosePlugin backing, Class<T> settingType, Supplier<T> defaultValueSupplier, String... comments) {
        return new BackedRoseSetting<>(backing, settingType, name.toLowerCase(), defaultValueSupplier, comments);
    }

    static <T> RoseSetting<T> ofBacked(String name, RosePlugin backing, SettingType<T> settingType, Supplier<T> defaultValueSupplier, String... comments) {
        return new BackedRoseSetting<>(backing, settingType, name.toLowerCase(), defaultValueSupplier, comments);
    }

    static <T> RoseSetting<T> ofBackedValue(String name, RosePlugin backing, Class<T> settingType, T defaultValue, String... comments) {
        return new BackedRoseSetting<>(backing, settingType, name.toLowerCase(), () -> defaultValue, comments);
    }

    static <T> RoseSetting<T> ofBackedValue(String name, RosePlugin backing, SettingType<T> settingType, T defaultValue, String... comments) {
        return new BackedRoseSetting<>(backing, settingType, name.toLowerCase(), () -> defaultValue, comments);
    }

    static <T> RoseSetting<ConfigurationSection> ofBackedSection(String name, RosePlugin backing, String... comments) {
        return ofBackedValue(name, backing, ConfigurationSection.class, null, comments);
    }

}
