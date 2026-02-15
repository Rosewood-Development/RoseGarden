package dev.rosewood.rosegarden.codec;


import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.util.function.Function;

public interface SettingCodec<C, T> {

    SettingType<T> getSettingType();

    Class<C> getContainerType();

    void encode(C container, String key, T value, boolean appendDefault, String... comments);

    default void encode(C container, String key, T value, String... comments) {
        this.encode(container, key, value, false, comments);
    }

    T decode(C container, String key);

    default boolean verify(C container, String key) {
        return this.decode(container, key) != null;
    }

    default String encodeString(T value) {
        throw new UnsupportedOperationException("encodeString not available for a setting codec of type " + this.getSettingType().getType().getTypeName() + ", check supportsStringEncoding() first");
    }

    default T decodeString(String value) {
        throw new UnsupportedOperationException("decodeString not available for a setting codec of type " + this.getSettingType().getType().getTypeName() + ", check supportsStringEncoding() first");
    }

    default boolean supportsStringEncoding() {
        return false;
    }

    default String createDefaultComment(T value) {
        if (!this.supportsStringEncoding())
            return null;

        String encoded = this.encodeString(value);
        if (encoded.length() > 80)
            return null;

        String defaultComment = "Default: ";
        if (!encoded.startsWith("[") && !encoded.startsWith("{") && RoseGardenUtils.containsConfigSpecialCharacters(encoded)) {
            defaultComment += '\'' + encoded + '\'';
        } else if (encoded.isEmpty()) {
            defaultComment += "''";
        } else {
            defaultComment += encoded;
        }
        return defaultComment;
    }

    static <C, P, T> SettingCodec<C, T> ofMapped(Class<T> type, SettingCodec<C, P> primitiveCodec, Function<T, P> primitiveEncodeFunction, Function<P, T> primitiveDecodeFunction) {
        return new BaseSettingCodec<C, T>(type) {
            @Override
            public Class<C> getContainerType() {
                return primitiveCodec.getContainerType();
            }

            @Override
            public void encode(C container, String key, T value, boolean appendDefault, String... comments) {
                P primitive = primitiveEncodeFunction.apply(value);
                if (primitive == null)
                    return;
                primitiveCodec.encode(container, key, primitive, appendDefault, comments);
            }

            @Override
            public T decode(C container, String key) {
                P primitive = primitiveCodec.decode(container, key);
                if (primitive == null)
                    return null;
                return primitiveDecodeFunction.apply(primitive);
            }
        };
    }

}
