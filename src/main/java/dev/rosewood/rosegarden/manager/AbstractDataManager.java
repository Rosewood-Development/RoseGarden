package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import dev.rosewood.rosegarden.database.MySQLConnector;
import dev.rosewood.rosegarden.database.SQLiteConnector;
import java.util.Map;
import org.bukkit.Bukkit;

public abstract class AbstractDataManager extends Manager {

    protected DatabaseConnector databaseConnector;

    public AbstractDataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public final void reload() {
        try {
            AbstractConfigurationManager configurationManager = this.rosePlugin.getManager(AbstractConfigurationManager.class);
            Map<String, RoseSetting> roseSettings = configurationManager.getSettings();

            if (roseSettings.get("mysql-settings.enabled").getBoolean()) {
                String hostname = roseSettings.get("mysql-settings.hostname").getString();
                int port = roseSettings.get("mysql-settings.port").getInt();
                String database = roseSettings.get("mysql-settings.database-name").getString();
                String username = roseSettings.get("mysql-settings.user-name").getString();
                String password = roseSettings.get("mysql-settings.user-password").getString();
                boolean useSSL = roseSettings.get("mysql-settings.use-ssl").getBoolean();
                int poolSize = roseSettings.get("mysql-settings.connection-pool-size").getInt();

                this.databaseConnector = new MySQLConnector(this.rosePlugin, hostname, port, database, username, password, useSSL, poolSize);
                this.rosePlugin.getLogger().info("Data handler connected using MySQL.");
            } else {
                this.databaseConnector = new SQLiteConnector(this.rosePlugin);
                this.databaseConnector.cleanup();
                this.rosePlugin.getLogger().info("Data handler connected using SQLite.");
            }
        } catch (Exception ex) {
            this.rosePlugin.getLogger().severe("Fatal error trying to connect to database. Please make sure all your connection settings are correct and try again. Plugin has been disabled.");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this.rosePlugin);
        }
    }

    @Override
    public final void disable() {
        if (this.databaseConnector == null)
            return;

        // Wait for all database connections to finish
        long now = System.currentTimeMillis();
        long deadline = now + 5000; // Wait at most 5 seconds
        synchronized (this.databaseConnector.getLock()) {
            while (!this.databaseConnector.isFinished() && now < deadline) {
                try {
                    this.databaseConnector.getLock().wait(deadline - now);
                    now = System.currentTimeMillis();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.databaseConnector.closeConnection();
    }

    /**
     * @return true if the database connection is established, otherwise false
     */
    public final boolean isConnected() {
        return this.databaseConnector != null;
    }

    /**
     * @return The connector to the database
     */
    public final DatabaseConnector getDatabaseConnector() {
        return this.databaseConnector;
    }

    /**
     * @return the prefix to be used by all table names
     */
    public String getTablePrefix() {
        return this.rosePlugin.getDescription().getName().toLowerCase() + '_';
    }

}
