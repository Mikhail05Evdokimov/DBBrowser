package app.backend.controllers;

import app.MenuController;
import app.backend.Signaller;
import app.backend.entities.Connection;
import app.backend.entities.ConnectionInfo;
import app.backend.entities.DataTable;
import app.backend.entities.View;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionController {

    private static Signaller signaller;

    public static void init(MenuController controller) {
        signaller = new Signaller(controller);
    }

    public static void addConnection(ConnectionInfo.ConnectionType type, String filePath) {
        Map<String, String> path = new HashMap<>();
        path.put("path", filePath);
        ConnectionInfo connectionInfo = new ConnectionInfo(type, path);
        StorageController.connectionStorage.addConnectionToStorage(new Connection(filePath, connectionInfo));
        signaller.emitSignalToAddConnectionName(filePath);
        signaller.emitSignalToShowTree(filePath);
        signaller.emitSignalToDBInfo();
    }

    public static void addConnection(ConnectionInfo.ConnectionType type, String host, String port, String dbname, String login, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("host", host);
        map.put("port", port);
        map.put("dbname", dbname);
        map.put("username", login);
        map.put("password", password);
        ConnectionInfo connectionInfo = new ConnectionInfo(type, map);
        StorageController.connectionStorage.addConnectionToStorage(new Connection(dbname, connectionInfo));
        signaller.emitSignalToAddConnectionName(dbname);
        signaller.emitSignalToShowTree(dbname);
        signaller.emitSignalToDBInfo();
    }

    public static List<String> getSchema(String conName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setSchema();
        connection.setTables();
        return StorageController.connectionStorage.getConnection(conName).getSchema().getTableList();
    }

    public static String getDBInfo(String conName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        return connection.getConnectionInfo().getConnectionType().toString();
    }

    public static void closeConnection(String conName) {
        StorageController.connectionStorage.getConnection(conName).disconnect();
        signaller.emitSignalToHideTree();
        signaller.emitSignalToDeleteConnection(conName);
    }

    public static void deleteCon(String conName) {
        StorageController.connectionStorage.removeConnection(conName);
    }

    public static void reconnectConnection(String conName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        if (connection.isConnected()) {
            connection.reconnect();
        }
        else {
            connection.connect();
        }
        signaller.emitSignalToHideTree();
        signaller.emitSignalToShowTree(conName);
        signaller.emitSignalToAddConnectionName(conName);
        signaller.emitSignalToDBInfo();
    }

    public static List<String> getColumns(String conName, String tableName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setColumnsFor(tableName);
        return connection.getSchema().getTable(tableName).getColumnList();
    }

    public static void getContentInTable(String conName, String tableName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setSchema();
        connection.setTables();
        connection.setColumnsFor(tableName);
        signaller.emitSignalToGetTableData(connection.getDataFromTable(tableName), tableName);
    }

    public static void changeData(String conName, String tableName, int row, List<Integer> columns, List<String> data) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.updateData(tableName, row, columns, data);
        //connection.getSchema().getTable(tableName);
    }

    public static void saveChanges(String conName) {
        StorageController.connectionStorage.getConnection(conName).saveChanges();
    }

    public static void discardChanges(String conName, String tableName) {
        var connection = StorageController.connectionStorage.getConnection(conName);
        connection.discardChanges();

        signaller.emitSignalToGetTableData(connection.getDataFromTable(tableName), tableName);
    }

    public static void discardChanges(String conName) {
        var connection = StorageController.connectionStorage.getConnection(conName);
        connection.discardChanges();
    }

    public static void deleteRow(String conName, String tableName, int row) {
        StorageController.connectionStorage.getConnection(conName).deleteData(tableName, row);
    }

    public static DataTable addData(String conName, String tableName, List<String> data) {
        return StorageController.connectionStorage.getConnection(conName).insertData(tableName, data);
    }

    public static String getDDL(String conName, String tableName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        return connection.getSchema().getTable(tableName).getDefinition();
    }

    public static List<String> getView(String conName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setViews();
        return connection.getSchema().getViewList();
    }

    public static List<String> getIndexes(String conName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setIndexes();
        return connection.getSchema().getIndexList();
    }

    public static List<String> getKeys(String conName, String tableName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setKeysFor(tableName);
        return connection.getSchema().getTable(tableName).getKeyList();
    }

    public static List<String> getForeignKeys(String conName, String tableName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        connection.setForeignKeysFor(tableName);
        return connection.getSchema().getTable(tableName).getForeignKeyList();
    }

    public static DataTable execQuery(String conName, String query) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        return connection.executeQuery(query);
    }

    public static boolean isActive(String conName) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        return connection.isConnected();
    }

    public static List<String> getAllConnections() {
        return StorageController.connectionStorage.getConnectionList();
    }

}
