package dev.rosewood.rosegarden.codec.pdc;

import dev.rosewood.rosegarden.codec.SettingCodec;
import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.datatype.CustomPersistentDataType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class PdcCodecFactories {

    public static <T extends Enum<T>> PdcCodec<T> ofEnum(Class<T> enumClass) {
        return new PdcCodec<>(enumClass, CustomPersistentDataType.forEnum(enumClass));
    }

    public static <T extends Keyed> PdcCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return new PdcCodec<>(keyedClass, CustomPersistentDataType.forKeyed(keyedClass, valueOfFunction));
    }

    public static <T> PdcCodec<T[]> ofArray(SettingType<T[]> settingType, PdcCodec<T> elementCodec) {
        return new PdcCodec<>(settingType, CustomPersistentDataType.forArray(elementCodec.getDataType()));
    }

    public static <T> PdcCodec<List<T>> ofList(SettingType<List<T>> settingType, PdcCodec<T> elementCodec) {
        return new PdcCodec<>(settingType, CustomPersistentDataType.forList(elementCodec.getDataType()));
    }

    public static <K, V> PdcCodec<Map<K, V>> ofMap(SettingType<Map<K, V>> settingType, PdcCodec<K> keyElementCodec, PdcCodec<V> valueElementCodec) {
        return new PdcCodec<>(settingType, CustomPersistentDataType.forMap(keyElementCodec.getDataType(), valueElementCodec.getDataType()));
    }

    public static <T, M> SettingCodec<PersistentDataContainer, T> ofFieldMapped(Class<T> type, String fieldKey, SettingCodec<PersistentDataContainer, M> fieldCodec, Map<M, SettingCodec<PersistentDataContainer, ? extends T>> mapper) {
        return new PdcCodec<T>(type, null) {
            @Override
            public void encode(PersistentDataContainer container, String key, T value, String... comments) {
                M keyValue = this.decodeField(container, key, fieldKey);
                SettingCodec<PersistentDataContainer, T> codec = this.mapField(keyValue);
                if (codec != null)
                    codec.encode(container, key, value, comments);
            }

            @Override
            public T decode(PersistentDataContainer container, String key) {
                M keyValue = this.decodeField(container, key, fieldKey);
                SettingCodec<PersistentDataContainer, T> codec = this.mapField(keyValue);
                return codec != null ? codec.decode(container, key) : null;
            }

            @Override
            public boolean verify(PersistentDataContainer container, String key) {
                M keyValue = this.decodeField(container, key, fieldKey);
                SettingCodec<PersistentDataContainer, T> codec = this.mapField(keyValue);
                return codec != null && codec.verify(container, key);
            }

            @Override
            public PersistentDataType<?, T> getDataType() {
                throw new UnsupportedOperationException("Cannot get the data type of a field mapped pdc codec");
            }

            private M decodeField(PersistentDataContainer container, String key, String fieldKey) {
                String nestedKey = key + "." + fieldKey;
                M keyValue = fieldCodec.decode(container, nestedKey);
                if (keyValue != null)
                    return keyValue;
                if (!key.contains("."))
                    return null;
                String subKey = key.substring(0, key.lastIndexOf(".")) + "." + fieldKey;
                return fieldCodec.decode(container, subKey);
            }

            @SuppressWarnings("unchecked") // always maps to subtypes since it extends T, catching just in case
            private SettingCodec<PersistentDataContainer, T> mapField(M value) {
                if (value == null)
                    return null;
                try {
                    SettingCodec<PersistentDataContainer, ? extends T> codec = mapper.get(value);
                    return (SettingCodec<PersistentDataContainer, T>) codec;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

}
