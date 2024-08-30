package dev.rosewood.rosegarden.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* package */ class BasicRoseConfig implements RoseConfig {

    private final File file;
    private final List<RoseSetting<?>> settings;
    private final Map<RoseSetting<?>, Object> settingsValueCache;
    private final String[] header;
    private final boolean writeDefaultValueComments;
    private CommentedFileConfiguration fileConfiguration;

    private BasicRoseConfig(File file, List<RoseSetting<?>> settings, String[] header, boolean writeDefaultValueComments) {
        this.file = file;
        this.settings = settings;
        this.settingsValueCache = new HashMap<>(Math.min(16, this.settings.size()));
        this.header = header;
        this.writeDefaultValueComments = writeDefaultValueComments;
        this.reload();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(RoseSetting<T> setting) {
        if (this.settingsValueCache.containsKey(setting))
            return (T) this.settingsValueCache.get(setting);

        try {
            T value = setting.getSerializer().read(this.getBaseConfig(), setting);
            this.settingsValueCache.put(setting, value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return setting.getDefaultValue();
        }
    }

    @Override
    public <T> void set(RoseSetting<T> setting, T value) {
        setting.getSerializer().write(this.getBaseConfig(), setting, value);
        this.settingsValueCache.put(setting, value);
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public CommentedFileConfiguration getBaseConfig() {
        if (this.fileConfiguration == null)
            this.fileConfiguration = CommentedFileConfiguration.loadConfiguration(this.file);
        return this.fileConfiguration;
    }

    @Override
    public void reload() {
        this.fileConfiguration = null;
        this.settingsValueCache.clear();
        if (this.settings.isEmpty() && this.header.length == 0)
            return;

        boolean appendHeader = !this.file.exists();
        boolean changed = appendHeader;

        CommentedFileConfiguration config = this.getBaseConfig();
        if (appendHeader)
            config.addComments(this.header);

        for (RoseSetting<?> setting : this.settings) {
            if (config.contains(setting.getKey()))
                continue;

            setting.writeDefault(config, this.writeDefaultValueComments);
            changed = true;
        }

        if (changed)
            this.save();
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
