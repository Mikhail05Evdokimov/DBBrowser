package app.backend.controllers;

import app.backend.entities.Connection;
import app.backend.entities.ConnectionInfo;

import java.util.HashMap;
import java.util.Map;

public class ConnectionController {

    public static void addConnection(ConnectionInfo.ConnectionType type, String filePath) {
        Map<String, String> path = new HashMap<>();
        path.put("path", filePath);
        ConnectionInfo connectionInfo = new ConnectionInfo(type, path);
        StorageController.getConnectionStorage().addConnectionToStorage(new Connection(filePath, connectionInfo));
    }

    public static void addConnection(ConnectionInfo.ConnectionType type, String host, String port, String dbname, String login, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("host", host);
        map.put("port", port);
        map.put("dbname", dbname);
        map.put("username", login);
        map.put("password", password);
        ConnectionInfo connectionInfo = new ConnectionInfo(type, map);
        StorageController.getConnectionStorage().addConnectionToStorage(new Connection(dbname, connectionInfo));
    }

}
