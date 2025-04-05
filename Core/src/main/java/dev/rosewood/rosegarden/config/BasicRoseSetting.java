package dev.rosewood.rosegarden.config;

import java.util.Arrays;
import java.util.function.Supplier;
import org.bukkit.configuration.ConfigurationSection;

class BasicRoseSetting<T> implements RoseSetting<T> {

    protected final SettingSerializer<T> serializer;
    protected final String key;
    protected Supplier<T> defaultValueSupplier;
    protected final String[] comments;
    private final boolean hidden;

    protected BasicRoseSetting(SettingSerializer<T> serializer, String key, Supplier<T> defaultValueSupplier, boolean hidden, String... comments) {
        this.serializer = serializer;
        this.key = key;
        this.defaultValueSupplier = defaultValueSupplier;
        this.hidden = hidden;
        this.comments = comments;
    }

    @Override
    public void write(CommentedConfigurationSection config) {
        if (this.hidden)
            return;

        T defaultValue = this.defaultValueSupplier.get();
        this.serializer.write(config, this.key, defaultValue, this.comments);
    }

    @Override
    public void writeWithDefault(CommentedConfigurationSection config) {
        if (this.hidden)
            return;

        T defaultValue = this.defaultValueSupplier.get();
        this.serializer.writeWithDefault(config, this.key, defaultValue, this.comments);
    }

    @Override
    public void readDefault(ConfigurationSection config) {
        if (this.hidden || !config.contains(this.key))
            return;

        this.defaultValueSupplier = () -> this.serializer.read(config, this.key);
    }

    @Override
    public T read(ConfigurationSection config) {
        if (this.hidden || !config.contains(this.key))
            return null;

        return this.serializer.read(config, this.key);
    }

    @Override
    public SettingSerializer<T> getSerializer() {
        return this.serializer;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public T getDefaultValue() {
        return this.defaultValueSupplier.get();
    }

    @Override
    public String[] getComments() {
        return this.comments;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public RoseSetting<T> copy(T defaultValue, String... comments) {
        return this.copy(() -> defaultValue, comments);
    }

    @Override
    public RoseSetting<T> copy(Supplier<T> defaultValueSupplier, String... comments) {
        String[] newComments;
        if (comments == null) {
            newComments = new String[0];
        } else {
            newComments = Arrays.copyOf(this.comments, this.comments.length);
        }
        return new BasicRoseSetting<>(this.serializer, this.key, defaultValueSupplier, false, newComments);
    }

    @Override
    public String toString() {
        return "BasicRoseSetting{" +
                "key='" + this.key + '\'' +
                ", defaultValue=" + this.defaultValueSupplier.get() +
                '}';
    }

}
