package dev.rosewood.rosegarden.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface PDCRoseSetting<T> extends RoseSetting<T> {

    @Override
    PDCSettingSerializer<T> getSerializer();

    @Override
    PDCRoseSetting<T> copy(T defaultValue, String... comments);

    @Override
    PDCRoseSetting<T> copy(Supplier<T> defaultValueSupplier, String... comments);

    static PDCRoseSetting<Boolean> ofBoolean(String name, boolean defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.BOOLEAN, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Integer> ofInteger(String name, int defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.INTEGER, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Long> ofLong(String name, long defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.LONG, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Short> ofShort(String name, short defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.SHORT, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Byte> ofByte(String name, byte defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.BYTE, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Double> ofDouble(String name, double defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.DOUBLE, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Float> ofFloat(String name, float defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.FLOAT, () -> defaultValue, comments);
    }

    static PDCRoseSetting<Character> ofCharacter(String name, char defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.CHAR, () -> defaultValue, comments);
    }

    static PDCRoseSetting<String> ofString(String name, String defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.STRING, () -> defaultValue, comments);
    }

    static PDCRoseSetting<List<String>> ofStringList(String name, List<String> defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.STRING_LIST, () -> new ArrayList<>(defaultValue), comments);
    }

    static <T extends Enum<T>> PDCRoseSetting<T> ofEnum(String name, Class<T> enumClass, T defaultValue, String... comments) {
        return of(name, PDCSettingSerializers.ofEnum(enumClass), () -> defaultValue, comments);
    }

    /**
     * Creates a RoseSetting with a given default value.
     * This should only be used for primitives or immutable types. Instances will be shared.
     *
     * @param name The name of the setting
     * @param serializer The serializer for the setting
     * @param defaultValue The default value, do not use a mutable value
     * @param comments Comments describing the setting for writing to YAML
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> PDCRoseSetting<T> ofValue(String name, PDCSettingSerializer<T> serializer, T defaultValue, String... comments) {
        return new PDCBasicRoseSetting<>(serializer, name.toLowerCase(), () -> defaultValue, false, comments != null ? comments : new String[0]);
    }

    /**
     * Creates a RoseSetting with a given default value supplier.
     *
     * @param name The name of the setting
     * @param serializer The serializer for the setting
     * @param defaultValueSupplier The default value supplier to return a new default value instance
     * @param comments Comments describing the setting for writing to YAML
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> PDCRoseSetting<T> of(String name, PDCSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier, String... comments) {
        return new PDCBasicRoseSetting<>(serializer, name.toLowerCase(), defaultValueSupplier, false, comments != null ? comments : new String[0]);
    }

    /**
     * Creates a RoseSetting with a given default value.
     * This should only be used for primitives or immutable types. Instances will be shared.
     * Will not be written to YAML, will be written to PDC.
     *
     * @param name The name of the setting
     * @param serializer The serializer for the setting
     * @param defaultValue The default value, do not use a mutable value
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> PDCRoseSetting<T> ofHiddenValue(String name, PDCSettingSerializer<T> serializer, T defaultValue) {
        return new PDCBasicRoseSetting<>(serializer, name.toLowerCase(), () -> defaultValue, true);
    }

    /**
     * Creates a RoseSetting with a given default value supplier.
     * Will not be written to YAML, will be written to PDC.
     *
     * @param name The name of the setting
     * @param serializer The serializer for the setting
     * @param defaultValueSupplier The default value supplier to return a new default value instance
     * @return a new RoseSetting
     * @param <T> the type of value this setting is for
     */
    static <T> PDCRoseSetting<T> ofHidden(String name, PDCSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier) {
        return new PDCBasicRoseSetting<>(serializer, name.toLowerCase(), defaultValueSupplier, true);
    }

}
