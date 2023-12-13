package ru.nsu.dbb.entities;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStorage {
    private final List<Connection> connectionList;

    public ConnectionStorage() {
        this.connectionList = new ArrayList<>();
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public Connection getConnection(ConnectionInfo info){
        return connectionList.stream()
                .filter(element -> element.getConnectionInfo() == info)
                .findFirst().orElse(null);
    }

    public void removeConnection(ConnectionInfo info){
        connectionList.removeIf(x -> x.getConnectionInfo() == info);
    }

    public void addConnectionToStorage(Connection connection){
        connectionList.add(connection);
    }
}
