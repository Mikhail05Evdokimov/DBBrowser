package app.backend.entity;

import java.util.List;

public class Conection {
    private String name;
    private ConnectionInfo info;
    private List<Database> listOfDataSets;
    private Session session;

    public Conection(String name, ConnectionInfo info, List<Database> listOfDataSets, Session session) {
        this.name = name;
        this.info = info;
        this.listOfDataSets = listOfDataSets;
        this.session = session;
    }

    public void removeSession(){
        this.session = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectionInfo getInfo() {
        return info;
    }

    public void setInfo(ConnectionInfo info) {
        this.info = info;
    }

    public List<Database> getListOfDataSets() {
        return listOfDataSets;
    }

    public void setListOfDataSets(List<Database> listOfDataSets) {
        this.listOfDataSets = listOfDataSets;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
