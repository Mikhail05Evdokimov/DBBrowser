package ru.nsu.dbb;

import io.qt.widgets.*;
import org.postgresql.Driver;
import org.postgresql.ds.PGSimpleDataSource;
import org.sqlite.JDBC;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Test {
    public static void main(String[] args) throws SQLException {
        //        QApplication.initialize(args);
        //        QMessageBox.information(null, "QtJambi", "Hello World!");
        //        QApplication.shutdown();

//        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
//        sqLiteDataSource.setUrl("jdbc:sqlite:C:/Users/parnishkka/Downloads/SQL DB's/chinook.db");
//        Connection connection = sqLiteDataSource.getConnection();



        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setServerNames(new String[]{"localhost"});
        pgSimpleDataSource.setPortNumbers(new int[]{5432});
        pgSimpleDataSource.setDatabaseName("AppRentOfInstruments");
        pgSimpleDataSource.setUser("postgres");
        pgSimpleDataSource.setPassword("31032003");
        Connection connection = pgSimpleDataSource.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getCatalogs();
        List<String> catalogs = new ArrayList<>();
        while (resultSet.next()) {
            catalogs.add(resultSet.getString("TABLE_CAT"));
        }
        List<String> schemas = new ArrayList<>();
        for (String c : catalogs) {
            resultSet = databaseMetaData.getSchemas(c, null);
            while (resultSet.next()) {
                schemas.add(resultSet.getString("TABLE_SCHEM"));
            }
        }
        schemas.stream().forEach(System.out::println);
    }
}