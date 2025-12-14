package dev.rosewood.rosegarden.codec.yaml;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.RecordSettingSerializerBuilder;
import dev.rosewood.rosegarden.config.SettingField;
import dev.rosewood.rosegarden.config.SettingSerializer;
import dev.rosewood.rosegarden.config.SettingSerializers;
import dev.rosewood.rosegarden.utils.NMSUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public final class YamlCodecs {

    private YamlCodecs() {

    }

    //region Primitive Codecs
    public static final YamlCodec<Boolean> BOOLEAN = register(new YamlCodec<Boolean>(Boolean.class, Object::toString, Boolean::parseBoolean) {
        public void encode(ConfigurationSection container, String key, Boolean value, String... comments) { setWithComments(container, key, value, comments); }
        public Boolean decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getBoolean); }
    });

    public static final YamlCodec<Integer> INTEGER = register(new YamlCodec<Integer>(Integer.class, Object::toString, Integer::parseInt) {
        public void encode(ConfigurationSection container, String key, Integer value, String... comments) { setWithComments(container, key, value, comments); }
        public Integer decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getInt); }
    });

    public static final YamlCodec<Long> LONG = register(new YamlCodec<Long>(Long.class, Object::toString, Long::parseLong) {
        public void encode(ConfigurationSection container, String key, Long value, String... comments) { setWithComments(container, key, value, comments); }
        public Long decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getLong); }
    });

    public static final YamlCodec<Short> SHORT = register(new YamlCodec<Short>(Short.class, Object::toString, Short::parseShort) {
        public void encode(ConfigurationSection container, String key, Short value, String... comments) { setWithComments(container, key, value, comments); }
        public Short decode(ConfigurationSection container, String key) { return getOrNull(container, key, (x, y) -> (short) x.getInt(y)); }
    });

    public static final YamlCodec<Byte> BYTE = register(new YamlCodec<Byte>(Byte.class, Object::toString, Byte::parseByte) {
        public void encode(ConfigurationSection container, String key, Byte value, String... comments) { setWithComments(container, key, value, comments); }
        public Byte decode(ConfigurationSection container, String key) { return getOrNull(container, key, (x, y) -> (byte) x.getInt(y)); }
    });

    public static final YamlCodec<Double> DOUBLE = register(new YamlCodec<Double>(Double.class, Object::toString, Double::parseDouble) {
        public void encode(ConfigurationSection container, String key, Double value, String... comments) { setWithComments(container, key, value, comments); }
        public Double decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getDouble); }
    });

    public static final YamlCodec<Float> FLOAT = register(new YamlCodec<Float>(Float.class, Object::toString, Float::parseFloat) {
        public void encode(ConfigurationSection container, String key, Float value, String... comments) { setWithComments(container, key, value, comments); }
        public Float decode(ConfigurationSection container, String key) { return getOrNull(container, key, (x, y) -> (float) x.getDouble(y)); }
    });

    public static final YamlCodec<Character> CHAR = register(new YamlCodec<Character>(Character.class, Object::toString, x -> x.charAt(0)) {
        public void encode(ConfigurationSection container, String key, Character value, String... comments) { setWithComments(container, key, value, comments); }
        public Character decode(ConfigurationSection container, String key) {
            String value = container.getString(key);
            if (value == null || value.isEmpty())
                return ' ';
            return value.charAt(0);
        }
    });

    private static <T> T getOrNull(ConfigurationSection section, String key, BiFunction<ConfigurationSection, String, T> function) {
        if (!section.contains(key))
            return null;
        return function.apply(section, key);
    }
    //endregion

    //region Primitive List Codecs
    public static final YamlCodec<List<Boolean>> BOOLEAN_LIST = register(ofList(BOOLEAN));
    public static final YamlCodec<List<Integer>> INTEGER_LIST = register(ofList(INTEGER));
    public static final YamlCodec<List<Long>> LONG_LIST = register(ofList(LONG));
    public static final YamlCodec<List<Short>> SHORT_LIST = register(ofList(SHORT));
    public static final YamlCodec<List<Byte>> BYTE_LIST = register(ofList(BYTE));
    public static final YamlCodec<List<Double>> DOUBLE_LIST = register(ofList(DOUBLE));
    public static final YamlCodec<List<Float>> FLOAT_LIST = register(ofList(FLOAT));
    public static final YamlCodec<List<Character>> CHAR_LIST = register(ofList(CHAR));
    //endregion

    //region Other Codecs
    public static final YamlCodec<String> STRING = register(new YamlCodec<String>(String.class, Function.identity(), Function.identity()) {
        public void encode(ConfigurationSection container, String key, String value, String... comments) { setWithComments(container, key, value, comments); }
        public String decode(ConfigurationSection container, String key) { return container.getString(key); }
    });
    public static final YamlCodec<List<String>> STRING_LIST = register(ofList(STRING));

    public static final YamlCodec<Material> MATERIAL = register(ofEnum(Material.class));
    public static final YamlCodec<List<Material>> MATERIAL_LIST = register(ofList(MATERIAL));

    public static final YamlCodec<ConfigurationSection> SECTION = register(new YamlCodec<ConfigurationSection>(ConfigurationSection.class) {
        public void encode(ConfigurationSection container, String key, ConfigurationSection value, String... comments) {
            if (container instanceof CommentedConfigurationSection) {
                ((CommentedConfigurationSection) container).addPathedComments(key, comments);
            } else {
                if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1)) {
                    if (!container.isConfigurationSection(key))
                        container.createSection(key);
                    container.setComments(key, Arrays.asList(comments));
                }
            }
        }
        public ConfigurationSection decode(ConfigurationSection container, String key) { return container.getConfigurationSection(key); }
    });

    public static final YamlCodec<World> WORLD = register(new YamlCodec<World>(World.class, World::getName, Bukkit::getWorld) {
        public void encode(ConfigurationSection container, String key, World value, String... comments) { setWithComments(container, key, value, comments); }
        public World decode(ConfigurationSection container, String key) {
            String worldName = container.getString(key);
            return worldName != null ? Bukkit.getWorld(worldName) : null;
        }
    });

    public static final YamlCodec<UUID> UUID = register(new YamlCodec<UUID>(UUID.class, java.util.UUID::toString, java.util.UUID::fromString) {
        public void encode(ConfigurationSection container, String key, UUID value, String... comments) { setWithComments(container, key, value, comments); }
        public UUID decode(ConfigurationSection container, String key) {
            String uuid = container.getString(key);
            return uuid != null ? java.util.UUID.fromString(uuid) : null;
        }
    });
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
    public static <T extends Enum<T>> YamlCodec<T> ofEnum(Class<T> enumClass) {
        return YamlCodecFactories.ofEnum(enumClass);
    }

    public static <T extends Keyed> YamlCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return YamlCodecFactories.ofKeyed(keyedClass, valueOfFunction);
    }

    public static <T> YamlCodec<T[]> ofArray(YamlCodec<T> serializer) {
        return YamlCodecFactories.ofArray(serializer);
    }

    public static <T> YamlCodec<List<T>> ofList(YamlCodec<T> serializer) {
        return YamlCodecFactories.ofList(serializer);
    }

    public static <K, V> YamlCodec<Map<K, V>> ofMap(YamlCodec<K> keySerializer, YamlCodec<V> valueSerializer) {
        return YamlCodecFactories.ofMap(keySerializer, valueSerializer);
    }
    //endregion

    //region Record Codecs
    public static <O> SettingSerializer<O> ofRecord(Class<O> clazz, Function<RecordSettingSerializerBuilder<O>, RecordSettingSerializerBuilder.Built<O>> builder) {
        return RecordSettingSerializerBuilder.create(clazz, builder);
    }

    public static <T, M> YamlCodec<T> ofFieldMapped(Class<T> type, String fieldKey, YamlCodec<M> fieldSerializer, Map<M, YamlCodec<? extends T>> mapper) {
        return YamlCodecFactories.ofFieldMapped(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

    public static <T> YamlCodec<T> register(YamlCodec<T> codec) {
        RosePlugin.instance().getCodecRegistry().register(YamlCodecType.INSTANCE, codec);
        return codec;
    }

    private static void setWithComments(ConfigurationSection section, String key, Object value, String[] comments) {
        if (section instanceof CommentedConfigurationSection) {
            ((CommentedConfigurationSection) section).set(key, value, comments);
        } else {
            section.set(key, value);
            if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1))
                section.setComments(key, Arrays.asList(comments));
        }
    }

}
