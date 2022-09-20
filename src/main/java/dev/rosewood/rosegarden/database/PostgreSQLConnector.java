package dev.rosewood.rosegarden.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.plugin.Plugin;

public class PostgreSQLConnector implements DatabaseConnector {

    private final Plugin plugin;
    private HikariDataSource hikari;
    private final AtomicInteger openConnections;
    private final Object lock;

    public PostgreSQLConnector(Plugin plugin, String hostname, int port, String database, String username, String password, int poolSize) {
        this.plugin = plugin;
        this.openConnections = new AtomicInteger();
        this.lock = new Object();

        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.setProperty("dataSource.user", username);
        properties.setProperty("dataSource.password", password);
        properties.setProperty("dataSource.databaseName", database);
        properties.setProperty("dataSource.serverName", hostname);
        properties.setProperty("dataSource.portNumber", String.valueOf(port));

        HikariConfig config = new HikariConfig(properties);
        config.setMaximumPoolSize(poolSize);

        try {
            this.hikari = new HikariDataSource(config);
        } catch (Exception ex) {
            this.plugin.getLogger().severe("Failed to connect to the PostgreSQL server. Are your credentials correct?");
            ex.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        this.hikari.close();
    }

    @Override
    public void connect(ConnectionCallback callback) {
        this.openConnections.incrementAndGet();
        try (Connection connection = this.hikari.getConnection()) {
            callback.accept(connection);
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred executing a PostgreSQL query: " + ex.getMessage());
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
        this.connect(callback);
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

    }

}
