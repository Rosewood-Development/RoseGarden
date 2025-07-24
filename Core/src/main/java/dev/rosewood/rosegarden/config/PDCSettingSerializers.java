package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.datatype.CustomPersistentDataType;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public final class PDCSettingSerializers {

    private PDCSettingSerializers() { }

    //region Primitive Serializers
    public static final PDCSettingSerializer<Boolean> BOOLEAN = SettingSerializers.BOOLEAN.pdc().adapt(PersistentDataType.BOOLEAN);
    public static final PDCSettingSerializer<Integer> INTEGER = SettingSerializers.INTEGER.pdc().adapt(PersistentDataType.INTEGER);
    public static final PDCSettingSerializer<Long> LONG = SettingSerializers.LONG.pdc().adapt(PersistentDataType.LONG);
    public static final PDCSettingSerializer<Short> SHORT = SettingSerializers.SHORT.pdc().adapt(PersistentDataType.SHORT);
    public static final PDCSettingSerializer<Byte> BYTE = SettingSerializers.BYTE.pdc().adapt(PersistentDataType.BYTE);
    public static final PDCSettingSerializer<Double> DOUBLE = SettingSerializers.DOUBLE.pdc().adapt(PersistentDataType.DOUBLE);
    public static final PDCSettingSerializer<Float> FLOAT = SettingSerializers.FLOAT.pdc().adapt(PersistentDataType.FLOAT);
    public static final PDCSettingSerializer<Character> CHAR = SettingSerializers.CHAR.pdc().adapt(CustomPersistentDataType.CHARACTER);
    //endregion

    //region Primitive List Serializers
    public static final PDCSettingSerializer<List<Boolean>> BOOLEAN_LIST = ofList(BOOLEAN);
    public static final PDCSettingSerializer<List<Long>> LONG_LIST = ofList(LONG);
    public static final PDCSettingSerializer<List<Short>> SHORT_LIST = ofList(SHORT);
    public static final PDCSettingSerializer<List<Byte>> BYTE_LIST = ofList(BYTE);
    public static final PDCSettingSerializer<List<Double>> DOUBLE_LIST = ofList(DOUBLE);
    public static final PDCSettingSerializer<List<Float>> FLOAT_LIST = ofList(FLOAT);
    public static final PDCSettingSerializer<List<Character>> CHAR_LIST = ofList(CHAR);
    //endregion

    //region Other Serializers
    public static final PDCSettingSerializer<String> STRING = SettingSerializers.STRING.pdc().adapt(PersistentDataType.STRING);
    public static final PDCSettingSerializer<List<String>> STRING_LIST = ofList(STRING);

    public static final PDCSettingSerializer<Material> MATERIAL = ofEnum(Material.class);
    public static final PDCSettingSerializer<List<Material>> MATERIAL_LIST = ofList(MATERIAL);
     
    public static final PDCSettingSerializer<Duration> DURATION = SettingSerializers.DURATION.pdc().adapt(CustomPersistentDataType.DURATION);
    //endregion

    //region Record Serializers
    public static final PDCSettingSerializer<Vector> VECTOR = ofRecord(Vector.class, instance -> instance.group(
            PDCSettingField.of("x", PDCSettingSerializers.DOUBLE, Vector::getX),
            PDCSettingField.of("y", PDCSettingSerializers.DOUBLE, Vector::getY),
            PDCSettingField.of("z", PDCSettingSerializers.DOUBLE, Vector::getZ)
    ).apply(instance, Vector::new));
    //endregion

    //region Collection Serializer Factories
    public static <T extends Enum<T>> PDCSettingSerializer<T> ofEnum(Class<T> enumClass) {
        return SettingSerializerFactories.ofEnum(enumClass).pdc().adapt(CustomPersistentDataType.forEnum(enumClass));
    }

    public static <T extends Keyed> PDCSettingSerializer<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return SettingSerializerFactories.ofKeyed(keyedClass, valueOfFunction).pdc().adapt(CustomPersistentDataType.forKeyed(keyedClass, valueOfFunction));
    }

    public static <T> PDCSettingSerializer<T[]> ofArray(PDCSettingSerializer<T> serializer) {
        return SettingSerializerFactories.ofArray(serializer).pdc().adapt(CustomPersistentDataType.forArray(serializer.getPDCType()));
    }

    public static <T> PDCSettingSerializer<List<T>> ofList(PDCSettingSerializer<T> serializer) {
        return SettingSerializerFactories.ofList(serializer).pdc().adapt(CustomPersistentDataType.forList(serializer.getPDCType()));
    }

    public static <K, V> PDCSettingSerializer<Map<K, V>> ofMap(PDCSettingSerializer<K> keySerializer, PDCSettingSerializer<V> valueSerializer) {
        return new PDCDelegatingSettingSerializer<>(SettingSerializerFactories.ofMap(keySerializer, valueSerializer), CustomPersistentDataType.forMap(keySerializer.getPDCType(), valueSerializer.getPDCType()));
    }
    //endregion

    //region Record Serializers
    public static <O> PDCSettingSerializer<O> ofRecord(Class<O> clazz, Function<PDCRecordSettingSerializerBuilder<O>, PDCRecordSettingSerializerBuilder.BuiltPDC<O>> builder) {
        return PDCRecordSettingSerializerBuilder.createPDC(clazz, builder);
    }

    public static <T, M> PDCSettingSerializer<T> ofFieldMapped(Class<T> type, String fieldKey, PDCSettingSerializer<M> fieldSerializer, Map<M, PDCSettingSerializer<? extends T>> mapper) {
        return new PDCFieldMappedSettingSerializer<>(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

}
