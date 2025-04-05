package dev.rosewood.rosegarden.config;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

public interface RoseConfig {

    List<RoseSetting<?>> getSettings();

    <T> T get(RoseSetting<T> setting);

    default <T> T get(String key, SettingSerializer<T> serializer) {
        return this.get(key, serializer, (T) null);
    }

    default <T> T get(String key, SettingSerializer<T> serializer, T defaultValue) {
        return this.get(RoseSetting.ofValue(key, serializer, defaultValue));
    }

    default <T> T get(String key, SettingSerializer<T> serializer, Supplier<T> defaultValueSupplier) {
        return this.get(RoseSetting.of(key, serializer, defaultValueSupplier));
    }

    <T> void set(RoseSetting<T> setting, T value);

    default <T> void set(String key, SettingSerializer<T> serializer, T value) {
        this.set(RoseSetting.ofValue(key, serializer, value), value);
    }

    default <T> void set(String key, SettingSerializer<T> serializer, Supplier<T> valueSupplier) {
        this.set(RoseSetting.of(key, serializer, valueSupplier), valueSupplier.get());
    }

    File getFile();

    CommentedFileConfiguration getBaseConfig();

    void reload();

    default void save() {
        this.getBaseConfig().save(this.getFile());
    }

    static Builder builder(File file) {
        return new BasicRoseConfig.Builder(file);
    }

    interface Builder {

        Builder header(String... header);

        Builder settings(List<RoseSetting<?>> settings);

        Builder settings(SettingHolder settingHolder);

        Builder writeDefaultValueComments();

        RoseConfig build();

    }

}
