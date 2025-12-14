package dev.rosewood.rosegarden.codec.yaml;

import dev.rosewood.rosegarden.codec.CodecType;
import org.bukkit.configuration.ConfigurationSection;

public class YamlCodecType implements CodecType<ConfigurationSection> {

    public static final YamlCodecType INSTANCE = new YamlCodecType();

    private YamlCodecType() {

    }

    @Override
    public Class<ConfigurationSection> getContainerType() {
        return ConfigurationSection.class;
    }

    @Override
    public String toString() {
        return "YamlCodec";
    }

}
