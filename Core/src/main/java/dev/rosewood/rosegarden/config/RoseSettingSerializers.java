package dev.rosewood.rosegarden.config;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bukkit.Material;

public final class RoseSettingSerializers {

    private RoseSettingSerializers() { }

    //region Primitive Serializers
    public static final RoseSettingSerializer<Boolean> BOOLEAN = CommentedConfigurationSection::getBoolean;
    public static final RoseSettingSerializer<Integer> INTEGER = CommentedConfigurationSection::getInt;
    public static final RoseSettingSerializer<Long> LONG = CommentedConfigurationSection::getLong;
    public static final RoseSettingSerializer<Short> SHORT = (config, setting) -> (short) config.getInt(setting);
    public static final RoseSettingSerializer<Byte> BYTE = (config, setting) -> (byte) config.getInt(setting);
    public static final RoseSettingSerializer<Double> DOUBLE = CommentedConfigurationSection::getDouble;
    public static final RoseSettingSerializer<Float> FLOAT = (config, setting) -> (float) config.getDouble(setting);
    public static final RoseSettingSerializer<Character> CHAR = (config, setting) -> {
        String value = config.getString(setting);
        if (value == null || value.isEmpty())
            return ' ';
        return value.charAt(0);
    };
    //endregion

    //region Other Serializers
    public static final RoseSettingSerializer<CommentedConfigurationSection> SECTION = new RoseSettingSerializer<CommentedConfigurationSection>() {
        @Override
        public CommentedConfigurationSection read(CommentedConfigurationSection config, String setting) {
            return config.getConfigurationSection(setting);
        }

        @Override
        public void write(CommentedConfigurationSection config, String key, CommentedConfigurationSection value, String... comments) {
            config.addPathedComments(key, comments);
        }
    };
    public static final RoseSettingSerializer<String> STRING = CommentedConfigurationSection::getString;
    public static final RoseSettingSerializer<Material> MATERIAL = createMapped(STRING, Material::name, Material::matchMaterial);
    //endregion

    //region Primitive List Serializers
    public static final RoseSettingSerializer<List<Boolean>> BOOLEAN_LIST = CommentedConfigurationSection::getBooleanList;
    public static final RoseSettingSerializer<List<Long>> LONG_LIST = CommentedConfigurationSection::getLongList;
    public static final RoseSettingSerializer<List<Short>> SHORT_LIST = CommentedConfigurationSection::getShortList;
    public static final RoseSettingSerializer<List<Byte>> BYTE_LIST = CommentedConfigurationSection::getByteList;
    public static final RoseSettingSerializer<List<Double>> DOUBLE_LIST = CommentedConfigurationSection::getDoubleList;
    public static final RoseSettingSerializer<List<Float>> FLOAT_LIST = CommentedConfigurationSection::getFloatList;
    public static final RoseSettingSerializer<List<Character>> CHAR_LIST = CommentedConfigurationSection::getCharacterList;
    //endregion

    //region Other List Serializers
    public static final RoseSettingSerializer<List<String>> STRING_LIST = CommentedConfigurationSection::getStringList;
    public static final RoseSettingSerializer<List<Material>> MATERIAL_LIST = createMapped(STRING_LIST,
            x -> x.stream().map(Material::name).collect(Collectors.toList()),
            x -> x.stream().map(Material::matchMaterial).filter(Objects::nonNull).collect(Collectors.toList()));
    //endregion

    //region Helpers
    public static <T, M> RoseSettingSerializer<T> createMapped(RoseSettingSerializer<M> baseSerializer, Function<T, M> toMapped, Function<M, T> fromMapped) {
        return new RoseSettingSerializer<T>() {
            @Override
            public T read(CommentedConfigurationSection config, String setting) {
                M value = baseSerializer.read(config, setting);
                if (value == null)
                    return null;
                return fromMapped.apply(value);
            }

            @Override
            public void write(CommentedConfigurationSection config, String key, T value, String... comments) {
                M toWrite = toMapped.apply(value);
                if (toWrite != null)
                    config.set(key, toWrite, comments);
            }
        };
    }
    //endregion

}
