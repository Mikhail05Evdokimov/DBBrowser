package app.backend.controllers;

import app.backend.entities.ConnectionStorage;
import app.backend.utility.Saver;

import java.io.IOException;

public class StorageController {

    public static ConnectionStorage connectionStorage;

    public static void init() {
        connectionStorage = Saver.getConnectionStorage();
    }

}
