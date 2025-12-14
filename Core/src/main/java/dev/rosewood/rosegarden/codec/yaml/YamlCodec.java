package dev.rosewood.rosegarden.codec.yaml;

import dev.rosewood.rosegarden.codec.BaseSettingCodec;
import dev.rosewood.rosegarden.codec.SettingType;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlCodec<T> extends BaseSettingCodec<ConfigurationSection, T> {

    public YamlCodec(SettingType<T> type, Function<T, String> encodeStringFunction, Function<String, T> decodeStringFunction) {
        super(type, encodeStringFunction, decodeStringFunction);
    }

    public YamlCodec(SettingType<T> type) {
        super(type, null, null);
    }

    public YamlCodec(Class<T> type, Function<T, String> encodeStringFunction, Function<String, T> decodeStringFunction) {
        super(type, encodeStringFunction, decodeStringFunction);
    }

    public YamlCodec(Class<T> type) {
        super(type);
    }

    @Override
    public Class<ConfigurationSection> getContainerType() {
        return ConfigurationSection.class;
    }

    @Override
    public boolean isPresent(ConfigurationSection container, String key) {
        return container.contains(key);
    }

    @Override
    public boolean isValid(ConfigurationSection container, String key) {
        return this.isPresent(container, key);
    }

}
