package dev.rosewood.rosegarden.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {

    /**
     * Closes all open connections to the database
     */
    void closeConnection();

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished
     *
     * @param callback The callback to execute once the connection is retrieved
     */
    void connect(ConnectionCallback callback);

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished
     *
     * @param callback The callback to execute once the connection is retrieved
     * @param useTransaction If true, the query will be executed within a transaction. Not all connectors will use this.
     */
    void connect(ConnectionCallback callback, boolean useTransaction);

    /**
     * Aquires a database connection.
     * You must close this connection by calling {@link Connection#close()}.
     *
     * @return The database connection.
     */
    Connection connect() throws SQLException;

    /**
     * @return the lock to notify when all connections have been finalized
     */
    Object getLock();

    /**
     * @return true if all connections have finished, otherwise false
     */
    boolean isFinished();

    /**
     * Cleans up the database data, if applicable
     */
    void cleanup();

    /**
     * Wraps a connection in a callback which will automagically handle catching sql errors
     */
    interface ConnectionCallback {
        void accept(Connection connection) throws SQLException;
    }

}
