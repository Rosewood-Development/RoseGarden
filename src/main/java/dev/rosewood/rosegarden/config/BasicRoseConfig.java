package dev.rosewood.rosegarden.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* package */ class BasicRoseConfig implements RoseConfig {

    private final File configurationFile;
    private final List<RoseSetting<?>> settings;
    private final boolean writeDefaultValueComments;
    private CommentedFileConfiguration fileConfiguration;

    private BasicRoseConfig(File file, List<RoseSetting<?>> settings, String[] header, boolean writeDefaultValueComments) {
        this.configurationFile = file;
        this.settings = settings;
        this.writeDefaultValueComments = writeDefaultValueComments;

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

                setting.writeDefault(config, this.writeDefaultValueComments);
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
        private List<RoseSetting<?>> settings;
        private boolean writeDefaultValueComments;

        public Builder(File file) {
            this.file = file;
            this.header = new String[0];
            this.settings = Collections.emptyList();
            this.writeDefaultValueComments = false;
        }

        @Override
        public RoseConfig.Builder header(String... header) {
            this.header = header;
            return this;
        }

        @Override
        public RoseConfig.Builder settings(List<RoseSetting<?>> settings) {
            this.settings = new ArrayList<>(settings);
            return this;
        }

        @Override
        public RoseConfig.Builder writeDefaultValueComments() {
            this.writeDefaultValueComments = true;
            return this;
        }

        @Override
        public RoseConfig build() {
            return new BasicRoseConfig(this.file, this.settings, this.header, this.writeDefaultValueComments);
        }

    }

}
