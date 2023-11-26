package app;

import app.backend.LocalStorage;

import java.sql.SQLException;
import java.util.List;

/**
 * Тут можно создать поток для работы с базой. Пробовал это для решения дедлока.
 * Оказалось, что дедлока нет или он сложнее, чем я думал, но всё равно потом может пригодиться.
 */
public class DBThread extends Thread{

    //private static SqlConnector dbHandler;
    private final List<String> selectedFiles;

    public DBThread(List<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    @Override
    public void run(){
        //dbHandler = SqlConnector.getInstance(getFilePath());
        try {
            LocalStorage.setFilePath(this.selectedFiles);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
