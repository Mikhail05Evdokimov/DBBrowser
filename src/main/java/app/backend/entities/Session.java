package app.backend.entities;

import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.SQLiteDataSource;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Session {
    private Connection connection;
    private ConnectionInfo connectionInfo;
    private Statement saveStatement;
    private DatabaseMetaData meta;
    private boolean supportsDatabaseAndSchema;

    public Session(ConnectionInfo info) {
        try {
            connection = connect(info);
        } catch (SQLException e) {
            throw new RuntimeException("Problems with connection");
        }
    }

    public Connection connect(ConnectionInfo info) throws SQLException {
        Connection connection = null;
        this.connectionInfo = info;
        switch (connectionInfo.getConnectionType()) {
            case SQLITE -> {
                SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
                sqLiteDataSource.setUrl(connectionInfo.getProperties().get("url"));
                connection = sqLiteDataSource.getConnection();
            }
            case POSTGRESQL -> {
                PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                Map<String, String> props = connectionInfo.getProperties();
                pgSimpleDataSource.setUrl(props.get("url"));
                pgSimpleDataSource.setUser(props.get("username"));
                pgSimpleDataSource.setPassword(props.get("password"));
                connection = pgSimpleDataSource.getConnection();
            }
        }
        connection.setAutoCommit(false);
        saveStatement = connection.createStatement();
        meta = connection.getMetaData();
        supportsDatabaseAndSchema = meta.supportsCatalogsInDataManipulation() &&
            meta.supportsSchemasInDataManipulation();
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

    public boolean isSupportsDatabaseAndSchema() {
        return supportsDatabaseAndSchema;
    }

    // Statements functions
    // Это использовать для заранне подготовленных запросов (колонки и таблицы должны быть определены)
    public PreparedStatement getPreparedStatement(String sql) {
        try {
            switch (connectionInfo.getConnectionType()) {
                case SQLITE -> {
                    return connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                }
                case POSTGRESQL -> {
                    return connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }
                default -> {
                    return connection.prepareStatement(sql);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // execute query: get data, update data, update schema

    // Для запросов пользователя
    public Statement getStatement() {
        try {
            switch (connectionInfo.getConnectionType()) {
                case SQLITE -> {
                    return connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                }
                case POSTGRESQL -> {
                    return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }
                default -> {
                    return connection.createStatement();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataTable getDataFromTable(String tableName, int rowsToGet) {
        try {
            String sql = "SELECT * FROM " + tableName;
            Statement statement = getStatement();
            List<List<String>> rows = new ArrayList<>();
            List<String> columnNames = new ArrayList<>();
            int rowsGot = 0;

            long startTime = System.currentTimeMillis();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                columnNames.add(resultSetMetaData.getColumnName(i));
            }
            while (rowsGot < rowsToGet && rs.next()) {
                rowsGot++;
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    row.add(rs.getString(i));
                }
                rows.add(row);
            }
            long executionTime = System.currentTimeMillis() - startTime;

            return new DataTable(columnNames, rows, rs, rowsGot, executionTime);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query for some reasons...");
        }
    }

    public void insertData(String tableName, List<String> newValues, List<String> columnNames) {
        String columns = columnNames.stream().reduce("", (x, y) -> x + ", " + y).substring(2);
        String values = newValues.stream().reduce("", (x, y) ->  x + "\'"  + ", " + "\'" + y ).substring(3) + "\'";
        String sql = "INSERT INTO " + tableName + " (" + columns + ") " + "VALUES (" + values + ");";
        Statement statement = getStatement();
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Can't insert data");
        }
    }

    public void deleteData(String tableName, DataTable dataTable, int index) {
        List<Key> keys = getKeys(tableName);
        List<String> keyColumns = new ArrayList<>();
        for (Key k : keys) {
            keyColumns.addAll(k.getColumns());
        }
        List<String> allColumns = dataTable.getColumnNames();

        String actualKey = allColumns.stream().filter(keyColumns::contains).findFirst().orElse(null);
        int colIndex = allColumns.indexOf(actualKey);
        String id = dataTable.getRows().get(index).get(colIndex);

        String sql = "DELETE FROM " + tableName + " WHERE " + actualKey + " = " + "\'" + id + "\'" + ";";
        Statement statement = getStatement();
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete data");
        }

        dataTable.deleteRow(index);
    }

    public void updateData(String tableName, DataTable dataTable, int rowNumber, List<Integer> columnNumbers, List<String> values) {
        List<Key> keys = getKeys(tableName);
        List<String> keyColumns = new ArrayList<>();
        for (Key k : keys) {
            keyColumns.addAll(k.getColumns());
        }
        List<String> allColumns = dataTable.getColumnNames();

        String actualKey = allColumns.stream().filter(keyColumns::contains).findFirst().orElse(null);
        int colIndex = allColumns.indexOf(actualKey);
        String id = dataTable.getRows().get(rowNumber).get(colIndex);

        String sql = "UPDATE " + tableName + " SET ";
        for (int col : columnNumbers) {
            sql += allColumns.get(col) + " = ?, ";
        }
        sql = sql.substring(0, sql.length() - 2) + " WHERE " + actualKey + " = " + "\'" + id + "\'" + ";";

        PreparedStatement preparedStatement = getPreparedStatement(sql);
        try {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dataTable.changeRow(rowNumber, columnNumbers, values);
    }

    public void createIndex(String indexName, String tableName, boolean isUnique, List<String> columnsNames) {
        String sql = "CREATE ";
        if (isUnique) {
            sql += "UNIQUE ";
        }
        sql += "INDEX IF NOT EXISTS " + indexName + " ON " + tableName;
        String columns = columnsNames.stream().reduce("", (x, y) -> x + ", " + y).substring(2);
        sql += " (" + columns + ");";
        updateSaveStatement(sql);
    }

    public void deleteIndex(String indexName) {
        String sql = "DROP INDEX IF EXISTS " + indexName + ";";
        updateSaveStatement(sql);
    }

    public void createView(String viewName, String sql) {
        String query = "CREATE VIEW IF NOT EXISTS " + viewName + " AS " + sql + ";";
        updateSaveStatement(query);
    }

    public void deleteView(String viewName) {
        String sql = "DROP VIEW IF EXISTS " + viewName + ";";
        updateSaveStatement(sql);
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
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Can't discard changes");
        }
    }

    // Working with meta functions

    // PGSQL
    public List<Database> getDatabases() {
        try {
            List<Database> databaseList = new ArrayList<>();
            ResultSet resultSet = meta.getCatalogs();
            while (resultSet.next()) {
                databaseList.add(new Database(resultSet.getString("TABLE_CAT")));
            }
            return databaseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // PGSQL
    public List<Schema> getSchemas(String databaseName) {
        try {
            List<Schema> schemaList = new ArrayList<>();
            ResultSet resultSet = meta.getSchemas(databaseName, null);
            while (resultSet.next()) {
                schemaList.add(new Schema(resultSet.getString("TABLE_SCHEM")));
            }
            return schemaList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SQLITE
    public List<View> getViews() {
        try {
            List<View> viewList = new ArrayList<>();
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, sql FROM sqlite_master WHERE type == \"view\"");
            while (resultSet.next()) {
                viewList.add(new View(resultSet.getString("name"), resultSet.getString("sql")));
            }
            statement.close();
            return viewList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Table> getTables() {
        try {
            List<Table> tableList = new ArrayList<>();
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT name, sql FROM sqlite_master " +
                    "WHERE type == \"table\" AND name NOT IN ('sqlite_sequence', 'sqlite_stat1', 'sqlite_master')");
            while (resultSet.next()) {
                tableList.add(new Table(resultSet.getString("name"), resultSet.getString("sql")));
            }
            statement.close();
            return tableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Index> getIndexes() {
        try {
            List<Index> indexList = new ArrayList<>();
            Statement statement = getStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT name FROM sqlite_master " +
                    "WHERE type == \"table\" AND name NOT IN ('sqlite_sequence', 'sqlite_stat1', 'sqlite_master')");
            while (resultSet.next()) {
                indexList.addAll(getIndexes(resultSet.getString("name")));
            }
            statement.close();
            return indexList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Index> getIndexes(String tableName) {
        try {
            List<Index> indexList = new ArrayList<>();
            ResultSet resultSet = meta.getIndexInfo(null, null, tableName, false, false);
            Statement statement = getStatement();
            while (resultSet.next()) {
                String name = resultSet.getString("INDEX_NAME");

                if (indexList.stream().anyMatch(x -> x.getName().equals(name))) {
                    continue;
                }

                boolean unique = !resultSet.getBoolean("NON_UNIQUE");

                // Возможно убрать в отдельную функцию получение колонок
                LinkedList<Column> columnLinkedList = new LinkedList<>();
                ResultSet columnsNamesResultSet = statement.executeQuery("PRAGMA index_info('" + name + "')");
                while (columnsNamesResultSet.next()) {
                    String columnName = columnsNamesResultSet.getString("name");
                    ResultSet columnsResultSet = meta.getColumns(null, null, tableName, columnName);
                    String dataType = columnsResultSet.getString("TYPE_NAME");
                    boolean notNull = columnsResultSet.getString("IS_NULLABLE").equals("YES");
                    boolean autoInc = columnsResultSet.getString("IS_AUTOINCREMENT").equals("YES");
                    String defaultDefinition = columnsResultSet.getString("COLUMN_DEF");
                    columnLinkedList.addLast(new Column(columnName, dataType, notNull, autoInc, defaultDefinition));
                }

                indexList.add(new Index(name, unique, columnLinkedList));
            }
            statement.close();
            return indexList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Column> getColumns(String tableName) {
        try {
            List<Column> columnList = new ArrayList<>();
            ResultSet resultSet = meta.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                String name = resultSet.getString("COLUMN_NAME");
                String dataType = resultSet.getString("TYPE_NAME");
                boolean notNull = resultSet.getString("IS_NULLABLE").equals("YES");
                boolean autoInc = resultSet.getString("IS_AUTOINCREMENT").equals("YES");
                String defaultDefinition = resultSet.getString("COLUMN_DEF");
                columnList.add(new Column(name, dataType, notNull, autoInc, defaultDefinition));
            }
            return columnList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ForeignKey> getForeignKeys(String tableName) {
        try {
            List<ForeignKey> foreignKeyList = new ArrayList<>();
            ResultSet rs = meta.getImportedKeys(null, null, tableName);
            int index = 0;
            int previousKeySeq = 1;

            while (rs.next()) {
                String childColumn = rs.getString("FKCOLUMN_NAME");
                String parentColumn = rs.getString("PKCOLUMN_NAME");
                String parentTable = rs.getString("PKTABLE_NAME");
                int currentKeySeq = rs.getInt("KEY_SEQ");

                if (currentKeySeq <= previousKeySeq) {
                    index = index + 1;
                }
                String name = (rs.getString("FK_NAME") == null || rs.getString("FK_NAME").isEmpty()) ?
                    ("FK_" + tableName + "_" + parentTable + "_" + index) : rs.getString("FK_NAME");

                if (currentKeySeq == 1) {
                    previousKeySeq = 1;
                    foreignKeyList.add(new ForeignKey(name, childColumn, parentTable, parentColumn));
                } else {
                    previousKeySeq = currentKeySeq;
                    ForeignKey fk = foreignKeyList.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);

                    if (fk == null) {
                        foreignKeyList.add(new ForeignKey(name, childColumn, parentTable, parentColumn));
                    } else {
                        fk.addChildColumn(childColumn);
                        fk.addParentColumn(parentColumn);
                    }
                }
            }
            return foreignKeyList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Key> getKeys(String tableName) {
        try {
            List<Key> keyList = new ArrayList<>();
            ResultSet resultSet = meta.getPrimaryKeys(null, null, tableName);
            int index = 0;
            int previousKeySeq = 1;

            while (resultSet.next()) {
                String column = resultSet.getString("COLUMN_NAME");
                int currentKeySeq = resultSet.getInt("KEY_SEQ");

                if (currentKeySeq <= previousKeySeq) {
                    index = index + 1;
                }
                String name = (resultSet.getString("PK_NAME") == null ||
                    resultSet.getString("PK_NAME").isEmpty()) ?
                    (tableName + "_PK" + "_" + index) : resultSet.getString("PK_NAME");

                if (currentKeySeq == 1) {
                    previousKeySeq = 1;
                    keyList.add(new Key(name, column));
                } else {
                    previousKeySeq = currentKeySeq;
                    Key key = keyList.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
                    if (key == null) {
                        keyList.add(new Key(name, column));
                    } else {
                        key.addColumn(column);
                    }
                }
            }
            return keyList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataTable executeQuery(String sql, int rowsToGet) {
        Statement statement = getStatement();
        try {
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            List<String> columnNames = new ArrayList<>();
            List<List<String>> rows = new ArrayList<>();
            int rowsGot = 0;

            long startTime = System.currentTimeMillis();
            int columnsNumber = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                columnNames.add(resultSetMetaData.getColumnName(i));
            }
            while (rowsGot < rowsToGet && rs.next()) {
                rowsGot++;
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    row.add(rs.getString(i));
                }
                rows.add(row);
            }
            long executionTime = System.currentTimeMillis() - startTime;
            return new DataTable(columnNames, rows, rs, rowsGot, executionTime);
        } catch (SQLException e) {
            return new DataTable(e.getMessage());
        }
    }
}