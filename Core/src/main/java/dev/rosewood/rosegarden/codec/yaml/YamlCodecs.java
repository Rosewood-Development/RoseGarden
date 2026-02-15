package dev.rosewood.rosegarden.codec.yaml;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.codec.SettingCodec;
import dev.rosewood.rosegarden.codec.SettingCodecRegistry;
import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.codec.record.RecordCodecBuilder;
import dev.rosewood.rosegarden.codec.record.RecordField;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
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
    public static final YamlCodec<Boolean> BOOLEAN = new YamlCodec<Boolean>(Boolean.class, Object::toString, Boolean::parseBoolean) {
        public void encode(ConfigurationSection container, String key, Boolean value, boolean appendDefault, String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Boolean decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getBoolean); }
    };

    public static final YamlCodec<Integer> INTEGER = new YamlCodec<Integer>(Integer.class, Object::toString, Integer::parseInt) {
        public void encode(ConfigurationSection container, String key, Integer value, boolean appendDefault, String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Integer decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getInt); }
    };

    public static final YamlCodec<Long> LONG = new YamlCodec<Long>(Long.class, Object::toString, Long::parseLong) {
        public void encode(ConfigurationSection container, String key, Long value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Long decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getLong); }
    };

    public static final YamlCodec<Short> SHORT = new YamlCodec<Short>(Short.class, Object::toString, Short::parseShort) {
        public void encode(ConfigurationSection container, String key, Short value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Short decode(ConfigurationSection container, String key) { return getOrNull(container, key, (x, y) -> (short) x.getInt(y)); }
    };

    public static final YamlCodec<Byte> BYTE = new YamlCodec<Byte>(Byte.class, Object::toString, Byte::parseByte) {
        public void encode(ConfigurationSection container, String key, Byte value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Byte decode(ConfigurationSection container, String key) { return getOrNull(container, key, (x, y) -> (byte) x.getInt(y)); }
    };

    public static final YamlCodec<Double> DOUBLE = new YamlCodec<Double>(Double.class, Object::toString, Double::parseDouble) {
        public void encode(ConfigurationSection container, String key, Double value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Double decode(ConfigurationSection container, String key) { return getOrNull(container, key, ConfigurationSection::getDouble); }
    };

    public static final YamlCodec<Float> FLOAT = new YamlCodec<Float>(Float.class, Object::toString, Float::parseFloat) {
        public void encode(ConfigurationSection container, String key, Float value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Float decode(ConfigurationSection container, String key) { return getOrNull(container, key, (x, y) -> (float) x.getDouble(y)); }
    };

    public static final YamlCodec<Character> CHAR = new YamlCodec<Character>(Character.class, Object::toString, x -> x.charAt(0)) {
        public void encode(ConfigurationSection container, String key, Character value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public Character decode(ConfigurationSection container, String key) {
            String value = container.getString(key);
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

    //region Primitive List Codecs
    public static final YamlCodec<List<Boolean>> BOOLEAN_LIST = ofList(new SettingType<List<Boolean>>() {}, BOOLEAN);
    public static final YamlCodec<List<Integer>> INTEGER_LIST = ofList(new SettingType<List<Integer>>() {}, INTEGER);
    public static final YamlCodec<List<Long>> LONG_LIST = ofList(new SettingType<List<Long>>() {}, LONG);
    public static final YamlCodec<List<Short>> SHORT_LIST = ofList(new SettingType<List<Short>>() {}, SHORT);
    public static final YamlCodec<List<Byte>> BYTE_LIST = ofList(new SettingType<List<Byte>>() {}, BYTE);
    public static final YamlCodec<List<Double>> DOUBLE_LIST = ofList(new SettingType<List<Double>>() {}, DOUBLE);
    public static final YamlCodec<List<Float>> FLOAT_LIST = ofList(new SettingType<List<Float>>() {}, FLOAT);
    public static final YamlCodec<List<Character>> CHAR_LIST = ofList(new SettingType<List<Character>>() {}, CHAR);
    //endregion

    //region Other Codecs
    public static final YamlCodec<String> STRING = new YamlCodec<String>(String.class, Function.identity(), Function.identity()) {
        public void encode(ConfigurationSection container, String key, String value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public String decode(ConfigurationSection container, String key) { return container.getString(key); }
    };
    public static final YamlCodec<List<String>> STRING_LIST = ofList(new SettingType<List<String>>() {}, STRING);

    public static final YamlCodec<Material> MATERIAL = ofEnum(Material.class);
    public static final YamlCodec<List<Material>> MATERIAL_LIST = ofList(new SettingType<List<Material>>() {}, MATERIAL);

    public static final YamlCodec<ConfigurationSection> SECTION = new YamlCodec<ConfigurationSection>(ConfigurationSection.class) {
        public void encode(ConfigurationSection container, String key, ConfigurationSection value, boolean appendDefault,String... comments) {
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
    };

    public static final YamlCodec<World> WORLD = new YamlCodec<World>(World.class, World::getName, Bukkit::getWorld) {
        public void encode(ConfigurationSection container, String key, World value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public World decode(ConfigurationSection container, String key) {
            String worldName = container.getString(key);
            return worldName != null ? Bukkit.getWorld(worldName) : null;
        }
    };

    public static final YamlCodec<UUID> UUID = new YamlCodec<UUID>(UUID.class, java.util.UUID::toString, java.util.UUID::fromString) {
        public void encode(ConfigurationSection container, String key, UUID value, boolean appendDefault,String... comments) { setWithComments(this, container, key, value, appendDefault, comments); }
        public UUID decode(ConfigurationSection container, String key) {
            String uuid = container.getString(key);
            return uuid != null ? java.util.UUID.fromString(uuid) : null;
        }
    };
    //endregion

    //region Record Codecs
    public static final SettingCodec<ConfigurationSection, Location> LOCATION = ofRecord(Location.class, instance -> instance.group(
            RecordField.of("world", WORLD, Location::getWorld),
            RecordField.of("x", DOUBLE, Location::getX),
            RecordField.of("y", DOUBLE, Location::getY),
            RecordField.of("z", DOUBLE, Location::getZ),
            RecordField.of("yaw", FLOAT, Location::getYaw),
            RecordField.of("pitch", FLOAT, Location::getPitch)
    ).apply(instance, Location::new));

    public static final SettingCodec<ConfigurationSection, Vector> VECTOR = ofRecord(Vector.class, instance -> instance.group(
            RecordField.of("x", DOUBLE, Vector::getX),
            RecordField.of("y", DOUBLE, Vector::getY),
            RecordField.of("z", DOUBLE, Vector::getZ)
    ).apply(instance, Vector::new));
    //endregion

    //region Collection Codecs Factories
    public static <T extends Enum<T>> YamlCodec<T> ofEnum(Class<T> enumClass) {
        return YamlCodecFactories.ofEnum(enumClass);
    }

    public static <T extends Keyed> YamlCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return YamlCodecFactories.ofKeyed(keyedClass, valueOfFunction);
    }

    public static <T> YamlCodec<T[]> ofArray(SettingType<T[]> settingType, YamlCodec<T> serializer) {
        return YamlCodecFactories.ofArray(settingType, serializer);
    }

    public static <T> YamlCodec<List<T>> ofList(SettingType<List<T>> settingType, YamlCodec<T> serializer) {
        return YamlCodecFactories.ofList(settingType, serializer);
    }

    public static <K, V> YamlCodec<Map<K, V>> ofMap(SettingType<Map<K, V>> settingType, YamlCodec<K> keySerializer, YamlCodec<V> valueSerializer) {
        return YamlCodecFactories.ofMap(settingType, keySerializer, valueSerializer);
    }
    //endregion

    //region Record Codecs
    public static <O> SettingCodec<ConfigurationSection, O> ofRecord(Class<O> clazz, Function<RecordCodecBuilder<ConfigurationSection, O>, SettingCodec<ConfigurationSection, O>> builder) {
        return RecordCodecBuilder.create(YamlCodecType.INSTANCE, clazz, builder);
    }

    public static <T, M> YamlCodec<T> ofFieldMapped(Class<T> type, String fieldKey, SettingCodec<ConfigurationSection, M> fieldSerializer, Map<M, SettingCodec<ConfigurationSection, ? extends T>> mapper) {
        return YamlCodecFactories.ofFieldMapped(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

    public static <T> void setWithComments(SettingCodec<?, T> codec, ConfigurationSection section, String key, T value, boolean appendDefault, String[] comments) {
        comments = appendDefault ? appendDefaultComment(codec, value, comments) : comments;

        if (section instanceof CommentedConfigurationSection) {
            ((CommentedConfigurationSection) section).set(key, value, comments);
        } else {
            section.set(key, value);
            if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1))
                section.setComments(key, Arrays.asList(comments));
        }
    }

    private static <T> String[] appendDefaultComment(SettingCodec<?, T> codec, T value, String[] comments) {
        String defaultComment = codec.createDefaultComment(value);
        if (defaultComment == null)
            return comments;

        String[] newComments = new String[comments.length + 1];
        System.arraycopy(comments, 0, newComments, 0, comments.length);
        newComments[comments.length] = defaultComment;
        return newComments;
    }

    public static <C extends SettingCodec<ConfigurationSection, T>, T> C register(C codec) {
        RosePlugin.instance().getCodecRegistry().register(YamlCodecType.INSTANCE, codec);
        return codec;
    }

    public static void registerDefaults(SettingCodecRegistry codecRegistry) {
        codecRegistry.register(YamlCodecType.INSTANCE, Arrays.asList(
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

                SECTION,
                WORLD,
                UUID,
                LOCATION,
                VECTOR
        ));
    }

}
