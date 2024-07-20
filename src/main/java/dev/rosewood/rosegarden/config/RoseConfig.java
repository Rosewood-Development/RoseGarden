package dev.rosewood.rosegarden.config;

import java.io.File;
import java.util.List;

public interface RoseConfig {

    List<RoseSetting<?>> getSettings();

    <T> T get(RoseSetting<T> setting);

    default <T> T get(String key, RoseSettingSerializer<T> serializer) {
        return this.get(key, serializer, null);
    }

    default <T> T get(String key, RoseSettingSerializer<T> serializer, T defaultValue) {
        return this.get(RoseSetting.of(key, serializer, defaultValue));
    }

    <T> void set(RoseSetting<T> setting, T value);

    default <T> void set(String key, T value, RoseSettingSerializer<T> serializer) {
        this.set(RoseSetting.of(key, serializer, value), value);
    }

    File getFile();

    CommentedFileConfiguration getBaseConfig();

    default void save() {
        this.getBaseConfig().save(this.getFile());
    }

    static Builder builder(File file) {
        return new BasicRoseConfig.Builder(file);
    }

    interface Builder {

        Builder header(String... header);

        Builder settings(List<RoseSetting<?>> settings);

        Builder writeDefaultValueComments();

        RoseConfig build();

    }

}
