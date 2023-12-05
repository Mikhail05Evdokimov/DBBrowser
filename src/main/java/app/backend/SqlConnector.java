package app.backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import app.backend.table.Row;
import app.backend.table.Table;
import io.qt.widgets.QTableView;
import org.sqlite.JDBC;

/**
 * Класс драйвера для работы с БД
 * Нужно доплнить методами для различных типов запросов,
 * перезапуска соединения, разрыва соединения и тд
 */
public class SqlConnector {

    private static SqlConnector instance = null;
    private final Connection connection;

    /**
     * Создаёт новое соединение с бд
     * @param filePath - путь к файлу БД
     * @return - объект соединения
     * @throws SQLException - если файл не является базой данных (fake)
     */
    private Connection getNewConnection(String filePath) throws SQLException {
        String url = "jdbc:sqlite:" + filePath;
        return DriverManager.getConnection(url);
    }

    /**
     * Возвращает объект драйвера для работы с БД.
     * @param filePath - путь к файлу БД
     * @throws SQLException в случае ошибки
     */
    public static synchronized SqlConnector getInstance(String filePath) throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed())
            instance = new SqlConnector(filePath);
        return instance;
    }

    /**
     * Конструктор класса, исполбзуется не напрямую, а в методе getInstance()
     * @param filePath - путь к файлу БД
     */
    private SqlConnector(String filePath) throws SQLException {
        // Регистрируем драйвер, с которым будем работать
        // в нашем случае Sqlite
        DriverManager.registerDriver(new JDBC());
        if (filePath.endsWith(".db")) {
            Connection connectTry = getNewConnection(filePath);
            if (connectTry.isValid(1)) {
                // Выполняем подключение к базе данных
                LocalStorage.setOutputText("Successful connection");
                this.connection = connectTry;
            }
            else {
                LocalStorage.setOutputText("Failed to connect");
                this.connection = null;
            }
        }
        else {
            LocalStorage.setOutputText("ERROR: Chosen file is not a database file");
            this.connection = null;
        }

    }

    /**
     * https://alekseygulynin.ru/rabota-s-sqlite-v-java/
     * Здесь ещё примеры для delete и patch апросов.
     * getAllProducts не используется, так как сделан под конкретные данные, но в качестве образца сойдёт.
     * Соответственно класс Category тоже потом надо будет удалить.
     */

    public List<Category> getAllProducts() {

        // Statement используется для того, чтобы выполнить sql-запрос
        try (Statement statement = this.connection.createStatement()) {
            // В данный список будем загружать наши продукты, полученные из БД
            List<Category> products = new ArrayList<>();
            // В resultSet будет храниться результат нашего запроса,
            // который выполняется командой statement.executeQuery()
            ResultSet resultSet = statement.executeQuery("SELECT category_id, category_name, category_weight FROM category");
            // Проходимся по нашему resultSet и заносим данные в products
            while (resultSet.next()) {
                products.add(new Category(resultSet.getInt("category_id"),
                    resultSet.getString("category_name"),
                    resultSet.getString("category_weight")));
            }
            // Возвращаем наш список
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
    }

    /**
     * метод, исполняющий GET запрос
     * @param query - содержимое зароса
     * @return - результат запроса в виде списка строк. Каждая строка - одна запись таблицы
     */

    public Table execQuery(String query) {
        Table resultTable;
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            List<String> columnNames = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnsNumber = metaData.getColumnCount();

            for (int i = 1; i <= columnsNumber; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            List<Row> rowList = new ArrayList<>();

            while (resultSet.next()) {
                List<String> currentRow = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    currentRow.add(resultSet.getString(i));
                }
                rowList.add(new Row(currentRow));
            }

            resultTable = new Table(columnNames, rowList);

        } catch (SQLException e) {
            return null;
        }
        return resultTable;
    }

    public List<String> getSchema() throws SQLException {
        DatabaseMetaData dbmd = this.connection.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, null, null);
        List<String> resultList = new ArrayList<>();
        while(rs.next()) {
            resultList.add(rs.getString("TABLE_NAME"));
        }

        return resultList;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
