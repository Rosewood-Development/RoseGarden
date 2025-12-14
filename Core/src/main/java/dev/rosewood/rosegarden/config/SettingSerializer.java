package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.CodecType;
import dev.rosewood.rosegarden.codec.SettingCodec;
import dev.rosewood.rosegarden.codec.SettingCodecRegistry;
import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
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
        this(new SettingType<T>() {});
    }

    /**
     * Writes an object to a container.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The key path to save in the config
     * @param value The value to save
     * @param comments Comments to write above the setting in the config
     */
    public <C> void write(CodecType<C> codecType, C container, String key, T value, String... comments) {
        SettingCodecRegistry codecRegistry = RosePlugin.instance().getCodecRegistry();
        SettingCodec<C, T> codec = codecRegistry.get(codecType, this.type);
        codec.encode(container, key, value, comments);
    }

    /**
     * Writes an object to a container.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param setting The setting to save as in the config
     * @param value The value to save
     */
    public <C> void write(CodecType<C> codecType, C container, RoseSetting<T> setting, T value) {
        this.write(codecType, container, setting.getKey(), value, setting.getComments());
    }

    /**
     * Writes an object to a container with a default value comment, if applicable.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The key path to save in the config
     * @param value The value to save
     * @param comments Comments to write above the setting in the config
     */
    public <C> void writeWithDefault(CodecType<C> codecType, C container, String key, T value, String... comments) {
        SettingCodecRegistry codecRegistry = RosePlugin.instance().getCodecRegistry();
        SettingCodec<C, T> codec = codecRegistry.get(codecType, this.type);
        String[] newComments = this.appendDefaultComment(codec, value, comments);
        this.write(codecType, container, key, value, newComments);
    }

    /**
     * Writes an object to a container with a default value comment, if applicable.
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param setting The setting to save as
     * @param value The value to save
     */
    public <C> void writeWithDefault(CodecType<C> codecType, C container, RoseSetting<T> setting, T value) {
        this.writeWithDefault(codecType, container, setting.getKey(), value, setting.getComments());
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
     * Reads an object from a ConfigurationSection
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param setting The setting to read from in the config
     * @return the value read
     */
    public <C> T read(CodecType<C> codecType, C container,RoseSetting<T> setting) {
        return this.read(codecType, container, setting.getKey());
    }

    /**
     * Checks if the setting exists in the given config and has all properties written to
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param key The path to the value in the config
     * @return true if the setting exists in the given config
     */
    public <C> boolean readIsValid(CodecType<C> codecType, C container, String key) {
        SettingCodecRegistry codecRegistry = RosePlugin.instance().getCodecRegistry();
        SettingCodec<C, T> codec = codecRegistry.get(codecType, this.type);
        return codec.isValid(container, key);
    }

    /**
     * Checks if the setting exists in the given config and has all properties written to
     *
     * @param codecType The codec type to write with
     * @param container The container to write to
     * @param setting The setting to read from in the config
     * @return true if the setting exists in the given config
     */
    public <C> boolean readIsValid(CodecType<C> codecType, C container, RoseSetting<T> setting) {
        return this.readIsValid(codecType, container, setting.getKey());
    }

    public SettingType<T> getType() {
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

    private String[] appendDefaultComment(SettingCodec<?, T> codec, T value, String[] comments) {
        String defaultComment = this.getDefaultCommentText(codec, value);
        if (defaultComment == null)
            return comments;

        String[] newComments = new String[comments.length + 1];
        System.arraycopy(comments, 0, newComments, 0, comments.length);
        newComments[comments.length] = defaultComment;
        return newComments;
    }

    private String getDefaultCommentText(SettingCodec<?, T> codec, T value) {
        if (!codec.supportsStringEncoding())
            return null;

        String encoded = codec.encodeString(value);
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

}
