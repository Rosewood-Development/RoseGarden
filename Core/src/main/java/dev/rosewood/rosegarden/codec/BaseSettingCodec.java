package dev.rosewood.rosegarden.codec;

import java.util.function.Function;

public abstract class BaseSettingCodec<C, T> implements SettingCodec<C, T> {

    private final SettingType<T> type;
    private final Function<T, String> encodeStringFunction;
    private final Function<String, T> decodeStringFunction;

    public BaseSettingCodec(SettingType<T> type, Function<T, String> encodeStringFunction, Function<String, T> decodeStringFunction) {
        this.type = type;
        this.encodeStringFunction = encodeStringFunction;
        this.decodeStringFunction = decodeStringFunction;
    }

    public BaseSettingCodec(SettingType<T> type) {
        this(type, null, null);
    }

    public BaseSettingCodec(Class<T> type, Function<T, String> encodeStringFunction, Function<String, T> decodeStringFunction) {
        this(new SettingType<T>() { }, encodeStringFunction, decodeStringFunction);
    }

    public BaseSettingCodec(Class<T> type) {
        this(type, null, null);
    }

    @Override
    public SettingType<T> getSettingType() {
        return this.type;
    }

    public String encodeString(T value) {
        if (this.encodeStringFunction == null)
            throw new UnsupportedOperationException("encodeString not available for a setting codec of type " + this.getSettingType().getType().getTypeName() + ", check supportsStringEncoding() first");
        return this.encodeStringFunction.apply(value);
    }

    public T decodeString(String value) {
        if (this.decodeStringFunction == null)
            throw new UnsupportedOperationException("decodeString not available for a setting codec of type " + this.getSettingType().getType().getTypeName() + ", check supportsStringEncoding() first");
        return this.decodeStringFunction.apply(value);
    }

    public boolean supportsStringEncoding() {
        return this.encodeStringFunction != null && this.decodeStringFunction != null;
    }

}
