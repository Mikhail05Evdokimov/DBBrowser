package app.backend.entities;

import javax.xml.crypto.Data;
import java.sql.DatabaseMetaData;
import java.util.List;

public class Connection {
    private static int DEFAULT_ROWS_TO_GET = 100;
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

    public boolean isConnected() {
        return isConnected;
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

    public DataTable getDataFromTable(String tableName) {
        DataTable dataTable = session.getDataFromTable(tableName, DEFAULT_ROWS_TO_GET);
        schema.getTable(tableName).setDataTable(dataTable);
        return dataTable;
    }

    public DataTable executeQuery(String sql) {
        return session.executeQuery(sql, DEFAULT_ROWS_TO_GET);
    }

    public DataTable insertData(String tableName, List<String> newValues) {
        session.insertData(tableName, newValues, schema.getTable(tableName).getDataTable().getColumnNames());
        return getDataFromTable(tableName);
    }

    public DataTable deleteData(String tableName, int index) {
        DataTable dataTable = schema.getTable(tableName).getDataTable();
        session.deleteData(tableName, dataTable, index);
        return getDataFromTable(tableName);
    }

    public DataTable updateData(String tableName, int rowNumber, List<Integer> columnNumbers, List<String> values) {
        DataTable dataTable = schema.getTable(tableName).getDataTable();
        session.updateData(tableName, dataTable, rowNumber, columnNumbers, values);
        return getDataFromTable(tableName);
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
