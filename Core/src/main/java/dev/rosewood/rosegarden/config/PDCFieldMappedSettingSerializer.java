package dev.rosewood.rosegarden.config;

import java.util.Collections;
import java.util.Map;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCFieldMappedSettingSerializer<T, M> extends FieldMappedSettingSerializer<T, M> implements PDCSettingSerializer<T> {

    private final PDCSettingSerializer<M> fieldSerializer;
    private final Map<M, PDCSettingSerializer<? extends T>> mapper;

    public PDCFieldMappedSettingSerializer(Class<T> type, String fieldKey, PDCSettingSerializer<M> fieldSerializer, Map<M, PDCSettingSerializer<? extends T>> mapper) {
        super(type, fieldKey, fieldSerializer, Collections.unmodifiableMap(mapper));
        this.fieldSerializer = fieldSerializer;
        this.mapper = mapper;
    }

    @Override
    public void write(PersistentDataContainer container, String key, T value) {
        PDCSettingSerializer<T> serializer = this.mapField(this.fieldSerializer.read(container, this.fieldKey));
        if (serializer != null)
            serializer.write(container, key, value);
    }

    @Override
    public T read(PersistentDataContainer container, String key) {
        PDCSettingSerializer<T> serializer = this.mapField(this.fieldSerializer.read(container, this.fieldKey));
        return serializer != null ? serializer.read(container, key) : null;
    }

    @SuppressWarnings("unchecked") // always maps to subtypes since it extends T, catching just in case
    @Override
    protected PDCSettingSerializer<T> mapField(M value) {
        if (value == null)
            return null;
        try {
            SettingSerializer<? extends T> serializer = this.mapper.get(value);
            return (PDCSettingSerializer<T>) serializer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PersistentDataType<?, T> getPDCType() {
        throw new UnsupportedOperationException("No strict PDC type");
    }

}
