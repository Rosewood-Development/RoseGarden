package dev.rosewood.rosegarden.codec.pdc;

import dev.rosewood.rosegarden.codec.BaseSettingCodec;
import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.utils.KeyHelper;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PdcCodec<T> extends BaseSettingCodec<PersistentDataContainer, T> {

    private final PersistentDataType<?, T> dataType;

    public PdcCodec(SettingType<T> type, PersistentDataType<?, T> dataType) {
        super(type);
        this.dataType = dataType;
    }

    public PdcCodec(Class<T> type, PersistentDataType<?, T> dataType) {
        this(new SettingType<T>() { }, dataType);
    }

    @Override
    public void encode(PersistentDataContainer container, String key, T value, String... comments) {
        container.set(KeyHelper.get(key), this.dataType, value);
    }

    @Override
    public T decode(PersistentDataContainer container, String key) {
        return container.get(KeyHelper.get(key), this.dataType);
    }

    @Override
    public Class<PersistentDataContainer> getContainerType() {
        return PersistentDataContainer.class;
    }

    @Override
    public boolean isPresent(PersistentDataContainer container, String key) {
        return container.has(KeyHelper.get(key));
    }

    @Override
    public boolean isValid(PersistentDataContainer container, String key) {
        return this.isPresent(container, key);
    }

    public PersistentDataType<?, T> getDataType() {
        return this.dataType;
    }

}
