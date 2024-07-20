package dev.rosewood.rosegarden.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicRoseConfig implements RoseConfig {

    private final File configurationFile;
    private final List<RoseSetting<?>> settings;
    private CommentedFileConfiguration fileConfiguration;

    private BasicRoseConfig(File file, List<RoseSetting<?>> settings, String[] header) {
        this.configurationFile = file;
        this.settings = settings;

        if (this.settings.isEmpty() && header.length == 0)
            return;

        boolean appendHeader = !file.exists();
        boolean changed = appendHeader;

        if (!file.exists()) {
            CommentedFileConfiguration config = this.getBaseConfig();

            if (appendHeader)
                config.addComments(header);

            for (RoseSetting<?> setting : settings) {
                if (config.contains(setting.getKey()))
                    continue;

                setting.writeDefault(config);
                changed = true;
            }
        }

        if (changed)
            this.save();
    }

    @Override
    public <T> T get(RoseSetting<T> setting) {
        return setting.getSerializer().read(this.getBaseConfig(), setting);
    }

    @Override
    public <T> void set(RoseSetting<T> setting, T value) {
        setting.getSerializer().write(this.getBaseConfig(), setting, value);
    }

    @Override
    public File getFile() {
        return this.configurationFile;
    }

    @Override
    public CommentedFileConfiguration getBaseConfig() {
        if (this.fileConfiguration == null)
            this.fileConfiguration = CommentedFileConfiguration.loadConfiguration(this.configurationFile);
        return this.fileConfiguration;
    }

    @Override
    public List<RoseSetting<?>> getSettings() {
        return Collections.unmodifiableList(this.settings);
    }

    public static class Builder implements RoseConfig.Builder {

        private final File file;
        private String[] header;
        private final List<RoseSetting<?>> settings;

        public Builder(File file) {
            this.file = file;
            this.settings = new ArrayList<>();
        }

        @Override
        public RoseConfig.Builder header(String... header) {
            this.header = header;
            return this;
        }

        @Override
        public RoseConfig.Builder settings(List<RoseSetting<?>> settings) {
            this.settings.addAll(settings);
            return this;
        }

        @Override
        public RoseConfig build() {
            return new BasicRoseConfig(this.file, this.settings, this.header);
        }

    }

}
