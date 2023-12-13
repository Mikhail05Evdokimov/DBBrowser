package app.backend.entity;


import java.util.ArrayList;
import java.util.List;

public class ConnectionStorage {
    private final List<Conection> listOfConnections;

    public ConnectionStorage() {
        this.listOfConnections = new ArrayList<>();
    }

    public Conection getConnection(ConnectionInfo info){
        return listOfConnections.stream()
                .filter(element -> element.getInfo() == info)
                .findFirst().orElse(null);
    }

    public void removeConnection(ConnectionInfo info){
        listOfConnections.removeIf(x -> x.getInfo() == info);
    }

    public void addConnectionToStorage(Conection conection){
        listOfConnections.add(conection);
    }
}
