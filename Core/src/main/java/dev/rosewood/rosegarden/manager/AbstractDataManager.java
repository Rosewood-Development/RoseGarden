package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.RoseConfig;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.SettingHolder;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import dev.rosewood.rosegarden.database.MySQLConnector;
import dev.rosewood.rosegarden.database.SQLiteConnector;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDataManager extends Manager {

    public static final class Settings implements SettingHolder {
        public static final SettingHolder INSTANCE = new Settings();
        private static final List<RoseSetting<?>> SETTINGS = new ArrayList<>();

        public static final RoseSetting<ConfigurationSection> MYSQL_SETTINGS = define(RoseSetting.forSection("mysql-settings", "Settings for if you want to use MySQL for data management"));
        public static final RoseSetting<Boolean> MYSQL_SETTINGS_ENABLED = define(RoseSetting.forBoolean("mysql-settings.enabled", false, "Enable MySQL", "If false, SQLite will be used instead"));
        public static final RoseSetting<String> MYSQL_SETTINGS_HOSTNAME = define(RoseSetting.forString("mysql-settings.hostname", "127.0.0.1", "MySQL Database Hostname"));
        public static final RoseSetting<Integer> MYSQL_SETTINGS_PORT = define(RoseSetting.forInteger("mysql-settings.port", 3306, "MySQL Database Port"));
        public static final RoseSetting<String> MYSQL_SETTINGS_DATABASE = define(RoseSetting.forString("mysql-settings.database-name", "", "MySQL Database Name"));
        public static final RoseSetting<String> MYSQL_SETTINGS_USERNAME = define(RoseSetting.forString("mysql-settings.user-name", "", "MySQL Database User Name"));
        public static final RoseSetting<String> MYSQL_SETTINGS_PASSWORD = define(RoseSetting.forString("mysql-settings.user-password", "", "MySQL Database User Password"));
        public static final RoseSetting<Boolean> MYSQL_SETTINGS_USE_SSL = define(RoseSetting.forBoolean("mysql-settings.use-ssl", false, "If the database connection should use SSL", "You should enable this if your database supports SSL"));
        public static final RoseSetting<Integer> MYSQL_SETTINGS_POOL_SIZE = define(RoseSetting.forInteger("mysql-settings.connection-pool-size", 3, "The number of connections to make to the database"));

        private Settings() {}

        @Override
        public List<RoseSetting<?>> get() {
            return Collections.unmodifiableList(SETTINGS);
        }

        private static <T> RoseSetting<T> define(RoseSetting<T> setting) {
            SETTINGS.add(setting);
            return setting;
        }
    }

    protected DatabaseConnector databaseConnector;

    public AbstractDataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        try {
            RoseConfig roseConfig = this.rosePlugin.getRoseConfig();
            if (roseConfig.get(Settings.MYSQL_SETTINGS_ENABLED)) {
                String hostname = roseConfig.get(Settings.MYSQL_SETTINGS_HOSTNAME);
                int port = roseConfig.get(Settings.MYSQL_SETTINGS_PORT);
                String database = roseConfig.get(Settings.MYSQL_SETTINGS_DATABASE);
                String username = roseConfig.get(Settings.MYSQL_SETTINGS_USERNAME);
                String password = roseConfig.get(Settings.MYSQL_SETTINGS_PASSWORD);
                boolean useSSL = roseConfig.get(Settings.MYSQL_SETTINGS_USE_SSL);
                int poolSize = roseConfig.get(Settings.MYSQL_SETTINGS_POOL_SIZE);

                this.databaseConnector = new MySQLConnector(this.rosePlugin, hostname, port, database, username, password, useSSL, poolSize);
                this.rosePlugin.getLogger().info("Data handler connected using MySQL.");
            } else {
                this.databaseConnector = new SQLiteConnector(this.rosePlugin);
                this.databaseConnector.cleanup();
                this.rosePlugin.getLogger().info("Data handler connected using SQLite.");
            }

            this.applyMigrations();
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
    public abstract List<Supplier<? extends DataMigration>> getDataMigrations();

    /**
     * Applies the DataMigrations defined by {@link #getDataMigrations()}.
     */
    private void applyMigrations() {
        List<DataMigration> migrations = this.getDataMigrations().stream()
                .map(Supplier::get)
                .collect(Collectors.toList());

        DatabaseConnector databaseConnector = this.getDatabaseConnector();
        databaseConnector.connect((connection -> {
            int currentMigration = -1;
            boolean migrationsExist;

            String query;
            if (databaseConnector instanceof SQLiteConnector) {
                query = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
            } else {
                query = "SHOW TABLES LIKE ?";
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, this.getMigrationsTableName());
                migrationsExist = statement.executeQuery().next();
            }

            if (!migrationsExist) {
                // No migration table exists, create one
                String createTable = "CREATE TABLE " + this.getMigrationsTableName() + " (migration_version INT NOT NULL)";
                try (PreparedStatement statement = connection.prepareStatement(createTable)) {
                    statement.execute();
                }

                // Insert primary row into migration table
                String insertRow = "INSERT INTO " + this.getMigrationsTableName() + " VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
                    statement.setInt(1, -1);
                    statement.execute();
                }
            } else {
                // Grab the current migration version
                String selectVersion = "SELECT migration_version FROM " + this.getMigrationsTableName();
                boolean badState = false;
                try (PreparedStatement statement = connection.prepareStatement(selectVersion)) {
                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        currentMigration = result.getInt("migration_version");
                    } else {
                        badState = true;
                    }
                }

                if (badState) {
                    RoseGardenUtils.getLogger().severe("Database migration table is missing the migration_version row! " +
                            "The database is currently in a bad state due to an unknown issue. Attempting to fix the migration " +
                            "column automatically... please contact the plugin developer for assistance if this does not work.");

                    // Find the highest migration version
                    currentMigration = migrations.stream()
                            .mapToInt(DataMigration::getRevision)
                            .max()
                            .orElse(-1);

                    // Insert a new row into the migration table, assume the highest migration is already applied in an
                    // attempt to prevent getting into an even worse state
                    String insertRow = "INSERT INTO " + this.getMigrationsTableName() + " VALUES (?)";
                    try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
                        statement.setInt(1, currentMigration);
                        statement.execute();
                    }
                }
            }

            // Grab required migrations
            int finalCurrentMigration = currentMigration;
            List<DataMigration> requiredMigrations = migrations.stream()
                    .filter(x -> x.getRevision() > finalCurrentMigration)
                    .sorted(Comparator.comparingInt(DataMigration::getRevision))
                    .collect(Collectors.toList());

            // Nothing to migrate, abort
            if (requiredMigrations.isEmpty())
                return;

            // Migrate the data
            for (DataMigration dataMigration : requiredMigrations)
                dataMigration.migrate(databaseConnector, connection, this.getTablePrefix());

            // Set the new current migration to be the highest migrated to
            currentMigration = requiredMigrations
                    .stream()
                    .mapToInt(DataMigration::getRevision)
                    .max()
                    .orElse(-1);

            String updateVersion = "UPDATE " + this.getMigrationsTableName() + " SET migration_version = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateVersion)) {
                statement.setInt(1, currentMigration);
                statement.execute();
            }
        }));
    }

    /**
     * @return the name of the migrations table
     */
    private String getMigrationsTableName() {
        return this.rosePlugin.getManager(AbstractDataManager.class).getTablePrefix() + "migrations";
    }

}
