package dev.rosewood.rosegarden.codec.pdc;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.SettingCodec;
import dev.rosewood.rosegarden.codec.SettingCodecRegistry;
import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.codec.record.RecordField;
import dev.rosewood.rosegarden.codec.record.RecordCodecBuilder;
import dev.rosewood.rosegarden.datatype.CustomPersistentDataType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public final class PdcCodecs {

    private PdcCodecs() {

    }

    //region Primitive Codecs
    public static final PdcCodec<Boolean> BOOLEAN = new PdcCodec<>(Boolean.class, CustomPersistentDataType.BOOLEAN);
    public static final PdcCodec<Integer> INTEGER = new PdcCodec<>(Integer.class, PersistentDataType.INTEGER);
    public static final PdcCodec<Long> LONG = new PdcCodec<>(Long.class, PersistentDataType.LONG);
    public static final PdcCodec<Short> SHORT = new PdcCodec<>(Short.class, PersistentDataType.SHORT);
    public static final PdcCodec<Byte> BYTE = new PdcCodec<>(Byte.class, PersistentDataType.BYTE);
    public static final PdcCodec<Double> DOUBLE = new PdcCodec<>(Double.class, PersistentDataType.DOUBLE);
    public static final PdcCodec<Float> FLOAT = new PdcCodec<>(Float.class, PersistentDataType.FLOAT);
    public static final PdcCodec<Character> CHAR = new PdcCodec<>(Character.class, CustomPersistentDataType.CHAR);
    //endregion

    //region Primitive List Codecs
    public static final PdcCodec<List<Boolean>> BOOLEAN_LIST = ofList(new SettingType<List<Boolean>>() {}, BOOLEAN);
    public static final PdcCodec<List<Integer>> INTEGER_LIST = ofList(new SettingType<List<Integer>>() {}, INTEGER);
    public static final PdcCodec<List<Long>> LONG_LIST = ofList(new SettingType<List<Long>>() {}, LONG);
    public static final PdcCodec<List<Short>> SHORT_LIST = ofList(new SettingType<List<Short>>() {}, SHORT);
    public static final PdcCodec<List<Byte>> BYTE_LIST = ofList(new SettingType<List<Byte>>() {}, BYTE);
    public static final PdcCodec<List<Double>> DOUBLE_LIST = ofList(new SettingType<List<Double>>() {}, DOUBLE);
    public static final PdcCodec<List<Float>> FLOAT_LIST = ofList(new SettingType<List<Float>>() {}, FLOAT);
    public static final PdcCodec<List<Character>> CHAR_LIST = ofList(new SettingType<List<Character>>() {}, CHAR);
    //endregion

    //region Other Codecs
    public static final PdcCodec<String> STRING = new PdcCodec<>(String.class, PersistentDataType.STRING);
    public static final PdcCodec<List<String>> STRING_LIST = ofList(new SettingType<List<String>>() {}, STRING);

    public static final PdcCodec<Material> MATERIAL = ofEnum(Material.class);
    public static final PdcCodec<List<Material>> MATERIAL_LIST = ofList(new SettingType<List<Material>>() {}, MATERIAL);

    public static final PdcCodec<World> WORLD = new PdcCodec<>(World.class, CustomPersistentDataType.WORLD);

    public static final PdcCodec<UUID> UUID = new PdcCodec<>(UUID.class, CustomPersistentDataType.UUID);
    //endregion

    //region Record Codecs
    public static final SettingCodec<PersistentDataContainer, Location> LOCATION = ofRecord(Location.class, instance -> instance.group(
            RecordField.of("world", WORLD, Location::getWorld),
            RecordField.of("x", DOUBLE, Location::getX),
            RecordField.of("y", DOUBLE, Location::getY),
            RecordField.of("z", DOUBLE, Location::getZ),
            RecordField.of("yaw", FLOAT, Location::getYaw),
            RecordField.of("pitch", FLOAT, Location::getPitch)
    ).apply(instance, Location::new));

    public static final SettingCodec<PersistentDataContainer, Vector> VECTOR = ofRecord(Vector.class, instance -> instance.group(
            RecordField.of("x", DOUBLE, Vector::getX),
            RecordField.of("y", DOUBLE, Vector::getY),
            RecordField.of("z", DOUBLE, Vector::getZ)
    ).apply(instance, Vector::new));
    //endregion

    //region Collection Codecs Factories
    public static <T extends Enum<T>> PdcCodec<T> ofEnum(Class<T> enumClass) {
        return PdcCodecFactories.ofEnum(enumClass);
    }

    public static <T extends Keyed> PdcCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return PdcCodecFactories.ofKeyed(keyedClass, valueOfFunction);
    }

    public static <T> PdcCodec<T[]> ofArray(SettingType<T[]> settingType, PdcCodec<T> serializer) {
        return PdcCodecFactories.ofArray(settingType, serializer);
    }

    public static <T> PdcCodec<List<T>> ofList(SettingType<List<T>> settingType, PdcCodec<T> serializer) {
        return PdcCodecFactories.ofList(settingType, serializer);
    }

    public static <K, V> PdcCodec<Map<K, V>> ofMap(SettingType<Map<K, V>> settingType, PdcCodec<K> keySerializer, PdcCodec<V> valueSerializer) {
        return PdcCodecFactories.ofMap(settingType, keySerializer, valueSerializer);
    }
    //endregion

    //region Record Codecs
    public static <O> SettingCodec<PersistentDataContainer, O> ofRecord(Class<O> clazz, Function<RecordCodecBuilder<PersistentDataContainer, O>, SettingCodec<PersistentDataContainer, O>> builder) {
        return RecordCodecBuilder.create(PdcCodecType.INSTANCE, clazz, builder);
    }

    public static <T, M> SettingCodec<PersistentDataContainer, T> ofFieldMapped(Class<T> type, String fieldKey, PdcCodec<M> fieldSerializer, Map<M, SettingCodec<PersistentDataContainer, ? extends T>> mapper) {
        return PdcCodecFactories.ofFieldMapped(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

    public static <C extends SettingCodec<PersistentDataContainer, T>, T> C register(C codec) {
        RosePlugin.instance().getCodecRegistry().register(PdcCodecType.INSTANCE, codec);
        return codec;
    }

    public static void registerDefaults(SettingCodecRegistry codecRegistry) {
        codecRegistry.register(PdcCodecType.INSTANCE, Arrays.asList(
                BOOLEAN,
                INTEGER,
                LONG,
                SHORT,
                BYTE,
                DOUBLE,
                FLOAT,
                CHAR,

                BOOLEAN_LIST,
                INTEGER_LIST,
                LONG_LIST,
                SHORT_LIST,
                BYTE_LIST,
                DOUBLE_LIST,
                FLOAT_LIST,
                CHAR_LIST,

                STRING,
                STRING_LIST,

                MATERIAL,
                MATERIAL_LIST,

                WORLD,
                UUID,
                LOCATION,
                VECTOR
        ));
    }

}
