package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.utils.NMSUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public final class SettingSerializers {

    private SettingSerializers() { }

    //region Primitive Serializers
    public static final SettingSerializer<Boolean> BOOLEAN = new BaseSettingSerializer<Boolean>(Boolean.class, Object::toString, Boolean::parseBoolean) {
        public void write(ConfigurationSection config, String key, Boolean value, String... comments) { setWithComments(config, key, value, comments); }
        public Boolean read(ConfigurationSection config, String key) { return getOrNull(config, key, ConfigurationSection::getBoolean); }
    };

    public static final SettingSerializer<Integer> INTEGER = new BaseSettingSerializer<Integer>(Integer.class, Object::toString, Integer::parseInt) {
        public void write(ConfigurationSection config, String key, Integer value, String... comments) { setWithComments(config, key, value, comments); }
        public Integer read(ConfigurationSection config, String key) { return getOrNull(config, key, ConfigurationSection::getInt); }
    };

    public static final SettingSerializer<Long> LONG = new BaseSettingSerializer<Long>(Long.class, Object::toString, Long::parseLong) {
        public void write(ConfigurationSection config, String key, Long value, String... comments) { setWithComments(config, key, value, comments); }
        public Long read(ConfigurationSection config, String key) { return getOrNull(config, key, ConfigurationSection::getLong); }
    };

    public static final SettingSerializer<Short> SHORT = new BaseSettingSerializer<Short>(Short.class, Object::toString, Short::parseShort) {
        public void write(ConfigurationSection config, String key, Short value, String... comments) { setWithComments(config, key, value, comments); }
        public Short read(ConfigurationSection config, String key) { return getOrNull(config, key, (x, y) -> (short) x.getInt(y)); }
    };

    public static final SettingSerializer<Byte> BYTE = new BaseSettingSerializer<Byte>(Byte.class, Object::toString, Byte::parseByte) {
        public void write(ConfigurationSection config, String key, Byte value, String... comments) { setWithComments(config, key, value, comments); }
        public Byte read(ConfigurationSection config, String key) { return getOrNull(config, key, (x, y) -> (byte) x.getInt(y)); }
    };

    public static final SettingSerializer<Double> DOUBLE = new BaseSettingSerializer<Double>(Double.class, Object::toString, Double::parseDouble) {
        public void write(ConfigurationSection config, String key, Double value, String... comments) { setWithComments(config, key, value, comments); }
        public Double read(ConfigurationSection config, String key) { return getOrNull(config, key, ConfigurationSection::getDouble); }
    };

    public static final SettingSerializer<Float> FLOAT = new BaseSettingSerializer<Float>(Float.class, Object::toString, Float::parseFloat) {
        public void write(ConfigurationSection config, String key, Float value, String... comments) { setWithComments(config, key, value, comments); }
        public Float read(ConfigurationSection config, String key) { return getOrNull(config, key, (x, y) -> (float) x.getDouble(y)); }
    };

    public static final SettingSerializer<Character> CHAR = new BaseSettingSerializer<Character>(Character.class, Object::toString, x -> x.charAt(0)) {
        public void write(ConfigurationSection config, String key, Character value, String... comments) { setWithComments(config, key, value, comments); }
        public Character read(ConfigurationSection config, String key) {
            String value = config.getString(key);
            if (value == null || value.isEmpty())
                return ' ';
            return value.charAt(0);
        }
    };

    private static <T> T getOrNull(ConfigurationSection section, String key, BiFunction<ConfigurationSection, String, T> function) {
        if (!section.contains(key))
            return null;
        return function.apply(section, key);
    }
    //endregion

    //region Primitive List Serializers
    public static final SettingSerializer<List<Boolean>> BOOLEAN_LIST = ofList(BOOLEAN);
    public static final SettingSerializer<List<Long>> LONG_LIST = ofList(LONG);
    public static final SettingSerializer<List<Short>> SHORT_LIST = ofList(SHORT);
    public static final SettingSerializer<List<Byte>> BYTE_LIST = ofList(BYTE);
    public static final SettingSerializer<List<Double>> DOUBLE_LIST = ofList(DOUBLE);
    public static final SettingSerializer<List<Float>> FLOAT_LIST = ofList(FLOAT);
    public static final SettingSerializer<List<Character>> CHAR_LIST = ofList(CHAR);
    //endregion

    //region Other Serializers
    public static final SettingSerializer<String> STRING = new BaseSettingSerializer<String>(String.class, Function.identity(), Function.identity()) {
        public void write(ConfigurationSection config, String key, String value, String... comments) { setWithComments(config, key, value, comments); }
        public String read(ConfigurationSection config, String key) { return config.getString(key); }
    };
    public static final SettingSerializer<List<String>> STRING_LIST = ofList(STRING);

    public static final SettingSerializer<Material> MATERIAL = ofEnum(Material.class);
    public static final SettingSerializer<List<Material>> MATERIAL_LIST = ofList(MATERIAL);

    public static final SettingSerializer<ConfigurationSection> SECTION = new BaseSettingSerializer<ConfigurationSection>(ConfigurationSection.class) {
        public void write(ConfigurationSection config, String key, ConfigurationSection value, String... comments) {
            if (config instanceof CommentedConfigurationSection) {
                ((CommentedConfigurationSection) config).addPathedComments(key, comments);
            } else {
                if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1)) {
                    if (!config.isConfigurationSection(key))
                        config.createSection(key);
                    config.setComments(key, Arrays.asList(comments));
                }
            }
        }
        public ConfigurationSection read(ConfigurationSection config, String key) { return config.getConfigurationSection(key); }
    };
    //endregion

    //region Record Serializers
    public static final SettingSerializer<Vector> VECTOR = ofRecord(Vector.class, instance -> instance.group(
            SettingField.of("x", SettingSerializers.DOUBLE, Vector::getX),
            SettingField.of("y", SettingSerializers.DOUBLE, Vector::getY),
            SettingField.of("z", SettingSerializers.DOUBLE, Vector::getZ)
    ).apply(instance, Vector::new));
    //endregion

    //region Collection Serializer Factories
    public static <T extends Enum<T>> SettingSerializer<T> ofEnum(Class<T> enumClass) {
        return SettingSerializerFactories.ofEnum(enumClass);
    }

    public static <T extends Keyed> SettingSerializer<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return SettingSerializerFactories.ofKeyed(keyedClass, valueOfFunction);
    }

    public static <T> SettingSerializer<T[]> ofArray(SettingSerializer<T> serializer) {
        return SettingSerializerFactories.ofArray(serializer);
    }

    public static <T> SettingSerializer<List<T>> ofList(SettingSerializer<T> serializer) {
        return SettingSerializerFactories.ofList(serializer);
    }

    public static <K, V> SettingSerializer<Map<K, V>> ofMap(SettingSerializer<K> keySerializer, SettingSerializer<V> valueSerializer) {
        return SettingSerializerFactories.ofMap(keySerializer, valueSerializer);
    }
    //endregion

    //region Record Serializers
    public static <O> SettingSerializer<O> ofRecord(Class<O> clazz, Function<RecordSettingSerializerBuilder<O>, RecordSettingSerializerBuilder.Built<O>> builder) {
        return RecordSettingSerializerBuilder.create(clazz, builder);
    }

    public static <T, M> SettingSerializer<T> ofFieldMapped(Class<T> type, String fieldKey, SettingSerializer<M> fieldSerializer, Map<M, SettingSerializer<? extends T>> mapper) {
        return SettingSerializerFactories.ofFieldMapped(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

    public static void setWithComments(ConfigurationSection section, String key, Object value, String[] comments) {
        if (section instanceof CommentedConfigurationSection) {
            ((CommentedConfigurationSection) section).set(key, value, comments);
        } else {
            section.set(key, value);
            if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1))
                section.setComments(key, Arrays.asList(comments));
        }
    }

}
