package dev.rosewood.rosegarden.config;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bukkit.Material;

public final class RoseSettingSerializers {

    private RoseSettingSerializers() { }

    //region Primitive Serializers
    public static final RoseSettingSerializer<Boolean> BOOLEAN = (config, setting) -> config.getBoolean(setting.getKey());
    public static final RoseSettingSerializer<Integer> INTEGER = (config, setting) -> config.getInt(setting.getKey());
    public static final RoseSettingSerializer<Long> LONG = (config, setting) -> config.getLong(setting.getKey());
    public static final RoseSettingSerializer<Short> SHORT = (config, setting) -> (short) config.getInt(setting.getKey());
    public static final RoseSettingSerializer<Byte> BYTE = (config, setting) -> (byte) config.getInt(setting.getKey());
    public static final RoseSettingSerializer<Double> DOUBLE = (config, setting) -> config.getDouble(setting.getKey());
    public static final RoseSettingSerializer<Float> FLOAT = (config, setting) -> (float) config.getDouble(setting.getKey());
    public static final RoseSettingSerializer<Character> CHAR = (config, setting) -> {
        String value = config.getString(setting.getKey());
        if (value == null || value.isEmpty())
            return ' ';
        return value.charAt(0);
    };
    //endregion

    //region Other Serializers
    public static final RoseSettingSerializer<CommentedConfigurationSection> SECTION = new RoseSettingSerializer<CommentedConfigurationSection>() {
        @Override
        public CommentedConfigurationSection read(CommentedConfigurationSection config, RoseSetting<CommentedConfigurationSection> setting) {
            return config.getConfigurationSection(setting.getKey());
        }

        @Override
        public void write(CommentedConfigurationSection config, String key, CommentedConfigurationSection value, String... comments) {
            config.addPathedComments(key, comments);
        }
    };
    public static final RoseSettingSerializer<String> STRING = (config, setting) -> config.getString(setting.getKey());
    public static final RoseSettingSerializer<Material> MATERIAL = createStringBased(Material::name, Material::matchMaterial);
    //endregion

    //region Primitive List Serializers
    public static final RoseSettingSerializer<List<Boolean>> BOOLEAN_LIST = (config, setting) -> config.getBooleanList(setting.getKey());
    public static final RoseSettingSerializer<List<Long>> LONG_LIST = (config, setting) -> config.getLongList(setting.getKey());
    public static final RoseSettingSerializer<List<Short>> SHORT_LIST = (config, setting) -> config.getShortList(setting.getKey());
    public static final RoseSettingSerializer<List<Byte>> BYTE_LIST = (config, setting) -> config.getByteList(setting.getKey());
    public static final RoseSettingSerializer<List<Double>> DOUBLE_LIST = (config, setting) -> config.getDoubleList(setting.getKey());
    public static final RoseSettingSerializer<List<Float>> FLOAT_LIST = (config, setting) -> config.getFloatList(setting.getKey());
    public static final RoseSettingSerializer<List<Character>> CHAR_LIST = (config, setting) -> config.getCharacterList(setting.getKey());
    //endregion

    //region Other List Serializers
    public static final RoseSettingSerializer<List<String>> STRING_LIST = (config, setting) -> config.getStringList(setting.getKey());
    public static final RoseSettingSerializer<List<Material>> MATERIAL_LIST = createStringBasedList(Material::name, Material::matchMaterial);
    //endregion

    //region Helpers
    private static <T> RoseSettingSerializer<T> createStringBased(Function<T, String> toString, Function<String, T> fromString) {
        return new RoseSettingSerializer<T>() {
            @Override
            public T read(CommentedConfigurationSection config, RoseSetting<T> setting) {
                String value = config.getString(setting.getKey());
                if (value == null)
                    return null;
                return fromString.apply(value);
            }

            @Override
            public void write(CommentedConfigurationSection config, String key, T value, String... comments) {
                String toWrite = toString.apply(value);
                if (toWrite != null)
                    config.set(key, toWrite, comments);
            }
        };
    }

    private static <T> RoseSettingSerializer<List<T>> createStringBasedList(Function<T, String> toString, Function<String, T> fromString) {
        return new RoseSettingSerializer<List<T>>() {
            @Override
            public List<T> read(CommentedConfigurationSection config, RoseSetting<List<T>> setting) {
                return config.getStringList(setting.getKey()).stream()
                        .map(fromString)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            @Override
            public void write(CommentedConfigurationSection config, String key, List<T> values, String... comments) {
                List<String> toWrite = values.stream().map(toString)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                config.set(key, toWrite, comments);
            }
        };
    }
    //endregion

}
