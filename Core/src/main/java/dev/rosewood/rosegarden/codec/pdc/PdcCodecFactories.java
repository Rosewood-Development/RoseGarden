package dev.rosewood.rosegarden.codec.pdc;

import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.datatype.CustomPersistentDataType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class PdcCodecFactories {

    public static <T extends Enum<T>> PdcCodec<T> ofEnum(Class<T> enumClass) {
        return new PdcCodec<>(enumClass, CustomPersistentDataType.forEnum(enumClass));
    }

    public static <T extends Keyed> PdcCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return new PdcCodec<>(keyedClass, CustomPersistentDataType.forKeyed(keyedClass, valueOfFunction));
    }

    public static <T> PdcCodec<T[]> ofArray(PdcCodec<T> elementCodec) {
        return new PdcCodec<>(new SettingType<T[]>() { }, CustomPersistentDataType.forArray(elementCodec.getDataType()));
    }

    public static <T> PdcCodec<List<T>> ofList(PdcCodec<T> elementCodec) {
        return new PdcCodec<>(new SettingType<List<T>>() { }, CustomPersistentDataType.forList(elementCodec.getDataType()));
    }

    public static <K, V> PdcCodec<Map<K, V>> ofMap(PdcCodec<K> keyElementCodec, PdcCodec<V> valueElementCodec) {
        return new PdcCodec<>(new SettingType<Map<K, V>>() { }, CustomPersistentDataType.forMap(keyElementCodec.getDataType(), valueElementCodec.getDataType()));
    }

    public static <T, M> PdcCodec<T> ofFieldMapped(Class<T> type, String fieldKey, PdcCodec<M> fieldSerializer, Map<M, PdcCodec<? extends T>> mapper) {
        return new PdcCodec<T>(type, null) {
            @Override
            public void encode(PersistentDataContainer container, String key, T value, String... comments) {
                PdcCodec<T> codec = this.mapField(fieldSerializer.decode(container, fieldKey));
                if (codec != null)
                    codec.encode(container, key, value, comments);
            }

            @Override
            public T decode(PersistentDataContainer container, String key) {
                PdcCodec<T> codec = this.mapField(fieldSerializer.decode(container, fieldKey));
                return codec != null ? codec.decode(container, key) : null;
            }

            @Override
            public boolean isValid(PersistentDataContainer container, String key) {
                PdcCodec<T> codec = this.mapField(fieldSerializer.decode(container, fieldKey));
                return codec != null && codec.isValid(container, key);
            }

            @Override
            public PersistentDataType<?, T> getDataType() {
                throw new UnsupportedOperationException("Cannot get the data type of a field mapped pdc codec");
            }

            @SuppressWarnings("unchecked") // always maps to subtypes since it extends T, catching just in case
            private PdcCodec<T> mapField(M value) {
                if (value == null)
                    return null;
                try {
                    PdcCodec<? extends T> codec = mapper.get(value);
                    return (PdcCodec<T>) codec;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

}
