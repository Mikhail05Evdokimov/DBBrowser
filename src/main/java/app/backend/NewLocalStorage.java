package app.backend;

import app.backend.entities.ConnectionStorage;

public class NewLocalStorage {

    public static ConnectionStorage connectionStorage;

    public void init() {
        connectionStorage = new ConnectionStorage();
    }

}
