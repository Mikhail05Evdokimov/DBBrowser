package app.backend.utility;

import app.backend.entities.ConnectionStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saver {
    public static void saveConnectionStorage(ConnectionStorage connectionStorage) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("saves/save.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(connectionStorage);
        objectOutputStream.close();
    }

    public static ConnectionStorage getConnectionStorage() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("saves/save.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (ConnectionStorage) objectInputStream.readObject();
    }

}
