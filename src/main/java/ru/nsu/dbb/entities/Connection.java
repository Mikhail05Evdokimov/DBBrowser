package ru.nsu.dbb.entities;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    private String name;
    private boolean isConnected;
    private ConnectionInfo connectionInfo;
    private List<Database> databaseList;
    private Schema schema;
    private Session session;

    // в зависимости от этой переменной будем выводить сразу содержимое схемы или список баз данных
    private boolean supportsDatabaseAndSchema;

    public Connection(String name, ConnectionInfo connectionInfo) {
        this.name = name;
        this.connectionInfo = connectionInfo;
        connect();
    }

    public void connect() {
        if (session != null) {
            session.disconnect();
        }
        this.session = new Session(connectionInfo);
        this.isConnected = session.isConnected();
        this.supportsDatabaseAndSchema = session.isSupportsDatabaseAndSchema();
        if (supportsDatabaseAndSchema) {
            setDatabaseList();
        } else {
            setSchema();
        }
    }

    public void disconnect() {
        session.disconnect();
        session = null;
        isConnected = false;
    }

    public void reconnect() {
        session.reconnect(connectionInfo);
        this.isConnected = session.isConnected();
        this.supportsDatabaseAndSchema = session.isSupportsDatabaseAndSchema();
        if (supportsDatabaseAndSchema) {
            setDatabaseList();
        } else {
            setSchema();
        }
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
        this.session.reconnect(connectionInfo);
        this.isConnected = session.isConnected();
        this.supportsDatabaseAndSchema = session.isSupportsDatabaseAndSchema();
        if (supportsDatabaseAndSchema) {
            setDatabaseList();
        } else {
            setSchema();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDatabaseList() {
        return databaseList.stream().map(Database::getName).toList();
    }

    public Database getDatabase(String name) {
        return databaseList.stream()
                .filter(element -> element.getName().equals(name))
                .findFirst().orElse(null);
    }

    public void setDatabaseList() {
        this.databaseList = session.getDatabases();
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema() {
        this.schema = new Schema("schema");
    }

    // Вместо этого надо сделать изменить удалить добавить данные в таблицу
    public PreparedStatement getPreparedStatement(String sql) {
        return session.getPreparedStatement(sql);
    }

    // а тут сразу исполнить результат и получить данные
    public Statement getStatement() {
        return session.getStatement();
    }

    public void updateSaveStatement(String sql) {
        session.updateSaveStatement(sql);
    }

    public void saveChanges() {
        session.saveChanges();
    }

    public void discardChanges() {
        session.discardChanges();
    }

    public void setSchemasFor(String databaseName) {
        List<Schema> schemaList = session.getSchemas(databaseName);
        getDatabase(databaseName).setSchemaList(schemaList);
    }

    // SQLITE SPECIFIC FUNCTIONS
    public void setViews() {
        List<View> viewList = session.getViews();
        schema.setViewList(viewList);
    }

    public void setTables() {
        List<Table> tableList = session.getTables();
        schema.setTableList(tableList);
    }

    public void setIndexes() {
        List<Index> indexList = session.getIndexes();
        schema.setIndexList(indexList);
    }

    public void setIndexesFor(String tableName) {
        List<Index> indexList = session.getIndexes(tableName);
        Table table = schema.getTable(tableName);
        if (table == null) {
            throw new RuntimeException("No such table in schema. Possible solution: use setTables()");
        }
        table.setIndexList(indexList);
    }

    public void setColumnsFor(String tableName) {
        List<Column> columnList = session.getColumns(tableName);
        Table table = schema.getTable(tableName);
        if (table == null) {
            throw new RuntimeException("No such table in schema. Possible solution: use setTables()");
        }
        table.setColumnList(columnList);
    }

    public void setForeignKeysFor(String tableName) {
        List<ForeignKey> foreignKeyList = session.getForeignKeys(tableName);
        Table table = schema.getTable(tableName);
        if (table == null) {
            throw new RuntimeException("No such table in schema. Possible solution: use setTables()");
        }
        table.setForeignKeyList(foreignKeyList);
    }

    public void setKeysFor(String tableName) {
        List<Key> keyList = session.getKeys(tableName);
        Table table = schema.getTable(tableName);
        if (table == null) {
            throw new RuntimeException("No such table in schema. Possible solution: use setTables()");
        }
        table.setKeyList(keyList);
    }


    @Deprecated
    public Session getSession() {
        return session;
    }



    public boolean isSupportsDatabaseAndSchema() {
        return supportsDatabaseAndSchema;
    }
}
