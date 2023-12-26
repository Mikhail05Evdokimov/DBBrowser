import app.backend.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
//    public static void main(String[] args) {
//        // Вот так подключаемся
//        ConnectionStorage connectionStorage = new ConnectionStorage();
//        Map<String, String> info1 = new HashMap<>();
//        info1.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
//        ConnectionInfo connectionInfo1 = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info1);
//        connectionStorage.addConnectionToStorage(new Connection("sqlite1", connectionInfo1));
//        Connection connection1 = connectionStorage.getConnection("sqlite1");
//
//        // Гайд как добавлять удалять индексы
//        // логично, что сначала мы должны добраться до вкладки с индексами таблицы
//        connection1.setTables();
//        connection1.setIndexesFor("NewTable");
//        // смотрим что там до
//        connection1.getSchema().getTable("NewTable").getIndexList().forEach(System.out::println);
//        System.out.println();
//        // при создании надо указать: название_индекса, название_таблицы, будет ли он Unique, Список названий колонок (порядок важен)
//        connection1.createIndex("new_index", "NewTable", true, List.of("Column2", "Column3"));
//        // смотрим, что обновилось локально
//        connection1.getSchema().getTable("NewTable").getIndexList().forEach(System.out::println);
//        System.out.println();
//
//        // вот так всё локально останется так же, а в бд обновится
//        connection1.saveChanges();
//        // а так удалиться локально и сбросится запрос сохранения
//        connection1.discardChanges();
//        // глядим
//        connection1.getSchema().getTable("NewTable").getIndexList().forEach(System.out::println);
//        System.out.println();
//    }

//    public static void main(String[] args) {
//        ConnectionStorage connectionStorage = new ConnectionStorage();
//        Map<String, String> info1 = new HashMap<>();
//        info1.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
//        ConnectionInfo connectionInfo1 = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info1);
//        connectionStorage.addConnectionToStorage(new Connection("sqlite1", connectionInfo1));
//        Connection connection1 = connectionStorage.getConnection("sqlite1");
//
//        connection1.setViews();
//        System.out.println(connection1.getSchema().getViewList());
//        connection1.createView("NewView", "SELECT * FROM artists");
//        System.out.println(connection1.getSchema().getViewList());
//        connection1.saveChanges();
//        System.out.println(connection1.getSchema().getViewList());
//    }

    public static void main(String[] args) {
        ConnectionStorage connectionStorage = new ConnectionStorage();
        Map<String, String> info1 = new HashMap<>();
        info1.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
        ConnectionInfo connectionInfo1 = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info1);
        connectionStorage.addConnectionToStorage(new Connection("sqlite1", connectionInfo1));
        Connection connection1 = connectionStorage.getConnection("sqlite1");
    }

//    public static void main(String[] args) {
//        ConnectionStorage connectionStorage = new ConnectionStorage();
//
//        Map<String, String> info1 = new HashMap<>();
//        info1.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
//        ConnectionInfo connectionInfo1 = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info1);
//
//        Map<String, String> info2 = new HashMap<>();
//        info2.put("path", "C:/Users/parnishkka/Downloads/SQL DB's/my_db");
//        ConnectionInfo connectionInfo2 = new ConnectionInfo(ConnectionInfo.ConnectionType.SQLITE, info2);
//
//        connectionStorage.addConnectionToStorage(new Connection("sqlite1", connectionInfo1));
//        connectionStorage.addConnectionToStorage(new Connection("sqlite2", connectionInfo2));
//
//        Connection connection1 = connectionStorage.getConnection("sqlite1"); // click on corresponding tab
//        Connection connection2 = connectionStorage.getConnection("sqlite2"); // click on corresponding tab
//
//        connection1.connect();
//        connection1.connect();
//
//        connection1.disconnect();
//        connection2.disconnect();
//        System.out.println(connection1.isConnected());
//        connection1.connect();
//        connection1.connect();
//        System.out.println(connection1.isConnected());
//        // my_connection
//        // - tables
//        // connection.getSchema().getTableList();
//        //      - 1
//        //      - 2
//        // - view
//        // - indexes
//
//        // connection.set<smth>();
//        // List<String> names = connection.getSchema().getTableList();
//        // Table table = connection.getSchema().getTable(names.get(2));
//
//        connection1.setViews(); // click on corresponding tab
//
//        // For example for View: how to open certain view
//        Schema schema = connection1.getSchema();
//        // Get all views names as string list
//        List<String> viewsNamesList = schema.getViewList();
//        // This way we choose one view and then get exactly View object (not String)
//        View view = schema.getView(viewsNamesList.get(0));
//        // Each schema unit have own methods to get required data
//        System.out.println(view.getName());
//        System.out.println(view.getDefinition());
//
//        connection1.setIndexes(); // click on corresponding tab
//        connection1.setTables(); // click on corresponding tab
//
//        DataTable dataTable = connection1.getDataFromTable("artists");
//
//        System.out.println(dataTable.getMessage());
//        System.out.println(dataTable.getColumnNames());
//        for (List<String> row : dataTable.getRows()) {
//            for (int i = 0; i < dataTable.getColumnNames().size(); i++) {
//                System.out.print(row.get(i) + " ");
//            }
//            System.out.println();
//        }
//
//        dataTable.getMoreRows(20);
//
//        System.out.println(dataTable.getMessage());
//        System.out.println(dataTable.getColumnNames());
//        for (List<String> row : dataTable.getRows()) {
//            for (int i = 0; i < dataTable.getColumnNames().size(); i++) {
//                System.out.print(row.get(i) + " ");
//            }
//            System.out.println();
//        }
//
//        Table table = schema.getTable(schema.getTableList().get(0));
//        connection1.setForeignKeysFor(table.getName());
//        connection1.setKeysFor(table.getName());
//        connection1.setColumnsFor(table.getName());
//        connection1.setIndexesFor(table.getName());
//
//        connection1.insertData("artists", new ArrayList<>(List.of("6", "newName", "1")));
//        connection1.saveChanges();
//
//        connection1.deleteData("artists", 5);
//        connection1.saveChanges();
//
//        connection1.updateData("artists", 5, new ArrayList<>(List.of(1, 2)), new ArrayList<>(List.of("new1", "12")));
//        connection1.saveChanges();
//
////        for (String s : dataTable.getRows().get(5)) {
////            System.out.println(s);
////        }
//
//        // Таким образом мы получили схему всей бд, как брать конкретные элементы описано выше
//    }
}