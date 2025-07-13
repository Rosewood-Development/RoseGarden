package dev.rosewood.rosegarden.config;

import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataType;

public abstract class BaseSettingSerializer<T> implements SettingSerializer<T> {

    protected final Class<T> type;
    protected final Function<T, String> asStringFunction;
    protected final Function<String, T> fromStringFunction;

    public BaseSettingSerializer(Class<T> type, Function<T, String> asStringFunction, Function<String, T> fromStringFunction) {
        this.type = type;
        this.asStringFunction = asStringFunction;
        this.fromStringFunction = fromStringFunction;
    }

    public BaseSettingSerializer(Class<T> type) {
        this(type, null, null);
    }

    /**
     * Writes an object to a ConfigurationSection, including comments
     *
     * @param config The ConfigurationSection to write to
     * @param key The key path to save in the config
     * @param value The value to save
     * @param comments Comments to write above the setting in the config
     */
    public abstract void write(ConfigurationSection config, String key, T value, String... comments);

    /**
     * Reads an object from a ConfigurationSection
     *
     * @param config The ConfigurationSection to read from
     * @param key The path to the value in the config
     * @return the value read
     */
    public abstract T read(ConfigurationSection config, String key);

    @Override
    public final boolean isStringKey() {
        return this.asStringFunction != null && this.fromStringFunction != null;
    }

    @Override
    public final String asStringKey(T key) {
        if (this.asStringFunction == null)
            throw new UnsupportedOperationException("asStringKey not available, check isStringKey() first");
        return this.asStringFunction.apply(key);
    }

    @Override
    public final T fromStringKey(String key) {
        if (this.fromStringFunction == null)
            throw new UnsupportedOperationException("fromStringKey not available, check isStringKey() first");
        return this.fromStringFunction.apply(key);
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    @Override
    public PDCAdapter<T> pdc() {
        return new DelegateAdapter();
    }

    public class DelegateAdapter implements PDCAdapter<T> {

        @Override
        public PDCDelegatingSettingSerializer<T> adapt(PersistentDataType<?, T> pdc) {
            return new PDCDelegatingSettingSerializer<>(BaseSettingSerializer.this, pdc);
        }

    }

}
