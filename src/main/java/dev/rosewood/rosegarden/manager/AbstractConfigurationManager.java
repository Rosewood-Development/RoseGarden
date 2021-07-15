package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.SingularRoseSetting;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractConfigurationManager extends Manager {

    private static final String[] FOOTER = new String[] {
            "That's everything! You reached the end of the configuration.",
            "Enjoy the plugin!"
    };

    private final Class<? extends RoseSetting> settingEnum;
    private CommentedFileConfiguration configuration;
    private Map<String, RoseSetting> cachedValues;

    public AbstractConfigurationManager(RosePlugin rosePlugin, Class<? extends RoseSetting> settingEnum) {
        super(rosePlugin);

        if (!settingEnum.isEnum())
            throw new IllegalArgumentException("settingEnum class must be of type Enum");

        this.settingEnum = settingEnum;
    }

    @Override
    public final void reload() {
        File configFile = new File(this.rosePlugin.getDataFolder(), "config.yml");
        boolean setHeaderFooter = !configFile.exists();
        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(configFile);

        if (setHeaderFooter)
            this.configuration.addComments(this.getHeader());

        for (RoseSetting setting : this.getSettings().values()) {
            setting.reset();
            changed |= setting.setIfNotExists(this.configuration);
        }

        if (setHeaderFooter)
            this.configuration.addComments(FOOTER);

        if (changed)
            this.configuration.save();
    }

    @Override
    public final void disable() {
        for (RoseSetting setting : this.getSettings().values())
            setting.reset();
    }

    /**
     * @return the header to place at the top of the configuration file
     */
    protected abstract String[] getHeader();

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public final CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    /**
     * @return the values of the setting enum
     */
    public Map<String, RoseSetting> getSettings() {
        if (this.cachedValues == null) {
            try {
                RoseSetting[] roseSettings = (RoseSetting[]) this.settingEnum.getDeclaredMethod("values").invoke(null);
                this.cachedValues = new LinkedHashMap<>();
                for (RoseSetting roseSetting : roseSettings)
                    this.cachedValues.put(roseSetting.getKey(), roseSetting);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
                this.cachedValues = Collections.emptyMap();
            }

            this.injectAdditionalSettings();
        }

        return this.cachedValues;
    }

    /**
     * Injects additional settings into the config
     */
    private void injectAdditionalSettings() {
        Map<String, RoseSetting> values = this.cachedValues;
        this.cachedValues = new LinkedHashMap<>();

        if (this.rosePlugin.hasLocaleManager())
            this.cachedValues.put("locale", new SingularRoseSetting(this.rosePlugin, "locale", "en_US", "The locale to use in the /locale folder"));

        this.cachedValues.putAll(values);

        if (this.rosePlugin.hasDataManager()) {
            Arrays.asList(
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings", null, "Settings for if you want to use MySQL for data management"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.enabled", false, "Enable MySQL", "If false, SQLite will be used instead"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.hostname", "", "MySQL Database Hostname"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.port", 3306, "MySQL Database Port"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.database-name", "", "MySQL Database Name"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.user-name", "", "MySQL Database User Name"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.user-password", "", "MySQL Database User Password"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.use-ssl", false, "If the database connection should use SSL", "You should enable this if your database supports SSL"),
                    new SingularRoseSetting(this.rosePlugin, "mysql-settings.connection-pool-size", 3, "The number of connections to make to the database")
            ).forEach(x -> this.cachedValues.put(x.getKey(), x));
        }
    }

}
