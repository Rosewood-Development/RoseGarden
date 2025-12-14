package dev.rosewood.rosegarden.codec.yaml;

import dev.rosewood.rosegarden.codec.SettingCodec;
import dev.rosewood.rosegarden.codec.SettingType;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import static dev.rosewood.rosegarden.config.SettingSerializers.setWithComments;

public final class YamlCodecFactories {

    public static <T extends Enum<T>> YamlCodec<T> ofEnum(Class<T> enumClass) {
        return wrap(SettingCodec.ofMapped(enumClass, YamlCodecs.STRING, x -> x.name().toLowerCase(), x -> Enum.valueOf(enumClass, x.toUpperCase())));
    }

    public static <T extends Keyed> YamlCodec<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return wrap(SettingCodec.ofMapped(keyedClass, YamlCodecs.STRING, x -> translateName(x.getKey()), x -> valueOfFunction.apply(translateKey(x))));
    }

    private static <T> YamlCodec<T> wrap(SettingCodec<ConfigurationSection, T> codec) {
        return new YamlCodec<T>(codec.getSettingType()) {
            public void encode(ConfigurationSection container, String key, T value, String... comments) { codec.encode(container, key, value, comments); }
            public T decode(ConfigurationSection container, String key) { return codec.decode(container, key); }
            public String encodeString(T value) { return codec.encodeString(value); }
            public T decodeString(String value) { return codec.decodeString(value); }
            public boolean supportsStringEncoding() { return codec.supportsStringEncoding(); }
            public boolean isValid(ConfigurationSection container, String key) { return codec.isValid(container, key); }
            public boolean isPresent(ConfigurationSection container, String key) { return codec.isPresent(container, key); }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> YamlCodec<T[]> ofArray(YamlCodec<T> elementCodec) {
        if (!(elementCodec.getSettingType().getType() instanceof Class<?>))
            throw new IllegalArgumentException("Array element codec must be for a basic type");
        Class<T> elementType = (Class<T>) elementCodec.getSettingType().getType();

        return new YamlCodec<T[]>(new SettingType<T[]>() {}) {
            public void encode(ConfigurationSection config, String key, T[] value, String... comments) {
                if (elementCodec.supportsStringEncoding()) {
                    setWithComments(config, key, Arrays.stream(value).map(x -> x == null ? "" : elementCodec.encodeString(x)).collect(Collectors.toList()), comments);
                } else {
                    ConfigurationSection section = getOrCreateSection(config, key, comments);
                    int index = 0;
                    for (T t : value) {
                        if (t == null) {
                            section.set(String.valueOf(index++), "");
                        } else {
                            elementCodec.encode(section, String.valueOf(index++), t);
                        }
                    }
                }
            }
            public T[] decode(ConfigurationSection config, String key) {
                if (!this.isValid(config, key))
                    return null;
                if (elementCodec.supportsStringEncoding()) {
                    List<String> contents = config.getStringList(key);
                    T[] array = (T[]) Array.newInstance(elementType, contents.size());
                    for (int i = 0; i < contents.size(); i++) {
                        String content = contents.get(i);
                        if (!content.isEmpty())
                            array[i] = elementCodec.decodeString(content);
                    }
                    return array;
                } else {
                    ConfigurationSection section = config.getConfigurationSection(key);
                    if (section != null) {
                        List<String> contents = config.getStringList(key);
                        T[] array = (T[]) Array.newInstance(elementType, contents.size());
                        for (String configKey : section.getKeys(false)) {
                            int index = Integer.parseInt(configKey);
                            String content = section.getString(configKey, "");
                            if (!content.isEmpty())
                                array[index] = elementCodec.decode(section, configKey);
                        }
                    }
                    return (T[]) Array.newInstance(elementType, 0);
                }
            }
            public String encodeString(T[] values) {
                if (!elementCodec.supportsStringEncoding() || values.length > 5)
                    return null;
                StringBuilder builder = new StringBuilder("Default: [");
                Iterator<T> iterator = Arrays.stream(values).iterator();
                boolean hasNext = iterator.hasNext();
                while (hasNext) {
                    T value = iterator.next();
                    String defaultValueStringKey = elementCodec.encodeString(value);
                    if (RoseGardenUtils.containsConfigSpecialCharacters(defaultValueStringKey)) {
                        builder.append('\'').append(defaultValueStringKey).append('\'');
                    } else if (defaultValueStringKey.isEmpty()) {
                        builder.append("''");
                    } else {
                        builder.append(defaultValueStringKey);
                    }
                    hasNext = iterator.hasNext();
                    if (hasNext)
                        builder.append(", ");
                }
                builder.append(']');
                if (builder.length() > 60)
                    return null;
                return builder.toString();
            }
        };
    }

    public static <T> YamlCodec<List<T>> ofList(YamlCodec<T> elementCodec) {
        return new YamlCodec<List<T>>(new SettingType<List<T>>() {}) {
            public void encode(ConfigurationSection config, String key, List<T> value, String... comments) {
                if (elementCodec.supportsStringEncoding()) {
                    setWithComments(config, key, value.stream().map(elementCodec::encodeString).collect(Collectors.toList()), comments);
                } else {
                    ConfigurationSection section = getOrCreateSection(config, key, comments);
                    int index = 0;
                    for (T t : value)
                        elementCodec.encode(section, String.valueOf(index++), t);
                }
            }
            public List<T> decode(ConfigurationSection container, String key) {
                if (!this.isValid(container, key))
                    return null;
                if (elementCodec.supportsStringEncoding())
                    return container.getStringList(key).stream().map(elementCodec::decodeString).collect(Collectors.toList());
                List<T> list = new ArrayList<>();
                ConfigurationSection section = container.getConfigurationSection(key);
                if (section != null) {
                    for (String configKey : section.getKeys(false)) {
                        T t = elementCodec.decode(section, configKey);
                        if (t != null)
                            list.add(t);
                    }
                }
                return list;
            }
            public String encodeString(List<T> values) {
                if (!elementCodec.supportsStringEncoding() || values.size() > 5)
                    return null;
                StringBuilder builder = new StringBuilder("Default: [");
                Iterator<T> iterator = values.iterator();
                boolean hasNext = iterator.hasNext();
                while (hasNext) {
                    T value = iterator.next();
                    String defaultValueStringKey = elementCodec.encodeString(value);
                    if (RoseGardenUtils.containsConfigSpecialCharacters(defaultValueStringKey)) {
                        builder.append('\'').append(defaultValueStringKey).append('\'');
                    } else if (defaultValueStringKey.isEmpty()) {
                        builder.append("''");
                    } else {
                        builder.append(defaultValueStringKey);
                    }
                    hasNext = iterator.hasNext();
                    if (hasNext)
                        builder.append(", ");
                }
                builder.append(']');
                if (builder.length() > 60)
                    return null;
                return builder.toString();
            }
        };
    }

    public static <K, V> YamlCodec<Map<K, V>> ofMap(YamlCodec<K> keyElementCodec, YamlCodec<V> valueElementCodec) {
        return new YamlCodec<Map<K, V>>(new SettingType<Map<K, V>>() {}) {
            public void encode(ConfigurationSection config, String key, Map<K, V> value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                if (keyElementCodec.supportsStringEncoding() && valueElementCodec.supportsStringEncoding()) {
                    for (Map.Entry<K, V> entry : value.entrySet())
                        section.set(keyElementCodec.encodeString(entry.getKey()), valueElementCodec.encodeString(entry.getValue()));
                } else {
                    int index = 0;
                    for (Map.Entry<K, V> entry : value.entrySet()) {
                        ConfigurationSection indexedSection = getOrCreateSection(section, String.valueOf(index++));
                        keyElementCodec.encode(indexedSection, "key", entry.getKey());
                        valueElementCodec.encode(indexedSection, "value", entry.getValue());
                    }
                }
            }
            public Map<K, V> decode(ConfigurationSection config, String key) {
                if (!this.isValid(config, key))
                    return null;
                Map<K, V> map = new HashMap<>();
                ConfigurationSection section = config.getConfigurationSection(key);
                if (section != null) {
                    if (keyElementCodec.supportsStringEncoding() && valueElementCodec.supportsStringEncoding()) {
                        for (String configKey : section.getKeys(false)) {
                            K k = keyElementCodec.decodeString(configKey);
                            V v = valueElementCodec.decodeString(section.getString(configKey, ""));
                            if (k != null && v != null)
                                map.put(k, v);
                        }
                    } else {
                        for (String configKey : section.getKeys(false)) {
                            ConfigurationSection indexedSection = section.getConfigurationSection(configKey);
                            if (indexedSection != null) {
                                K k = keyElementCodec.decode(indexedSection, "key");
                                V v = valueElementCodec.decode(indexedSection, "value");
                                if (k != null && v != null)
                                    map.put(k, v);
                            }
                        }
                    }
                }
                return map;
            }
        };
    }

    public static <T, M> YamlCodec<T> ofFieldMapped(Class<T> type, String fieldKey, YamlCodec<M> fieldCodec, Map<M, YamlCodec<? extends T>> mapper) {
        return new YamlCodec<T>(type) {
            @Override
            public void encode(ConfigurationSection container, String key, T value, String... comments) {
                YamlCodec<T> codec = this.mapField(fieldCodec.decode(container, fieldKey));
                if (codec != null)
                    codec.encode(container, key, value, comments);
            }

            @Override
            public T decode(ConfigurationSection container, String key) {
                YamlCodec<T> codec = this.mapField(fieldCodec.decode(container, fieldKey));
                return codec != null ? codec.decode(container, key) : null;
            }

            @Override
            public boolean isValid(ConfigurationSection container, String key) {
                YamlCodec<T> codec = this.mapField(fieldCodec.decode(container, fieldKey));
                return codec != null && codec.isValid(container, key);
            }

            @SuppressWarnings("unchecked") // always maps to subtypes since it extends T, catching just in case
            private YamlCodec<T> mapField(M value) {
                if (value == null)
                    return null;
                try {
                    YamlCodec<? extends T> codec = mapper.get(value);
                    return (YamlCodec<T>) codec;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    static ConfigurationSection getOrCreateSection(ConfigurationSection container, String key, String... comments) {
        if (container instanceof CommentedConfigurationSection) {
            CommentedConfigurationSection commentedConfig = (CommentedConfigurationSection) container;
            CommentedConfigurationSection section = commentedConfig.getConfigurationSection(key);
            if (section == null) {
                commentedConfig.addPathedComments(key, comments);
                section = commentedConfig.createSection(key);
            }
            return section;
        } else {
            ConfigurationSection section = container.getConfigurationSection(key);
            if (section == null) {
                section = container.createSection(key);
                if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1))
                    container.setComments(key, Arrays.asList(comments));
            }
            return section;
        }
    }

    private static NamespacedKey translateKey(String key) {
        return NamespacedKey.fromString(key);
    }

    private static String translateName(NamespacedKey key) {
        if (key.getNamespace().equals(NamespacedKey.MINECRAFT))
            return key.getKey();
        return key.toString();
    }

}
