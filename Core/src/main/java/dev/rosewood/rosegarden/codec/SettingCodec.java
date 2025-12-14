package dev.rosewood.rosegarden.codec;


import java.util.function.Function;

public interface SettingCodec<C, T> {

    SettingType<T> getSettingType();

    Class<C> getContainerType();

    void encode(C container, String key, T value, String... comments);

    T decode(C container, String key);

    boolean isPresent(C container, String key);

    boolean isValid(C container, String key);

    default String encodeString(T value) {
        throw new UnsupportedOperationException("encodeString not available for a setting codec of type " + this.getSettingType().getType().getTypeName() + ", check supportsStringEncoding() first");
    }

    default T decodeString(String value) {
        throw new UnsupportedOperationException("decodeString not available for a setting codec of type " + this.getSettingType().getType().getTypeName() + ", check supportsStringEncoding() first");
    }

    default boolean supportsStringEncoding() {
        return false;
    }

    static <C, P, T> SettingCodec<C, T> ofMapped(Class<T> type, SettingCodec<C, P> primitiveCodec, Function<T, P> primitiveEncodeFunction, Function<P, T> primitiveDecodeFunction) {
        return new BaseSettingCodec<C, T>(type) {
            @Override
            public Class<C> getContainerType() {
                return primitiveCodec.getContainerType();
            }

            @Override
            public void encode(C container, String key, T value, String... comments) {
                P primitive = primitiveEncodeFunction.apply(value);
                if (primitive == null)
                    return;
                primitiveCodec.encode(container, key, primitive, comments);
            }

            @Override
            public T decode(C container, String key) {
                P primitive = primitiveCodec.decode(container, key);
                if (primitive == null)
                    return null;
                return primitiveDecodeFunction.apply(primitive);
            }

            @Override
            public boolean isPresent(C container, String key) {
                return primitiveCodec.isPresent(container, key);
            }

            @Override
            public boolean isValid(C container, String key) {
                return primitiveCodec.isValid(container, key);
            }
        };
    }

}
