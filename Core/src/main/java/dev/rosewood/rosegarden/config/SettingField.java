package dev.rosewood.rosegarden.config;

import java.util.function.Function;
import java.util.function.Supplier;

public class SettingField<O, T> {

    private final String key;
    private final SettingSerializer<T> settingSerializer;
    private final Function<O, T> getter;
    private final Supplier<T> defaultValueSupplier;
    private final boolean optional;
    private final boolean flatten;
    private final String[] comments;

    SettingField(String key,
                 SettingSerializer<T> settingSerializer,
                 Function<O, T> getter,
                 Supplier<T> defaultValueSupplier,
                 boolean optional,
                 boolean flatten,
                 String... comments) {
        this.key = key;
        this.settingSerializer = settingSerializer;
        this.getter = getter;
        this.defaultValueSupplier = defaultValueSupplier;
        this.optional = optional;
        this.flatten = flatten;
        this.comments = comments;
    }

    public String key() {
        return this.key;
    }

    public SettingSerializer<T> settingSerializer() {
        return this.settingSerializer;
    }

    public Function<O, T> getter() {
        return this.getter;
    }

    public T defaultValue() {
        if (this.defaultValueSupplier == null)
            return null;
        return this.defaultValueSupplier.get();
    }

    public boolean optional() {
        return this.optional;
    }

    public String[] comments() {
        return this.comments;
    }

    public boolean flatten() {
        return this.flatten;
    }

    public static <O, T> SettingField<O, T> of(String key,
                                               SettingSerializer<T> settingSerializer,
                                               Function<O, T> getter,
                                               String... comments) {
        return new SettingField<>(key, settingSerializer, getter, null, false, false, comments);
    }

    public static <O, T> SettingField<O, T> ofOptional(String key,
                                                       SettingSerializer<T> settingSerializer,
                                                       Function<O, T> getter,
                                                       Supplier<T> defaultValueSupplier,
                                                       String... comments) {
        return new SettingField<>(key, settingSerializer, getter, defaultValueSupplier, true, false, comments);
    }

    public static <O, T> SettingField<O, T> ofOptionalValue(String key,
                                                            SettingSerializer<T> settingSerializer,
                                                            Function<O, T> getter,
                                                            T defaultValue,
                                                            String... comments) {
        return new SettingField<>(key, settingSerializer, getter, () -> defaultValue, true, false, comments);
    }

    public static <O, T> SettingField<O, T> ofFlattened(String key,
                                                        SettingSerializer<T> settingSerializer,
                                                        Function<O, T> getter,
                                                        String... comments) {
        return new SettingField<>(key, settingSerializer, getter, null, false, true, comments);
    }

    public static <O, T> SettingField<O, T> ofFlattenedOptional(String key,
                                                                SettingSerializer<T> settingSerializer,
                                                                Function<O, T> getter,
                                                                Supplier<T> defaultValueSupplier,
                                                                String... comments) {
        return new SettingField<>(key, settingSerializer, getter, defaultValueSupplier, true, true, comments);
    }

    public static <O, T> SettingField<O, T> ofFlattenedOptionalValue(String key,
                                                                     SettingSerializer<T> settingSerializer,
                                                                     Function<O, T> getter,
                                                                     T defaultValue,
                                                                     String... comments) {
        return new SettingField<>(key, settingSerializer, getter, () -> defaultValue, true, true, comments);
    }

}
