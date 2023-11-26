package app.backend;

import io.qt.widgets.QTextEdit;

import java.sql.SQLException;
import java.util.List;

/**
 * Костыльный слой для работы базой данных и вывода результатов в UI.
 * Обходит проблему асинхронности, модульности и тд.
 * Надо будет потом сильно пофиксить
 */
public class LocalStorage {

    private static List<String> filePath;
    private static SqlConnector dbHandler;
    private static QTextEdit output;
    private static String query;

    /**
     * Костыль, который передаёт ссылку на объект UI для вывода результатов.
     * Вызывается в момент инициализации главной страницы.
     * @param output - текстовое поле для вывода результатов в UI.
     */
    public static void setOutput(QTextEdit output) {
        LocalStorage.output = output;
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
    public static void execQuery() throws SQLException {
        String finalText = "";
        for (String i : dbHandler.execQuery(query)) {
            finalText = finalText.concat(i + "\n");
        }
        output.setText(finalText);
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
        dbHandler = SqlConnector.getInstance(getFilePath());
    }

    /**
     * Парсит список строк с путём к файлу БД
     * @return - первая строка в списке - путь к выбранной бд
     */
    public static String getFilePath() {
        return LocalStorage.filePath.get(0);
    }

}
