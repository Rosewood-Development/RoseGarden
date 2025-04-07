package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.datatype.CustomPersistentDataType;
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
import org.bukkit.persistence.PersistentDataContainer;
import static dev.rosewood.rosegarden.config.SettingSerializers.setWithComments;

@SuppressWarnings("unchecked")
final class SettingSerializerFactories {

    private SettingSerializerFactories() { }

    //region Serializer Factories
    public static <T extends Enum<T>> SettingSerializer<T> ofEnum(Class<T> enumClass) {
        return new SettingSerializer<T>(enumClass, CustomPersistentDataType.forEnum(enumClass), x -> x.name().toLowerCase(), x -> Enum.valueOf(enumClass, x.toUpperCase())) {
            public void write(ConfigurationSection config, String key, T value, String... comments) { setWithComments(config, key, value.name().toLowerCase(), comments); }
            public T read(ConfigurationSection config, String key) { return Enum.valueOf(enumClass, config.getString(key, "").toUpperCase()); }
        };
    }

    public static <T extends Keyed> SettingSerializer<T> ofKeyed(Class<T> keyedClass, Function<NamespacedKey, T> valueOfFunction) {
        return new SettingSerializer<T>(keyedClass, CustomPersistentDataType.forKeyed(keyedClass, valueOfFunction), x -> translateName(x.getKey()), x -> valueOfFunction.apply(translateKey(x))) {
            public void write(ConfigurationSection config, String key, T value, String... comments) { setWithComments(config, key, translateName(value.getKey()), comments); }
            public T read(ConfigurationSection config, String key) { return valueOfFunction.apply(translateKey(config.getString(key))); }
        };
    }

    public static <T> SettingSerializer<T[]> ofArray(SettingSerializer<T> serializer) {
        return new SettingSerializer<T[]>(CustomPersistentDataType.forArray(serializer.persistentDataType)) {
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
                if (serializer.isStringKey()) {
                    List<String> contents = config.getStringList(key);
                    T[] array = (T[]) Array.newInstance(serializer.type, contents.size());
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
                        T[] array = (T[]) Array.newInstance(serializer.type, contents.size());
                        for (String configKey : section.getKeys(false)) {
                            int index = Integer.parseInt(configKey);
                            String content = section.getString(configKey, "");
                            if (!content.isEmpty())
                                array[index] = serializer.read(section, configKey);
                        }
                    }
                    return (T[]) Array.newInstance(serializer.type, 0);
                }
            }
            protected String getDefaultCommentText(T[] values) {
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
        return new SettingSerializer<List<T>>(CustomPersistentDataType.forList(serializer.persistentDataType)) {
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
                if (serializer.isStringKey()) {
                    return config.getStringList(key).stream().map(serializer::fromStringKey).collect(Collectors.toList());
                } else {
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
            }
            protected String getDefaultCommentText(List<T> values) {
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
        return new SettingSerializer<Map<K, V>>(CustomPersistentDataType.forMap(keySerializer.persistentDataType, valueSerializer.persistentDataType)) {
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
        return new SettingSerializer<T>(type, null) {
            @Override
            public void write(ConfigurationSection config, String key, T value, String... comments) {
                SettingSerializer<T> serializer = this.mapField(fieldSerializer.read(config, fieldKey));
                if (serializer != null)
                    serializer.write(config, key, value, comments);
            }

            @Override
            public void writeWithDefault(ConfigurationSection config, String key, T value, String... comments) {
                SettingSerializer<T> serializer = this.mapField(fieldSerializer.read(config, fieldKey));
                if (serializer != null)
                    serializer.writeWithDefault(config, key, value, comments);
            }

            @Override
            public void write(PersistentDataContainer container, String key, T value) {
                SettingSerializer<T> serializer = this.mapField(fieldSerializer.read(container, fieldKey));
                if (serializer != null)
                    serializer.write(container, key, value);
            }

            @Override
            public T read(ConfigurationSection config, String key) {
                SettingSerializer<T> serializer = this.mapField(fieldSerializer.read(config, fieldKey));
                return serializer != null ? serializer.read(config, key) : null;
            }

            @Override
            public T read(PersistentDataContainer container, String key) {
                SettingSerializer<T> serializer = this.mapField(fieldSerializer.read(container, fieldKey));
                return serializer != null ? serializer.read(container, key) : null;
            }

            @SuppressWarnings("unchecked") // always maps to subtypes since it extends T
            private SettingSerializer<T> mapField(M value) {
                if (value == null)
                    return null;
                SettingSerializer<? extends T> serializer = mapper.get(value);
                return (SettingSerializer<T>) serializer;
            }
        };
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
