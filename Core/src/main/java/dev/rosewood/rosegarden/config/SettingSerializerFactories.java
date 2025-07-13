package dev.rosewood.rosegarden.config;

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

@SuppressWarnings("unchecked")
final class SettingSerializerFactories {

    private SettingSerializerFactories() { }

    //region Serializer Factories
    public static <T extends Enum<T>> SettingSerializer<T> ofEnum(Class<T> enumClass) {
        return new BaseSettingSerializer<T>(enumClass, x -> x.name().toLowerCase(), x -> Enum.valueOf(enumClass, x.toUpperCase())) {
            public void write(ConfigurationSection config, String key, T value, String... comments) { setWithComments(config, key, value.name().toLowerCase(), comments); }
            public T read(ConfigurationSection config, String key) {
                try {
                    return Enum.valueOf(enumClass, config.getString(key, "").toUpperCase());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        };
    }

    public static <T extends Keyed> SettingSerializer<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return new BaseSettingSerializer<T>(keyedClass, x -> translateName(x.getKey()), x -> valueOfFunction.apply(translateKey(x))) {
            public void write(ConfigurationSection config, String key, T value, String... comments) { setWithComments(config, key, translateName(value.getKey()), comments); }
            public T read(ConfigurationSection config, String key) { return valueOfFunction.apply(translateKey(config.getString(key))); }
        };
    }

    public static <T> SettingSerializer<T[]> ofArray(SettingSerializer<T> serializer) {
        return new BaseSettingSerializer<T[]>((Class<T[]>) Array.newInstance(serializer.getType(), 0).getClass()) {
            public void write(ConfigurationSection config, String key, T[] value, String... comments) {
                if (serializer.isStringKey()) {
                    setWithComments(config, key, Arrays.stream(value).map(x -> x == null ? "" : serializer.asStringKey(x)).collect(Collectors.toList()), comments);
                } else {
                    ConfigurationSection section = getOrCreateSection(config, key, comments);
                    int index = 0;
                    for (T t : value) {
                        if (t == null) {
                            section.set(String.valueOf(index++), "");
                        } else {
                            serializer.write(section, String.valueOf(index++), t);
                        }
                    }
                }
            }
            public T[] read(ConfigurationSection config, String key) {
                if (!this.readIsValid(config, key))
                    return null;
                if (serializer.isStringKey()) {
                    List<String> contents = config.getStringList(key);
                    T[] array = (T[]) Array.newInstance(serializer.getType(), contents.size());
                    for (int i = 0; i < contents.size(); i++) {
                        String content = contents.get(i);
                        if (!content.isEmpty())
                            array[i] = serializer.fromStringKey(content);
                    }
                    return array;
                } else {
                    ConfigurationSection section = config.getConfigurationSection(key);
                    if (section != null) {
                        List<String> contents = config.getStringList(key);
                        T[] array = (T[]) Array.newInstance(serializer.getType(), contents.size());
                        for (String configKey : section.getKeys(false)) {
                            int index = Integer.parseInt(configKey);
                            String content = section.getString(configKey, "");
                            if (!content.isEmpty())
                                array[index] = serializer.read(section, configKey);
                        }
                    }
                    return (T[]) Array.newInstance(serializer.getType(), 0);
                }
            }
            public String getDefaultCommentText(T[] values) {
                if (!serializer.isStringKey() || values.length > 5)
                    return null;
                StringBuilder builder = new StringBuilder("Default: [");
                Iterator<T> iterator = Arrays.stream(values).iterator();
                boolean hasNext = iterator.hasNext();
                while (hasNext) {
                    T value = iterator.next();
                    String defaultValueStringKey = serializer.asStringKey(value);
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

    public static <T> SettingSerializer<List<T>> ofList(SettingSerializer<T> serializer) {
        return new BaseSettingSerializer<List<T>>((Class<List<T>>) (Class<?>) List.class) {
            public void write(ConfigurationSection config, String key, List<T> value, String... comments) {
                if (serializer.isStringKey()) {
                    setWithComments(config, key, value.stream().map(serializer::asStringKey).collect(Collectors.toList()), comments);
                } else {
                    ConfigurationSection section = getOrCreateSection(config, key, comments);
                    int index = 0;
                    for (T t : value)
                        serializer.write(section, String.valueOf(index++), t);
                }
            }
            public List<T> read(ConfigurationSection config, String key) {
                if (!this.readIsValid(config, key))
                    return null;
                if (serializer.isStringKey())
                    return config.getStringList(key).stream().map(serializer::fromStringKey).collect(Collectors.toList());
                List<T> list = new ArrayList<>();
                ConfigurationSection section = config.getConfigurationSection(key);
                if (section != null) {
                    for (String configKey : section.getKeys(false)) {
                        T t = serializer.read(section, configKey);
                        if (t != null)
                            list.add(t);
                    }
                }
                return list;
            }
            public String getDefaultCommentText(List<T> values) {
                if (!serializer.isStringKey() || values.size() > 5)
                    return null;
                StringBuilder builder = new StringBuilder("Default: [");
                Iterator<T> iterator = values.iterator();
                boolean hasNext = iterator.hasNext();
                while (hasNext) {
                    T value = iterator.next();
                    String defaultValueStringKey = serializer.asStringKey(value);
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

    public static <K, V> SettingSerializer<Map<K, V>> ofMap(SettingSerializer<K> keySerializer, SettingSerializer<V> valueSerializer) {
        return new BaseSettingSerializer<Map<K, V>>((Class<Map<K, V>>) (Class<?>) Map.class) {
            public void write(ConfigurationSection config, String key, Map<K, V> value, String... comments) {
                ConfigurationSection section = getOrCreateSection(config, key, comments);
                if (keySerializer.isStringKey() && valueSerializer.isStringKey()) {
                    for (Map.Entry<K, V> entry : value.entrySet())
                        section.set(keySerializer.asStringKey(entry.getKey()), valueSerializer.asStringKey(entry.getValue()));
                } else {
                    int index = 0;
                    for (Map.Entry<K, V> entry : value.entrySet()) {
                        ConfigurationSection indexedSection = getOrCreateSection(section, String.valueOf(index++));
                        keySerializer.write(indexedSection, "key", entry.getKey());
                        valueSerializer.write(indexedSection, "value", entry.getValue());
                    }
                }
            }
            public Map<K, V> read(ConfigurationSection config, String key) {
                if (!this.readIsValid(config, key))
                    return null;
                Map<K, V> map = new HashMap<>();
                ConfigurationSection section = config.getConfigurationSection(key);
                if (section != null) {
                    if (keySerializer.isStringKey() && valueSerializer.isStringKey()) {
                        for (String configKey : section.getKeys(false)) {
                            K k = keySerializer.fromStringKey(configKey);
                            V v = valueSerializer.fromStringKey(section.getString(configKey, ""));
                            if (k != null && v != null)
                                map.put(k, v);
                        }
                    } else {
                        for (String configKey : section.getKeys(false)) {
                            ConfigurationSection indexedSection = section.getConfigurationSection(configKey);
                            if (indexedSection != null) {
                                K k = keySerializer.read(indexedSection, "key");
                                V v = valueSerializer.read(indexedSection, "value");
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

    public static <T, M> SettingSerializer<T> ofFieldMapped(Class<T> type, String fieldKey, SettingSerializer<M> fieldSerializer, Map<M, SettingSerializer<? extends T>> mapper) {
        return new FieldMappedSettingSerializer<>(type, fieldKey, fieldSerializer, mapper);
    }
    //endregion

    static ConfigurationSection getOrCreateSection(ConfigurationSection config, String key, String... comments) {
        if (config instanceof CommentedConfigurationSection) {
            CommentedConfigurationSection commentedConfig = (CommentedConfigurationSection) config;
            CommentedConfigurationSection section = commentedConfig.getConfigurationSection(key);
            if (section == null) {
                commentedConfig.addPathedComments(key, comments);
                section = commentedConfig.createSection(key);
            }
            return section;
        } else {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) {
                section = config.createSection(key);
                if (NMSUtil.getVersionNumber() > 18 || (NMSUtil.getVersionNumber() == 18 && NMSUtil.getMinorVersionNumber() >= 1))
                    config.setComments(key, Arrays.asList(comments));
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
