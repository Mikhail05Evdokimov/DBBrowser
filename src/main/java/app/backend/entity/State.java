package app.backend.entity;

public class State {
    private Conection currentConnection;
    private Database currentDatabase;
    private Schema currentSchema;
    private Table currentTable;

    public State(Conection currentConnection, Database currentDatabase, Schema currentSchema, Table currentTable) {
        this.currentConnection = currentConnection;
        this.currentDatabase = currentDatabase;
        this.currentSchema = currentSchema;
        this.currentTable = currentTable;
    }

    public Conection getCurrentConnection() {
        return currentConnection;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public Schema getCurrentSchema() {
        return currentSchema;
    }

    public Table getCurrentTable() {
        return currentTable;
    }
}
