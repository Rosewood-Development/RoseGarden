package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import dev.rosewood.rosegarden.database.SQLiteConnector;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataMigrationManager extends Manager {

    private final List<DataMigration> migrations;

    public DataMigrationManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.migrations = new ArrayList<>();
        for (Class<? extends DataMigration> dataMigrationClass : rosePlugin.getManager(AbstractDataManager.class).getDataMigrations()) {
            try {
                this.migrations.add(dataMigrationClass.getConstructor().newInstance());
            } catch (NoSuchMethodException ex) {
                RoseGardenUtils.getLogger().severe("DEVELOPER ERROR!!! DataMigration (" + dataMigrationClass.getSimpleName() + ") is missing a parameterless constructor!" +
                        "This is likely going to cause database issues, as this migration will not be registered!");
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void reload() {
        AbstractDataManager dataManager = this.rosePlugin.getManager(AbstractDataManager.class);
        DatabaseConnector databaseConnector = dataManager.getDatabaseConnector();

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
                    currentMigration = this.migrations
                            .stream()
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
            List<DataMigration> requiredMigrations = this.migrations
                    .stream()
                    .filter(x -> x.getRevision() > finalCurrentMigration)
                    .sorted(Comparator.comparingInt(DataMigration::getRevision))
                    .collect(Collectors.toList());

            // Nothing to migrate, abort
            if (requiredMigrations.isEmpty())
                return;

            // Migrate the data
            for (DataMigration dataMigration : requiredMigrations)
                dataMigration.migrate(databaseConnector, connection, dataManager.getTablePrefix());

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

    @Override
    public void disable() {

    }

    /**
     * @return the name of the migrations table
     */
    private String getMigrationsTableName() {
        return this.rosePlugin.getManager(AbstractDataManager.class).getTablePrefix() + "migrations";
    }

}
