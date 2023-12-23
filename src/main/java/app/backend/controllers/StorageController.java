package app.backend.controllers;

import app.backend.entities.ConnectionStorage;

public class StorageController {

    private static ConnectionStorage connectionStorage;

    public static void init() {
        connectionStorage = new ConnectionStorage();
    }

    public static ConnectionStorage getConnectionStorage() {
        return connectionStorage;
    }




}
