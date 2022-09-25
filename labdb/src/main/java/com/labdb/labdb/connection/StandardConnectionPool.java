package com.labdb.labdb.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StandardConnectionPool implements ConnectionPool {
    private final String url;
    private final String user;
    private final String password;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 6;
    private static final int MAX_POOL_SIZE = 20;
    private static final int MAX_TIMEOUT = 3;


    public static StandardConnectionPool create(
            String url, String user, String password) throws SQLException {

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, user, password));
        }

        return new StandardConnectionPool(url, user, password, pool);
    }

    private StandardConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password));
            } else {
                throw new SQLException("Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);

        connection = prepareConnection(connection);

        usedConnections.add(connection);
        return connection;
    }

    private Connection prepareConnection(Connection connection) throws SQLException {
        try {
            if (!connection.isValid(MAX_TIMEOUT)) {
                connection = createConnection(url, user, password);
            }
            connection.setAutoCommit(true);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        } catch (SQLException e) {
            throw new SQLException("Connection validation failed", e);
        }
        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        connectionPool.add(connection);
        usedConnections.remove(connection);
    }

    @Override
    public List<Connection> getConnectionPool() {
        return this.connectionPool;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }

    private static Connection createConnection(
            String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

}
