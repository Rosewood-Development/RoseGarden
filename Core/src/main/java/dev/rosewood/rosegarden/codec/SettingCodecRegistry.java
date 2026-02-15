package dev.rosewood.rosegarden.codec;

import java.util.Collections;

public interface SettingCodecRegistry {

    /**
     * Registers codecs for a codec type.
     *
     * @param codecType The codec type
     * @param codecs The codecs to register
     * @param <C> The container type
     * @throws IllegalArgumentException if a codec with the same codec type and object type is already registered
     */
    <C> void register(CodecType<C> codecType, Iterable<SettingCodec<C, ?>> codecs);

    /**
     * Registers one codec for a codec type.
     *
     * @param codecType The codec type
     * @param codec The codec to register
     * @param <C> The container type
     * @throws IllegalArgumentException if a codec with the same codec type and object type is already registered
     */
    default <C> void register(CodecType<C> codecType, SettingCodec<C, ?> codec) {
        this.register(codecType, Collections.singleton(codec));
    }

    /**
     * Gets a codec from the registry.
     *
     * @param codecType The codec type
     * @param type The stored object type
     * @return the registered codec for the given codec type and object type
     * @param <C> The container type
     * @param <T> The type of object being stored
     * @throws IllegalArgumentException if no codec is registered for the given codec type and object type
     */
    <C, T> SettingCodec<C, T> get(CodecType<C> codecType, SettingType<T> type);

    /**
     * Checks if a codec is registered with the given codec type and object type.
     *
     * @param codecType The codec type
     * @param type The stored object type
     * @return true if the codec is in the registry, otherwise false
     * @param <C> The container type
     * @param <T> The type of object being stored
     */
    <C, T> boolean has(CodecType<C> codecType, SettingType<T> type);

}
