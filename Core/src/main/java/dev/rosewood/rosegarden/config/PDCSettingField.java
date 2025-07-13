package dev.rosewood.rosegarden.config;

import java.util.function.Function;
import java.util.function.Supplier;

public class PDCSettingField<O, T> extends SettingField<O, T> {

    private final PDCSettingSerializer<T> settingSerializer;

    private PDCSettingField(String key,
                            PDCSettingSerializer<T> settingSerializer,
                            Function<O, T> getter,
                            Supplier<T> defaultValueSupplier,
                            boolean optional,
                            boolean flatten,
                            String... comments) {
        super(key, settingSerializer, getter, defaultValueSupplier, optional, flatten, comments);
        this.settingSerializer = settingSerializer;
    }

    @Override
    public PDCSettingSerializer<T> settingSerializer() {
        return this.settingSerializer;
    }

    public static <O, T> PDCSettingField<O, T> of(String key,
                                                  PDCSettingSerializer<T> settingSerializer,
                                                  Function<O, T> getter,
                                                  String... comments) {
        return new PDCSettingField<>(key, settingSerializer, getter, null, false, false, comments);
    }

    public static <O, T> PDCSettingField<O, T> ofOptional(String key,
                                                          PDCSettingSerializer<T> settingSerializer,
                                                          Function<O, T> getter,
                                                          Supplier<T> defaultValueSupplier,
                                                          String... comments) {
        return new PDCSettingField<>(key, settingSerializer, getter, defaultValueSupplier, true, false, comments);
    }

    public static <O, T> PDCSettingField<O, T> ofOptionalValue(String key,
                                                               PDCSettingSerializer<T> settingSerializer,
                                                               Function<O, T> getter,
                                                               T defaultValue,
                                                               String... comments) {
        return new PDCSettingField<>(key, settingSerializer, getter, () -> defaultValue, true, false, comments);
    }

    public static <O, T> PDCSettingField<O, T> ofFlattened(String key,
                                                           PDCSettingSerializer<T> settingSerializer,
                                                           Function<O, T> getter,
                                                           String... comments) {
        return new PDCSettingField<>(key, settingSerializer, getter, null, false, true, comments);
    }

    public static <O, T> PDCSettingField<O, T> ofFlattenedOptional(String key,
                                                                   PDCSettingSerializer<T> settingSerializer,
                                                                   Function<O, T> getter,
                                                                   Supplier<T> defaultValueSupplier,
                                                                   String... comments) {
        return new PDCSettingField<>(key, settingSerializer, getter, defaultValueSupplier, true, true, comments);
    }

    public static <O, T> PDCSettingField<O, T> ofFlattenedOptionalValue(String key,
                                                                        PDCSettingSerializer<T> settingSerializer,
                                                                        Function<O, T> getter,
                                                                        T defaultValue,
                                                                        String... comments) {
        return new PDCSettingField<>(key, settingSerializer, getter, () -> defaultValue, true, true, comments);
    }

}
