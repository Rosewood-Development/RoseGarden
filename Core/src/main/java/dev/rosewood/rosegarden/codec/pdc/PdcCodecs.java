package dev.rosewood.rosegarden.codec.pdc;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.RecordSettingSerializerBuilder;
import dev.rosewood.rosegarden.config.SettingField;
import dev.rosewood.rosegarden.config.SettingSerializer;
import dev.rosewood.rosegarden.config.SettingSerializers;
import dev.rosewood.rosegarden.datatype.CustomPersistentDataType;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public final class PdcCodecs {

    private PdcCodecs() {

    }

    //region Primitive Codecs
    public static final PdcCodec<Boolean> BOOLEAN = register(new PdcCodec<>(Boolean.class, CustomPersistentDataType.BOOLEAN));
    public static final PdcCodec<Integer> INTEGER = register(new PdcCodec<>(Integer.class, PersistentDataType.INTEGER));
    public static final PdcCodec<Long> LONG = register(new PdcCodec<>(Long.class, PersistentDataType.LONG));
    public static final PdcCodec<Short> SHORT = register(new PdcCodec<>(Short.class, PersistentDataType.SHORT));
    public static final PdcCodec<Byte> BYTE = register(new PdcCodec<>(Byte.class, PersistentDataType.BYTE));
    public static final PdcCodec<Double> DOUBLE = register(new PdcCodec<>(Double.class, PersistentDataType.DOUBLE));
    public static final PdcCodec<Float> FLOAT = register(new PdcCodec<>(Float.class, PersistentDataType.FLOAT));
    public static final PdcCodec<Character> CHAR = register(new PdcCodec<>(Character.class, CustomPersistentDataType.CHAR));
    //endregion

    //region Primitive List Codecs
    public static final PdcCodec<List<Boolean>> BOOLEAN_LIST = register(ofList(BOOLEAN));
    public static final PdcCodec<List<Integer>> INTEGER_LIST = register(ofList(INTEGER));
    public static final PdcCodec<List<Long>> LONG_LIST = register(ofList(LONG));
    public static final PdcCodec<List<Short>> SHORT_LIST = register(ofList(SHORT));
    public static final PdcCodec<List<Byte>> BYTE_LIST = register(ofList(BYTE));
    public static final PdcCodec<List<Double>> DOUBLE_LIST = register(ofList(DOUBLE));
    public static final PdcCodec<List<Float>> FLOAT_LIST = register(ofList(FLOAT));
    public static final PdcCodec<List<Character>> CHAR_LIST = register(ofList(CHAR));
    //endregion

    //region Other Codecs
    public static final PdcCodec<String> STRING = register(new PdcCodec<>(String.class, PersistentDataType.STRING));
    public static final PdcCodec<List<String>> STRING_LIST = register(ofList(STRING));

    public static final PdcCodec<Material> MATERIAL = register(ofEnum(Material.class));
    public static final PdcCodec<List<Material>> MATERIAL_LIST = register(ofList(MATERIAL));

    public static final PdcCodec<World> WORLD = register(new PdcCodec<>(World.class, CustomPersistentDataType.WORLD));

    public static final PdcCodec<UUID> UUID = register(new PdcCodec<>(UUID.class, CustomPersistentDataType.UUID));
    //endregion

    //region Record Codecs TODO: REPLACED WITH NEW SYSTEM
    public static final SettingSerializer<Location> LOCATION = ofRecord(Location.class, instance -> instance.group(
            SettingField.of("world", SettingSerializers.WORLD, Location::getWorld),
            SettingField.of("x", SettingSerializers.DOUBLE, Location::getX),
            SettingField.of("y", SettingSerializers.DOUBLE, Location::getY),
            SettingField.of("z", SettingSerializers.DOUBLE, Location::getZ),
            SettingField.of("yaw", SettingSerializers.FLOAT, Location::getYaw),
            SettingField.of("pitch", SettingSerializers.FLOAT, Location::getPitch)
    ).apply(instance, Location::new));

    public static final SettingSerializer<Vector> VECTOR = ofRecord(Vector.class, instance -> instance.group(
            SettingField.of("x", SettingSerializers.DOUBLE, Vector::getX),
            SettingField.of("y", SettingSerializers.DOUBLE, Vector::getY),
            SettingField.of("z", SettingSerializers.DOUBLE, Vector::getZ)
    ).apply(instance, Vector::new));
    //endregion

    //region Collection Codecs Factories
    public static <T extends Enum<T>> PdcCodec<T> ofEnum(Class<T> enumClass) {
        return PdcCodecFactories.ofEnum(enumClass);
    }

    public static <T extends Keyed> PdcCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return PdcCodecFactories.ofKeyed(keyedClass, valueOfFunction);
    }

    public static <T> PdcCodec<T[]> ofArray(PdcCodec<T> serializer) {
        return PdcCodecFactories.ofArray(serializer);
    }

    public static <T> PdcCodec<List<T>> ofList(PdcCodec<T> serializer) {
        return PdcCodecFactories.ofList(serializer);
    }

    public static <K, V> PdcCodec<Map<K, V>> ofMap(PdcCodec<K> keySerializer, PdcCodec<V> valueSerializer) {
        return PdcCodecFactories.ofMap(keySerializer, valueSerializer);
    }
    //endregion

    //region Record Codecs
    public static <O> SettingSerializer<O> ofRecord(Class<O> clazz, Function<RecordSettingSerializerBuilder<O>, RecordSettingSerializerBuilder.Built<O>> builder) {
        return RecordSettingSerializerBuilder.create(clazz, builder);
    }

    public static <T, M> PdcCodec<T> ofFieldMapped(Class<T> type, String fieldKey, PdcCodec<M> fieldSerializer, Map<M, PdcCodec<? extends T>> mapper) {
        return PdcCodecFactories.ofFieldMapped(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

    public static <T> PdcCodec<T> register(PdcCodec<T> codec) {
        RosePlugin.instance().getCodecRegistry().register(PdcCodecType.INSTANCE, codec);
        return codec;
    }

}
