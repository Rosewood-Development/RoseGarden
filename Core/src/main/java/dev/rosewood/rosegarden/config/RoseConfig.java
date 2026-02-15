package dev.rosewood.rosegarden.config;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

public interface RoseConfig {

    List<RoseSetting<?>> getSettings();

    <T> T get(RoseSetting<T> setting);

    <T> void set(RoseSetting<T> setting, T value);

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
