package ru.nsu.dbb.entities;

import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session {
    private Connection connection;
    private Statement saveStatement;
    private DatabaseMetaData meta;

    public Session(ConnectionInfo info) {
        try {
            connection = connect(info);
        } catch (SQLException e) {
            throw new RuntimeException("Problems with connection");
        }
    }

    public Connection connect(ConnectionInfo info) throws SQLException {
        Connection connection = null;
        switch (info.getConnectionType()) {
            case SQLITE -> {
                SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
                sqLiteDataSource.setUrl(info.getProperties().get("url"));
                connection = sqLiteDataSource.getConnection();
            }
            case POSTGRESQL -> {
                PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                Map<String, String> props = info.getProperties();
                pgSimpleDataSource.setUrl(props.get("url"));
                pgSimpleDataSource.setUser(props.get("username"));
                pgSimpleDataSource.setPassword(props.get("password"));
                connection = pgSimpleDataSource.getConnection();
            }
        }
        connection.setAutoCommit(false);
        saveStatement = connection.createStatement();
        meta = connection.getMetaData();
        return connection;
    }

    // Connection functions
    public void reconnect(ConnectionInfo info) {
        try {
            disconnect();
            connection = connect(info);
        } catch (SQLException e) {
            throw new RuntimeException("Problems with connection");
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problems with disconnection");
        }
    }

    public boolean isConnected() {
        try {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    // Statements functions
    // Это использовать для заранне подготовленных запросов
    public PreparedStatement getPreparedStatement(String sql, int resultSetType, int resultSetConcurrency) {
        try {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Для запросов пользователя
    public Statement getStatement() {
        try {
            return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSaveStatement(String sql) {
        try {
            saveStatement.addBatch(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Something wrong happened while attempting to add new batch");
        }
    }

    public void saveChanges() {
        try {
            saveStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Can't save changes");
        }
    }

    public void discardChanges() {
        try {
            saveStatement.clearBatch();
            // тут нужно пройтись по всем элементам ниже схемы у которых стоит флаг,
            // что их изменения не сохранены, и удалить их
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Can't discard changes");
        }
    }

    // Working with meta functions
    public List<Database> getDatabases() {
        try {
            List<Database> databaseList = new ArrayList<>();
            ResultSet resultSet = meta.getCatalogs();
            while (resultSet.next()) {
                databaseList.add(new Database(resultSet.getString("TABLE_CAT")));
            }
            if (databaseList.isEmpty()) {
                databaseList.add(new Database("Database"));
            }
            return databaseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
