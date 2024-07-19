package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import java.io.File;

public class SingularRoseSetting implements RoseSetting {

    private final RosePlugin rosePlugin;
    private final String key;
    private final Object defaultValue;
    private final String[] comments;
    private Object value = null;

    public SingularRoseSetting(RosePlugin rosePlugin, String key, Object defaultValue, String... comments) {
        this.rosePlugin = rosePlugin;
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments != null ? comments : new String[0];
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String[] getComments() {
        return this.comments;
    }

    @Override
    public Object getCachedValue() {
        return this.value;
    }

    @Override
    public void setCachedValue(Object value) {
        this.value = value;
    }

    @Override
    public CommentedFileConfiguration getBaseConfig() {
        File configFile = new File(this.rosePlugin.getDataFolder(), "config.yml");
        return CommentedFileConfiguration.loadConfiguration(configFile);
        //return this.rosePlugin.getManager(AbstractConfigurationManager.class).getConfig();
    }

}
