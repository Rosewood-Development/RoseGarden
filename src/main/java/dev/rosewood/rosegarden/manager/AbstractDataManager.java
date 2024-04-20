package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import dev.rosewood.rosegarden.database.MySQLConnector;
import dev.rosewood.rosegarden.database.SQLiteConnector;
import java.util.List;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDataManager extends Manager {

    protected DatabaseConnector databaseConnector;

    public AbstractDataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        try {
            AbstractConfigurationManager configurationManager = this.rosePlugin.getManager(AbstractConfigurationManager.class);
            CommentedFileConfiguration roseSettings = configurationManager.getConfig();

            if (roseSettings.getBoolean("mysql-settings.enabled")) {
                String hostname = roseSettings.getString("mysql-settings.hostname");
                int port = roseSettings.getInt("mysql-settings.port");
                String database = roseSettings.getString("mysql-settings.database-name");
                String username = roseSettings.getString("mysql-settings.user-name");
                String password = roseSettings.getString("mysql-settings.user-password");
                boolean useSSL = roseSettings.getBoolean("mysql-settings.use-ssl");
                int poolSize = roseSettings.getInt("mysql-settings.connection-pool-size");

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
    public void disable() {
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
    @NotNull
    public final DatabaseConnector getDatabaseConnector() {
        if (this.databaseConnector == null)
            throw new IllegalStateException("A database connection could not be established.");
        return this.databaseConnector;
    }

    /**
     * @return the prefix to be used by all table names
     */
    @NotNull
    public String getTablePrefix() {
        return this.rosePlugin.getDescription().getName().toLowerCase() + '_';
    }

    /**
     * @return all data migrations for the DataMigrationManager to handle
     */
    @NotNull
    public abstract List<Class<? extends DataMigration>> getDataMigrations();

}
