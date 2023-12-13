package ru.nsu.dbb.entities;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    private String name;
    private ConnectionInfo connectionInfo;
    private List<Database> databaseList;
    private Session session;

    public Connection(String name, ConnectionInfo connectionInfo) {
        this.name = name;
        this.connectionInfo = connectionInfo;
        this.databaseList = new ArrayList<>();
        this.session = new Session(connectionInfo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
        this.session.disconnect();
        this.session = new Session(connectionInfo);
    }

    public List<Database> getDatabaseList() {
        return databaseList;
    }

    public Database getDatabase(String name) {
        return databaseList.stream()
                .filter(element -> element.getName() == name)
                .findFirst().orElse(null);
    }

    public void setDatabaseList(List<Database> databaseList) {
        this.databaseList = databaseList;
    }

    public Session getSession() {
        return session;
    }
}
