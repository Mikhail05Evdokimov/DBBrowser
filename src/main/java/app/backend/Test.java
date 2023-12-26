package app.backend;

import app.backend.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        ConnectionStorage connectionStorage = new ConnectionStorage();

        Map<String, String> info = new HashMap<>();
        info.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
        ConnectionInfo connectionInfo = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info);

        connectionStorage.addConnectionToStorage(new Connection("sqlite", connectionInfo));

        Connection connection = connectionStorage.getConnection("sqlite"); // click on corresponding tab

        connection.disconnect();
        System.out.println(connection.isConnected());
        connection.connect();
        System.out.println(connection.isConnected());
        // my_connection
        // - tables
        // connection.getSchema().getTableList();
        //      - 1
        //      - 2
        // - view
        // - indexes

        // connection.set<smth>();
        // List<String> names = connection.getSchema().getTableList();
        // Table table = connection.getSchema().getTable(names.get(2));

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

        DataTable dataTable = connection.getDataFromTable("artists");

        System.out.println(dataTable.getMessage());
        System.out.println(dataTable.getColumnNames());
        for (List<String> row : dataTable.getRows()) {
            for (int i = 0; i < dataTable.getColumnNames().size(); i++) {
                System.out.print(row.get(i) + " ");
            }
            System.out.println();
        }

        dataTable.getMoreRows(20);

        System.out.println(dataTable.getMessage());
        System.out.println(dataTable.getColumnNames());
        for (List<String> row : dataTable.getRows()) {
            for (int i = 0; i < dataTable.getColumnNames().size(); i++) {
                System.out.print(row.get(i) + " ");
            }
            System.out.println();
        }

        Table table = schema.getTable(schema.getTableList().get(0));
        connection.setForeignKeysFor(table.getName());
        connection.setKeysFor(table.getName());
        connection.setColumnsFor(table.getName());
        connection.setIndexesFor(table.getName());

        connection.insertData("artists", new ArrayList<>(List.of("6", "newName", "1")));
        connection.saveChanges();

        connection.deleteData("artists", 5);
        connection.saveChanges();

        connection.updateData("artists", 5, new ArrayList<>(List.of(1, 2)), new ArrayList<>(List.of("new1", "12")));
        connection.saveChanges();

//        for (String s : dataTable.getRows().get(5)) {
//            System.out.println(s);
//        }

        // Таким образом мы получили схему всей бд, как брать конкретные элементы описано выше
    }
}