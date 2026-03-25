package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.CodecType;
import dev.rosewood.rosegarden.codec.SettingCodec;
import dev.rosewood.rosegarden.codec.SettingCodecRegistry;
import dev.rosewood.rosegarden.codec.SettingType;
import java.util.Objects;

/**
 * Allows reading and writing an object type using registered codecs.
 * @param <T> The serialized type
 */
public final class SettingSerializer<T> {

    private final SettingType<T> type;

    public SettingSerializer(SettingType<T> type) {
        this.type = type;
    }

    public SettingSerializer(Class<T> type) {
        this(new SettingType<T>(type) { });
    }

    /**
     * Writes an object to a container.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The key path to save in the config
     * @param value The value to save
     * @param appendDefault if true, appends a default value comment
     * @param comments Comments to write above the setting in the config
     */
    public <C> void write(CodecType<C> codecType, C container, String key, T value, boolean appendDefault, String... comments) {
        SettingCodecRegistry codecRegistry = RosePlugin.instance().getCodecRegistry();
        SettingCodec<C, T> codec = codecRegistry.get(codecType, this.type);
        codec.encode(container, key, value, appendDefault, comments);
    }

    /**
     * Writes an object to a container.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The key path to save in the config
     * @param value The value to save
     */
    public <C> void write(CodecType<C> codecType, C container, String key, T value) {
        this.write(codecType, container, key, value, false);
    }

    /**
     * Reads an object from a container.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The path to the value in the config
     * @return the value read
     */
    public <C> T read(CodecType<C> codecType, C container, String key) {
        SettingCodecRegistry codecRegistry = RosePlugin.instance().getCodecRegistry();
        SettingCodec<C, T> codec = codecRegistry.get(codecType, this.type);
        return codec.decode(container, key);
    }

    /**
     * Checks if the setting exists in the given config and has all properties written to
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The path to the value in the config
     * @return true if the setting exists in the given config
     */
    public <C> boolean isValid(CodecType<C> codecType, C container, String key) {
        SettingCodecRegistry codecRegistry = RosePlugin.instance().getCodecRegistry();
        SettingCodec<C, T> codec = codecRegistry.get(codecType, this.type);
        return codec.verify(container, key);
    }

    public SettingType<T> getSettingType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SettingSerializer)) return false;
        SettingSerializer<?> that = (SettingSerializer<?>) o;
        return Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type);
    }

}
