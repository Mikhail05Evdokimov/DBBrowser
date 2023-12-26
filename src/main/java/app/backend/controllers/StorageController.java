package app.backend.controllers;

import app.backend.entities.ConnectionStorage;
import app.backend.utility.Saver;

public class StorageController {

    public static ConnectionStorage connectionStorage;

    public static void init() {
        connectionStorage = Saver.getConnectionStorage();
    }

}
