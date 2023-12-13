package app.backend;

import app.MenuController;
import app.backend.table.Table;
import io.qt.core.QObject;
import io.qt.widgets.QLabel;

import java.sql.SQLException;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Костыльный слой для работы базой данных и вывода результатов в UI.
 * Обходит проблему асинхронности, модульности и тд.
 * Надо будет потом сильно пофиксить
 */
public class LocalStorage {

    private static String filePath;
    private static SqlConnector dbHandler;
    private static QLabel output;
    private static String query;
    private static MenuController menuController;
    private static Signaller signaller;

    /**
     * Костыль, который передаёт ссылку на объект UI для вывода результатов.
     * Вызывается в момент инициализации главной страницы.
     * @param output - текстовое поле для вывода результатов в UI.
     */
    public static void setOutput(QLabel output) {
        LocalStorage.output = output;
    }

    public static void setMenuController(MenuController controller) {
        LocalStorage.menuController = controller;
        signaller = new Signaller(controller);
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

    /**
     * Получить схему БД
     */
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
    public static void createConnection(String filePath) throws SQLException {
        LocalStorage.filePath = filePath;
        LocalStorage.dbHandler = SqlConnector.getInstance(getFilePath());
        //signal to tree
        signaller.emitSignalToTree(TreeSignals.SHOW);
        signaller.emitSignalToDBInfo();
    }

    /**
     * Парсит список строк с путём к файлу БД
     * @return - первая строка в списке - путь к выбранной бд
     */
    public static String getFilePath() {
        return LocalStorage.filePath;
    }

    public static void closeConnection() {
        if(LocalStorage.dbHandler != null) {
            LocalStorage.dbHandler.closeConnection();
            output.setText("Connection closed");
            signaller.emitSignalToTree(TreeSignals.HIDE);
        }
    }

    public static void reconnectToDB() throws SQLException, InterruptedException {
        LocalStorage.dbHandler.closeConnection();
        signaller.emitSignalToTree(TreeSignals.HIDE);
        LocalStorage.dbHandler = SqlConnector.getInstance(getFilePath());
        sleep(500);
        signaller.emitSignalToTree(TreeSignals.SHOW);
        output.setText("Successful reconnection");
    }

    public static List<String> getChildren(String name) throws SQLException {
        return LocalStorage.dbHandler.getChildren(name);
    }

    public static List<String> getDBName() {
        return LocalStorage.dbHandler.getDBName();
    }

}
