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
        info.put("username", "postgres");
        info.put("password", "652385");
        info.put("host", "localhost");
        info.put("port", "5432");
        info.put("dbname", "demo");
        ConnectionInfo connectionInfo = new ConnectionInfo(ConnectionInfo.ConnectionType.POSTGRESQL, info);
        connectionStorage.addConnectionToStorage(new Connection("postgres", connectionInfo));
        Connection connection = connectionStorage.getConnection("postgres");
        System.out.println(connection.isConnected());

        // Добавляем базы данных из конекшена
        connection.setDatabaseList();
        List<String> databaseList = connection.getDatabaseList();
        System.out.println(connection.getDatabaseList());

        // Добавляем схемы для для первой базы данных и далее работаем с ней
        connection.setSchemasFor(databaseList.get(0));
        List<Schema> schemas = connection.getDatabase(databaseList.get(0)).getSchemaList();
        System.out.println("Schemas: " + schemas.get(0).getName());// первая это INFORMATION_SCHEMA это нам не надо, нам надо PUBLIC она вторая
        Schema actualSchema = schemas.get(0); // Схема, с котороый мы работаем
        connection.setSchema(actualSchema);

        // Добавляем и получаем таблицы для PUBLIC
        System.out.println("Таблицы: ");
        connection.setTables();
        List<String> tables = actualSchema.getTableList();
        System.out.println(tables);

        // колонки для таблиц
        System.out.println("Колонки:");
        for (String table : tables){
            connection.setColumnsFor(table);
            System.out.println(table + ": " + actualSchema.getTable(table).getColumnList());
        }
        // Записываем внешние ключи
        for (String table : tables){
            connection.setForeignKeysFor(table);
            System.out.println(table + ": " + actualSchema.getTable(table).getForeignKeyList());
        }
        // Записываем ключи
        for (String table : tables){
            connection.setKeysFor(table);
            System.out.println(table + ": " + actualSchema.getTable(table).getKeyList());
        }

        DataTable dataTable = connection.getDataFromTable("flights");
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
//
//        dataTable = connection.insertData("flights", new ArrayList<>(List.of("3", "Alice", "Johnson", "alice.johnson@example.com", "2345678901")));
//        System.out.println(dataTable.getMessage());
//        System.out.println(dataTable.getColumnNames());
//        for (List<String> row : dataTable.getRows()) {
//            for (int i = 0; i < dataTable.getColumnNames().size(); i++) {
//                System.out.print(row.get(i) + " ");
//            }
//            System.out.println();
//        }

//        dataTable = connection.updateData("flights", 0, new ArrayList<>(List.of(6)), new ArrayList<>(List.of("Scheduled")));
//        System.out.println(dataTable.getMessage());
//        System.out.println(dataTable.getColumnNames());
//        for (List<String> row : dataTable.getRows()) {
//            for (int i = 0; i < dataTable.getColumnNames().size(); i++) {
//                System.out.print(" " + row.get(i) + " ");
//            }
//            System.out.println();
//        }
//
//
//        DataTable dataTableBooking = connection.getDataFromTable("booking");
//        dataTableBooking = connection.deleteData("booking", 0);
//        System.out.println(dataTableBooking.getMessage());
//        System.out.println(dataTableBooking.getColumnNames());
//        for (List<String> row : dataTableBooking.getRows()) {
//            for (int i = 0; i < dataTableBooking.getColumnNames().size(); i++) {
//                System.out.print(row.get(i) + " ");
//            }
//            System.out.println();
//        }
//
//        connection.setIndexes();
//        System.out.println(actualSchema.getIndexes());
    }
}