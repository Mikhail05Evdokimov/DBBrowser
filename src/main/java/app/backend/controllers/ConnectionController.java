package app.backend.controllers;

import app.MenuController;
import app.backend.Signaller;
import app.backend.entities.Connection;
import app.backend.entities.ConnectionInfo;

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
        StorageController.connectionStorage.removeConnection(conName);
        signaller.emitSignalToHideTree();
        signaller.emitSignalToDeleteConnection(conName);
    }

    public static void reconnectConnection(String conName) {
        StorageController.connectionStorage.getConnection(conName).reconnect();
        signaller.emitSignalToHideTree();
        signaller.emitSignalToShowTree(conName);
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
        signaller.emitSignalToGetTableData(connection.getDataFromTable(tableName));
    }

    public static void execQuery(String conName, String query) {
        Connection connection = StorageController.connectionStorage.getConnection(conName);
        //connection.
    }

}
