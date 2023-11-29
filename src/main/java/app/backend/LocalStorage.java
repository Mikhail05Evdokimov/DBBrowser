package app.backend;

import app.backend.table.Row;
import app.backend.table.Table;
import io.qt.widgets.QLabel;
import io.qt.widgets.QTextEdit;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Костыльный слой для работы базой данных и вывода результатов в UI.
 * Обходит проблему асинхронности, модульности и тд.
 * Надо будет потом сильно пофиксить
 */
public class LocalStorage {

    private static List<String> filePath;
    private static SqlConnector dbHandler;
    private static QLabel output;
    private static String query;

    /**
     * Костыль, который передаёт ссылку на объект UI для вывода результатов.
     * Вызывается в момент инициализации главной страницы.
     * @param output - текстовое поле для вывода результатов в UI.
     */
    public static void setOutput(QLabel output) {
        LocalStorage.output = output;
    }

    public static void setOutputText(String text) {
        output.setText(text);
    }

    /**
     * сохраняет текущий текст запроса
     * что в данный момент реально не используется, но нужно в дальнейшем для нормальной
     * организации работы с приложением и возможно промежуточной обработки зароса
     *
     * @param query - содержимое запроса
     */
    public static void setQuery(String query) {
        LocalStorage.query = query;
    }

    /**
     * Обратиться к dbHandler чтобы исполнить sql запрос
     * @throws SQLException - при некорректном запросе в результат
     * запишется сообщение об ошибке.
     */
    public static Table execQuery() throws SQLException {

        return dbHandler.execQuery(query);
    }

    public static List<String> showSchema() throws SQLException {
        return dbHandler.getSchema();

    }

    /**
     * установить путь к базе данных и создать соединение.
     * Сохраняет в LocalStorage.dbHandler объект для работы с бд
     * @param filePath - путь к базе данных в виде списка строк
     *                 (файл диалог возвращает список строк с выбранными файлами,
     *                 берётся первый из списка)
     * @throws SQLException - если не получилось создать соединение
     *                 надо потом обработчик ошибок навесить, чтобы он писал юзеру
     *                 что именно не так.
     */
    public static void setFilePath(List<String> filePath) throws SQLException {
        LocalStorage.filePath = filePath;
        LocalStorage.dbHandler = SqlConnector.getInstance(getFilePath());
    }

    /**
     * Парсит список строк с путём к файлу БД
     * @return - первая строка в списке - путь к выбранной бд
     */
    public static String getFilePath() {
        if (LocalStorage.filePath == null) {
            return null;
        }
        return LocalStorage.filePath.get(0);
    }

}
