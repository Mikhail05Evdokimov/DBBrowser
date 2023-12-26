package app.backend.entities;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStorage {
    private final List<Connection> connectionList;

    public ConnectionStorage() {
        this.connectionList = new ArrayList<>();
    }

    public List<String> getConnectionList() {
        return connectionList.stream().map(Connection::getName).toList();
    }

    public Connection getConnection(String connectionName){
        return connectionList.stream()
                .filter(element -> element.getName().equals(connectionName))
                .findFirst().orElse(null);
    }

    public void removeConnection(String connectionName){
        connectionList.removeIf(x -> x.getName().equals(connectionName));
    }

    public void addConnectionToStorage(Connection connection){
        connectionList.add(connection);
    }

    public void disconnectAll() {
        connectionList.forEach(Connection::disconnect);
    }
}
