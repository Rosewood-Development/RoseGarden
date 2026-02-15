package dev.rosewood.rosegarden.codec.record;

import dev.rosewood.rosegarden.codec.SettingCodec;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RecordField<C, O, T> {

    private final String key;
    private final SettingCodec<C, T> settingCodec;
    private final Function<O, T> getter;
    private final Supplier<T> defaultValueSupplier;
    private final boolean optional;
    private final boolean flatten;
    private final String[] comments;

    private RecordField(String key,
                        SettingCodec<C, T> settingCodec,
                        Function<O, T> getter,
                        Supplier<T> defaultValueSupplier,
                        boolean optional,
                        boolean flatten,
                        String... comments) {
        this.key = key;
        this.settingCodec = settingCodec;
        this.getter = getter;
        this.defaultValueSupplier = defaultValueSupplier;
        this.optional = optional;
        this.flatten = flatten;
        this.comments = comments;
    }

    public String key() {
        return this.key;
    }

    public SettingCodec<C, T> settingCodec() {
        return this.settingCodec;
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

    public static <C, O, T> RecordField<C, O, T> of(String key,
                                                    SettingCodec<C, T> settingSerializer,
                                                    Function<O, T> getter,
                                                    String... comments) {
        return new RecordField<>(key, settingSerializer, getter, null, false, false, comments);
    }

    public static <C, O, T> RecordField<C, O, T> ofOptional(String key,
                                                            SettingCodec<C, T> settingSerializer,
                                                            Function<O, T> getter,
                                                            Supplier<T> defaultValueSupplier,
                                                            String... comments) {
        return new RecordField<>(key, settingSerializer, getter, defaultValueSupplier, true, false, comments);
    }

    public static <C, O, T> RecordField<C, O, T> ofOptionalValue(String key,
                                                                 SettingCodec<C, T> settingCodec,
                                                                 Function<O, T> getter,
                                                                 T defaultValue,
                                                                 String... comments) {
        return new RecordField<>(key, settingCodec, getter, () -> defaultValue, true, false, comments);
    }

    public static <C, O, T> RecordField<C, O, T> ofFlattened(String key,
                                                             SettingCodec<C, T> settingCodec,
                                                             Function<O, T> getter,
                                                             String... comments) {
        return new RecordField<>(key, settingCodec, getter, null, false, true, comments);
    }

    public static <C, O, T> RecordField<C, O, T> ofFlattenedOptional(String key,
                                                                     SettingCodec<C, T> settingCodec,
                                                                     Function<O, T> getter,
                                                                     Supplier<T> defaultValueSupplier,
                                                                     String... comments) {
        return new RecordField<>(key, settingCodec, getter, defaultValueSupplier, true, true, comments);
    }

    public static <C, O, T> RecordField<C, O, T> ofFlattenedOptionalValue(String key,
                                                                          SettingCodec<C, T> settingCodec,
                                                                          Function<O, T> getter,
                                                                          T defaultValue,
                                                                          String... comments) {
        return new RecordField<>(key, settingCodec, getter, () -> defaultValue, true, true, comments);
    }

}
