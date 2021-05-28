package dev.rosewood.rosegarden;

import dev.rosewood.rosegarden.command.RwdCommand;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.manager.DataMigrationManager;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.manager.PluginUpdateManager;
import dev.rosewood.rosegarden.objects.RosePluginData;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RosePlugin extends JavaPlugin {

    /**
     * The RosePlugin identifier
     */
    public static final String ROSEGARDEN_VERSION = "@version@";

    /**
     * The plugin ID on Spigot
     */
    private final int spigotId;

    /**
     * The plugin ID on bStats
     */
    private final int bStatsId;

    /**
     * The classes that extend the abstract managers
     */
    private final Class<? extends AbstractConfigurationManager> configurationManagerClass;
    private final Class<? extends AbstractDataManager> dataManagerClass;
    private final Class<? extends AbstractLocaleManager> localeManagerClass;

    /**
     * The plugin managers
     */
    private final Map<Class<? extends Manager>, Manager> managers;

    public RosePlugin(int spigotId,
                      int bStatsId,
                      Class<? extends AbstractConfigurationManager> configurationManagerClass,
                      Class<? extends AbstractDataManager> dataManagerClass,
                      Class<? extends AbstractLocaleManager> localeManagerClass) {
        if (configurationManagerClass != null && Modifier.isAbstract(configurationManagerClass.getModifiers()))
            throw new IllegalArgumentException("configurationManagerClass cannot be abstract");
        if (dataManagerClass != null && Modifier.isAbstract(dataManagerClass.getModifiers()))
            throw new IllegalArgumentException("dataManagerClass cannot be abstract");
        if (localeManagerClass != null && Modifier.isAbstract(localeManagerClass.getModifiers()))
            throw new IllegalArgumentException("localeManagerClass cannot be abstract");

        this.spigotId = spigotId;
        this.bStatsId = bStatsId;
        this.configurationManagerClass = configurationManagerClass;
        this.dataManagerClass = dataManagerClass;
        this.localeManagerClass = localeManagerClass;

        this.managers = new LinkedHashMap<>();
    }

    @Override
    public final void onEnable() {
        // Log that we are loading
        this.getLogger().info("Initializing using RoseGarden v" + ROSEGARDEN_VERSION);

        // bStats Metrics
        if (this.bStatsId != -1) {
            Metrics metrics = new Metrics(this, this.bStatsId);
            this.addCustomMetricsCharts(metrics);
        }

        // Check if the library is relocated properly
        if (!RoseGardenUtils.isRelocated()) {
            RoseGardenUtils.getLogger().severe("DEVELOPER ERROR!!! RoseGarden has not been relocated! Plugin has been forcefully disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        // Load managers
        this.reload();

        // Run the plugin's enable code
        this.enable();

        // Inject the plugin class into the spigot services manager
        this.injectService();
    }

    @Override
    public final void onDisable() {
        // Run the plugin's disable code
        this.disable();

        // Shut down the managers
        this.disableManagers();
        this.managers.clear();
    }

    /**
     * Called during {@link JavaPlugin#onEnable}
     */
    protected abstract void enable();

    /**
     * Called during {@link JavaPlugin#onDisable}
     */
    protected abstract void disable();

    /**
     * @return the order in which Managers should be loaded
     */
    protected abstract List<Class<? extends Manager>> getManagerLoadPriority();

    /**
     * @return all data migrations for the DataMigrationManager to handle
     */
    public abstract List<Class<? extends DataMigration>> getDataMigrations();

    /**
     * Registers any custom bStats Metrics charts for the plugin
     *
     * @param metrics The Metrics instance
     */
    protected void addCustomMetricsCharts(Metrics metrics) {
        // Must be overridden for any functionality.
    }

    /**
     * Reloads the plugin's managers
     */
    public void reload() {
        this.disableManagers();
        this.managers.values().forEach(Manager::reload);

        List<Class<? extends Manager>> managerLoadPriority = new ArrayList<>();

        if (this.hasConfigurationManager())
            managerLoadPriority.add(this.configurationManagerClass);

        if (this.hasDataManager()) {
            managerLoadPriority.add(this.dataManagerClass);
            managerLoadPriority.add(DataMigrationManager.class);
        }

        if (this.hasLocaleManager())
            managerLoadPriority.add(this.localeManagerClass);

        managerLoadPriority.addAll(this.getManagerLoadPriority());

        if (this.spigotId != -1)
            managerLoadPriority.add(PluginUpdateManager.class);

        managerLoadPriority.forEach(this::getManager);
    }

    /**
     * Runs {@link Manager#disable} on all managers in the reverse order that they were loaded
     */
    private void disableManagers() {
        List<Manager> managers = new ArrayList<>(this.managers.values());
        Collections.reverse(managers);
        managers.forEach(Manager::disable);
    }

    /**
     * Gets a manager instance
     *
     * @param managerClass The class of the manager to get
     * @param <T> extends Manager
     * @return A new or existing instance of the given manager class
     */
    @SuppressWarnings("unchecked")
    public final <T extends Manager> T getManager(Class<T> managerClass) {
        if (this.managers.containsKey(managerClass))
            return (T) this.managers.get(managerClass);

        // Get the actual class if the abstract one is requested
        if (this.hasConfigurationManager() && managerClass == AbstractConfigurationManager.class) {
            return this.getManager((Class<T>) this.configurationManagerClass);
        } else if (this.hasDataManager() && managerClass == AbstractDataManager.class) {
            return this.getManager((Class<T>) this.dataManagerClass);
        } else if (this.hasLocaleManager() && managerClass == AbstractLocaleManager.class) {
            return this.getManager((Class<T>) this.localeManagerClass);
        }

        try {
            T manager = managerClass.getConstructor(RosePlugin.class).newInstance(this);
            this.managers.put(managerClass, manager);
            manager.reload();
            return manager;
        } catch (Exception ex) {
            throw new ManagerNotFoundException(managerClass, ex);
        }
    }

    /**
     * @return the ID of the plugin on Spigot, or -1 if not tracked
     */
    public int getSpigotId() {
        return this.spigotId;
    }

    /**
     * @return the ID of this plugin on bStats, or -1 if not tracked
     */
    public int getBStatsId() {
        return this.bStatsId;
    }

    private void injectService() {
        // Search for other RoseGarden services
        boolean exists = !this.getLoadedRosePluginsData().isEmpty();

        // Register our service
        Bukkit.getServicesManager().register(RosePlugin.class, this, this, ServicePriority.Normal);

        // If we aren't the first then don't continue
        if (exists)
            return;

        // Register /rwd command
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("rosegarden", new RwdCommand(this));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return data of all RosePlugins installed on the server
     */
    public List<RosePluginData> getLoadedRosePluginsData() {
        List<RosePluginData> data = new ArrayList<>();

        ServicesManager servicesManager = Bukkit.getServicesManager();
        for (Class<?> service : servicesManager.getKnownServices()) {
            try {
                String roseGardenVersion = (String) service.getField("ROSEGARDEN_VERSION").get(null);
                Method updateVersionMethod = service.getMethod("getUpdateVersion");

                for (RegisteredServiceProvider<?> provider : servicesManager.getRegistrations(service)) {
                    Plugin plugin = provider.getPlugin();
                    String pluginName = plugin.getName();
                    String pluginVersion = plugin.getDescription().getVersion();
                    String website = plugin.getDescription().getWebsite();
                    String updateVersion = (String) updateVersionMethod.invoke(plugin);
                    data.add(new RosePluginData(pluginName, pluginVersion, updateVersion, website, roseGardenVersion));
                }
            } catch (ReflectiveOperationException | ClassCastException ignored) { }
        }

        return data;
    }

    /**
     * @return the version of the latest update of this plugin, or null if there is none
     */
    public String getUpdateVersion() {
        return this.getManager(PluginUpdateManager.class).getUpdateVersion();
    }

    public boolean hasConfigurationManager() {
        return this.configurationManagerClass != null;
    }

    public boolean hasDataManager() {
        return this.dataManagerClass != null;
    }

    public boolean hasLocaleManager() {
        return this.localeManagerClass != null;
    }

    /**
     * An exception thrown when a Manager fails to load
     */
    private static class ManagerNotFoundException extends RuntimeException {

        public ManagerNotFoundException(Class<? extends Manager> managerClass, Throwable cause) {
            super("Failed to load " + managerClass.getSimpleName(), cause);
        }

    }

}
