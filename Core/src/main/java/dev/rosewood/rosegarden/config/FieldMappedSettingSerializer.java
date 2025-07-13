package dev.rosewood.rosegarden.config;

import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;

public class FieldMappedSettingSerializer<T, M> extends BaseSettingSerializer<T> {

    protected final String fieldKey;
    private final SettingSerializer<M> fieldSerializer;
    private final Map<M, SettingSerializer<? extends T>> mapper;

    public FieldMappedSettingSerializer(Class<T> type, String fieldKey, SettingSerializer<M> fieldSerializer, Map<M, SettingSerializer<? extends T>> mapper) {
        super(type);
        this.fieldKey = fieldKey;
        this.fieldSerializer = fieldSerializer;
        this.mapper = mapper;
    }

    @Override
    public void write(ConfigurationSection config, String key, T value, String... comments) {
        SettingSerializer<T> serializer = this.mapField(this.fieldSerializer.read(config, this.fieldKey));
        if (serializer != null)
            serializer.write(config, key, value, comments);
    }

    @Override
    public void writeWithDefault(ConfigurationSection config, String key, T value, String... comments) {
        SettingSerializer<T> serializer = this.mapField(this.fieldSerializer.read(config, this.fieldKey));
        if (serializer != null)
            serializer.writeWithDefault(config, key, value, comments);
    }

    @Override
    public T read(ConfigurationSection config, String key) {
        SettingSerializer<T> serializer = this.mapField(this.fieldSerializer.read(config, this.fieldKey));
        return serializer != null ? serializer.read(config, key) : null;
    }

    @Override
    public boolean readIsValid(ConfigurationSection config, String key) {
        SettingSerializer<T> serializer = this.mapField(this.fieldSerializer.read(config, this.fieldKey));
        return serializer != null && serializer.readIsValid(config, key);
    }

    @SuppressWarnings("unchecked") // always maps to subtypes since it extends T, catching just in case
    protected SettingSerializer<T> mapField(M value) {
        if (value == null)
            return null;
        try {
            SettingSerializer<? extends T> serializer = this.mapper.get(value);
            return (SettingSerializer<T>) serializer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
