package dev.rosewood.rosegarden.config;

import java.util.function.Supplier;

/* package */ class RoseSettingImpl<T> implements RoseSetting<T> {

    private final String key;
    private final RoseSettingSerializer<T> serializer;
    private final Supplier<T> defaultValueSupplier;
    private final String[] comments;

    public RoseSettingImpl(String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        this(key, serializer, () -> defaultValue, comments);
    }

    public RoseSettingImpl(String key, RoseSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier, String... comments) {
        this.key = key;
        this.defaultValueSupplier = defaultValueSupplier;
        this.comments = comments != null ? comments : new String[0];
        this.serializer = serializer;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public RoseSettingSerializer<T> getSerializer() {
        return this.serializer;
    }

    @Override
    public T getDefaultValue() {
        return this.defaultValueSupplier.get();
    }

    @Override
    public String[] getComments() {
        return this.comments;
    }

}
