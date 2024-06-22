package app.backend.entities;

import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.SQLiteDataSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;

import static app.backend.entities.ConnectionInfo.ConnectionType.H2;
import static app.backend.entities.ConnectionInfo.ConnectionType.SQLITE;

public class Session {
    private Connection connection;
    private ConnectionInfo connectionInfo;
    private Statement saveStatement;
    private DatabaseMetaData meta;
    private boolean supportsDatabaseAndSchema;
    private Stack<Savepoint> savepoints;

    public Session(ConnectionInfo info) {
        try {
            connection = connect(info);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
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
                supportsDatabaseAndSchema = false;
            }
            case POSTGRESQL -> {
                PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                Map<String, String> props = connectionInfo.getProperties();
                pgSimpleDataSource.setUrl(props.get("url"));
                pgSimpleDataSource.setUser(props.get("username"));
                pgSimpleDataSource.setPassword(props.get("password"));
                connection = pgSimpleDataSource.getConnection();
                supportsDatabaseAndSchema = true;
            }
            case H2 -> {
                JdbcDataSource jdbcDataSource = new JdbcDataSource();
                Map<String, String> props = connectionInfo.getProperties();
                jdbcDataSource.setURL(props.get("url"));
                jdbcDataSource.setUser(props.get("username"));
                jdbcDataSource.setPassword(props.get("password"));
                System.out.println(jdbcDataSource.getURL());
                connection = jdbcDataSource.getConnection();
                System.out.println("Connection to H2 database successful!");
                supportsDatabaseAndSchema = true;
            }
        }
        savepoints = new Stack<>();
        connection.setAutoCommit(false);
        saveStatement = connection.createStatement();
        meta = connection.getMetaData();
        System.out.println(supportsDatabaseAndSchema);
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
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void insertData(String tableName, List<String> newValues, List<String> columnNames) {
        String columns = columnNames.stream().reduce("", (x, y) -> x + ", " + y).substring(2);
        String values = newValues.stream().reduce("", (x, y) -> x + "\'" + ", " + "\'" + y).substring(3) + "\'";
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

        // Найти фактический ключ из имен столбцов
        String actualKey = allColumns.stream().filter(keyColumns::contains).findFirst().orElse(null);
        if (actualKey == null) {
            throw new IllegalArgumentException("No matching key column found in the table.");
        }
        int colIndex = allColumns.indexOf(actualKey);
        System.out.println(colIndex);
        String id = dataTable.getRows().get(rowNumber).get(colIndex);

        // Получить типы столбцов из базы данных
        List<Integer> columnTypes = getColumnTypes(tableName, columnNumbers);

        // Построить SQL UPDATE запрос
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int col : columnNumbers) {
            sql.append(allColumns.get(col)).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Удалить последнюю запятую и пробел
        sql.append(" WHERE ").append(actualKey).append(" = ?;");

        try (PreparedStatement preparedStatement = getPreparedStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                int columnType = columnTypes.get(i);
                setPreparedStatementValue(preparedStatement, i + 1, value, columnType);
            }
            preparedStatement.setString(values.size() + 1, id); // Установить значение ключа
            preparedStatement.executeUpdate();

            // Обновить DataTable
            dataTable.changeRow(rowNumber, columnNumbers, values);
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL update", e);
        }
    }

    private List<Integer> getColumnTypes(String tableName, List<Integer> columnNumbers) {
        List<Integer> columnTypes = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " LIMIT 1";
        try (Statement stmt = getStatement(); ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int col : columnNumbers) {
                columnTypes.add(metaData.getColumnType(col + 1)); // ResultSetMetaData columns are 1-based
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving column types", e);
        }
        return columnTypes;
    }

    private void setPreparedStatementValue(PreparedStatement preparedStatement, int parameterIndex, String value, int columnType) throws SQLException {
        switch (columnType) {
            case Types.INTEGER, Types.NUMERIC:
                preparedStatement.setInt(parameterIndex, Integer.parseInt(value));
                break;
            case Types.DOUBLE:
                preparedStatement.setDouble(parameterIndex, Double.parseDouble(value));
                break;
            case Types.BOOLEAN:
                preparedStatement.setBoolean(parameterIndex, Boolean.parseBoolean(value));
                break;
            case Types.DATE:
                preparedStatement.setDate(parameterIndex, Date.valueOf(value));
                break;
            default:
                preparedStatement.setString(parameterIndex, value);
                break;
        }
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

    public List<Table> getTables(Schema schema) {
        try {
            List<Table> tableList = new ArrayList<>();
            Statement statement = getStatement();
            if (!supportsDatabaseAndSchema){

                ResultSet resultSet = statement.executeQuery(
                        "SELECT name, sql FROM sqlite_master " +
                                "WHERE type == \"table\" AND name NOT IN ('sqlite_sequence', 'sqlite_stat1', 'sqlite_master')");
                while (resultSet.next()) {
                    tableList.add(new Table(resultSet.getString("name"), resultSet.getString("sql")));
                }
            }
            else {
                ResultSet resultSet = statement.executeQuery(
                        "SELECT table_name\n" +
                                "FROM information_schema.tables\n" +
                                "WHERE table_schema = '" + schema.getName()+ "'\n" +
                                "  AND table_type = 'BASE TABLE';"
                );
                while (resultSet.next()) {
                    tableList.add(new Table(resultSet.getString("TABLE_NAME"), ""));
                }
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
            if(connectionInfo.getConnectionType() == SQLITE){
                ResultSet resultSet = statement.executeQuery(
                        "SELECT name FROM sqlite_master " +
                                "WHERE type == \"table\" AND name NOT IN ('sqlite_sequence', 'sqlite_stat1', 'sqlite_master')");
                while (resultSet.next()) {
                    indexList.addAll(getIndexes(resultSet.getString("name")));
                }
            }
            else if (connectionInfo.getConnectionType() == H2){
                ResultSet resultSet = statement.executeQuery(
                        "SELECT TABLE_NAME, COLUMN_NAME, IS_UNIQUE FROM INFORMATION_SCHEMA.INDEX_COLUMNS"
                );


                while (resultSet.next()) {
                    LinkedList<Column> columnLinkedList = new LinkedList<>();
                    String table = resultSet.getString("TABLE_NAME");
                    Statement colStatement = getStatement();
                    ResultSet columnsNamesResultSet = colStatement.executeQuery("SELECT i.TABLE_SCHEMA, i.TABLE_NAME, i.INDEX_NAME, ic.COLUMN_NAME, i.INDEX_TYPE_NAME, ic.IS_UNIQUE, COALESCE(c.IDENTITY_INCREMENT, 0) AS IDENTITY_INCREMENT, i.REMARKS, c.ORDINAL_POSITION, c.IS_NULLABLE\n" +
                            "FROM INFORMATION_SCHEMA.INDEXES i\n" +
                            "        JOIN INFORMATION_SCHEMA.INDEX_COLUMNS ic ON i.TABLE_NAME = ic.TABLE_NAME AND i.INDEX_NAME = ic.INDEX_NAME\n" +
                            "    JOIN INFORMATION_SCHEMA.COLUMNS c ON c.COLUMN_NAME = ic.COLUMN_NAME AND c.TABLE_NAME = ic.TABLE_NAME\n" +
                            "WHERE c.TABLE_NAME = '" + table + "'\n" +
                            "ORDER BY ORDINAL_POSITION");
                    while (columnsNamesResultSet.next()) {
                        String columnName = columnsNamesResultSet.getString("COLUMN_NAME");
//                        ResultSet columnsResultSet = meta.getColumns(null, null, table, columnName);
                        String dataType = columnsNamesResultSet.getString("INDEX_TYPE_NAME");
                        boolean notNull = columnsNamesResultSet.getString("IS_NULLABLE").equals("YES");
                        boolean autoInc = !columnsNamesResultSet.getString("IDENTITY_INCREMENT").equals("0");
                        String defaultDefinition = columnsNamesResultSet.getString("REMARKS");
                        columnLinkedList.addLast(new Column(columnName, dataType, notNull, autoInc, defaultDefinition));
                    }
                    indexList.add(new Index(resultSet.getString("COLUMN_NAME"), resultSet.getString("IS_UNIQUE").equals("true"), columnLinkedList));
                }

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