package ru.nsu.dbb;

import io.qt.widgets.*;
import ru.nsu.dbb.entities.Column;
import ru.nsu.dbb.entities.Connection;
import ru.nsu.dbb.entities.ConnectionInfo;
import ru.nsu.dbb.entities.ConnectionStorage;
import ru.nsu.dbb.entities.Database;
import ru.nsu.dbb.entities.ForeignKey;
import ru.nsu.dbb.entities.Index;
import ru.nsu.dbb.entities.Key;
import ru.nsu.dbb.entities.Schema;
import ru.nsu.dbb.entities.Session;
import ru.nsu.dbb.entities.Table;
import ru.nsu.dbb.entities.View;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws SQLException {
        ConnectionStorage connectionStorage = new ConnectionStorage();

        Map<String, String> info = new HashMap<>();
        info.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
        ConnectionInfo connectionInfo = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info);

        connectionStorage.addConnectionToStorage(new Connection("sqlite", connectionInfo));

        Connection connection = connectionStorage.getConnection("sqlite"); // click on corresponding tab

        connection.setViews(); // click on corresponding tab

        // For example for View: how to open certain view
        Schema schema = connection.getSchema();
        // Get all views names as string list
        List<String> viewsNamesList = schema.getViewList();
        // This way we choose one view and then get exactly View object (not String)
        View view = schema.getView(viewsNamesList.get(0));
        // Each schema unit have own methods to get required data
        System.out.println(view.getName());
        System.out.println(view.getDefinition());

        connection.setIndexes(); // click on corresponding tab
        connection.setTables(); // click on corresponding tab

        Table table = schema.getTable(schema.getTableList().get(0));
        connection.setForeignKeysFor(table.getName());
        connection.setKeysFor(table.getName());
        connection.setColumnsFor(table.getName());
        connection.setIndexesFor(table.getName());

        // Таким образом мы получили схему всей бд, как брать конкретные элементы описано выше
    }
}