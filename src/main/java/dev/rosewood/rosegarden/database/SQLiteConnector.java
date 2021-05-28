package dev.rosewood.rosegarden.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.plugin.Plugin;

public class SQLiteConnector implements DatabaseConnector {

    private final Plugin plugin;
    private final String connectionString;
    private Connection connection;
    private final AtomicInteger openConnections;
    private final Object lock;

    public SQLiteConnector(Plugin plugin) {
        this(plugin, plugin.getDescription().getName().toLowerCase());
    }

    public SQLiteConnector(Plugin plugin, String dbName) {
        this.plugin = plugin;
        this.connectionString = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + dbName + ".db";
        this.openConnections = new AtomicInteger();
        this.lock = new Object();

        try {
            Class.forName("org.sqlite.JDBC"); // Make sure the driver is actually loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        this.connect((connection) -> {
//            connection.createStatement().execute("PRAGMA journal_mode=MEMORY; ");
//            connection.createStatement().execute("PRAGMA temp_store=MEMORY");
//            connection.createStatement().execute("PRAGMA synchronous=NORMAL");
//        });
    }

    @Override
    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred closing the SQLite database connection: " + ex.getMessage());
        }
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(this.connectionString);
                this.connection.setAutoCommit(false);
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred retrieving the SQLite database connection: " + ex.getMessage());
        }

        this.openConnections.incrementAndGet();
        try {
            if (this.connection.getAutoCommit())
                this.connection.setAutoCommit(false);

            callback.accept(this.connection);
            try {
                this.connection.commit();
            } catch (SQLException e) {
                if (e.getMessage() != null && !e.getMessage().contains("transaction"))
                    throw e;

                try {
                    this.connection.rollback();
                } catch (SQLException ignored) { }
            }
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing an SQLite query: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            int open = this.openConnections.decrementAndGet();
            synchronized (this.lock) {
                if (open == 0)
                    this.lock.notify();
            }
        }
    }

    @Override
    public void connect(ConnectionCallback callback, boolean useTransaction) {
        if (useTransaction) {
            this.connect(callback);
            return;
        }

        try {
            if (this.connection == null || this.connection.isClosed())
                this.connection = DriverManager.getConnection(this.connectionString);
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred retrieving the SQLite database connection: " + ex.getMessage());
        }

        this.openConnections.incrementAndGet();
        try {
            if (!this.connection.getAutoCommit())
                this.connection.setAutoCommit(true);

            callback.accept(this.connection);
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing an SQLite query: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            int open = this.openConnections.decrementAndGet();
            synchronized (this.lock) {
                if (open == 0)
                    this.lock.notify();
            }
        }
    }

    @Override
    public Object getLock() {
        return this.lock;
    }

    @Override
    public boolean isFinished() {
        return this.openConnections.get() == 0;
    }

    @Override
    public void cleanup() {
        this.connect(connection -> connection.createStatement().execute("VACUUM"), false);
    }

}

