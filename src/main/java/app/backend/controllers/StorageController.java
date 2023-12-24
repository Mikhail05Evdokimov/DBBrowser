package app.backend.controllers;

import app.backend.entities.ConnectionStorage;

public class StorageController {

    public static ConnectionStorage connectionStorage;

    public static void init() {
        connectionStorage = new ConnectionStorage();
    }

}
