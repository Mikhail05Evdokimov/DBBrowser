package app.widgets.dialogs;

import app.MainWindow;
import app.backend.LocalStorage;
import io.qt.core.QDir;
import io.qt.widgets.QFileDialog;

import java.io.IOException;
import java.sql.SQLException;

import static java.lang.Thread.sleep;

/**
 * Класс для создания диалога с выбором файла базы данных.
 * После выбора файла передаёт путь к нему в LocalStorage
 * через метод LocalStorage.setFilePath
 */
public class FileDialog extends QFileDialog {
    private static MainWindow root;

    public FileDialog(MainWindow root) {
        FileDialog.root = root;
        QDir dir = new QDir("src/main/resources");
        this.setDirectory(dir);
        this.fileSelected.connect(this, "fileSelected()");
        this.open();
    }

    void fileSelected() throws SQLException, InterruptedException, IOException {
        System.out.println(selectedFiles());
        //DBThread dbThread = new DBThread(selectedFiles());
        //dbThread.start();
        LocalStorage.setFilePath(this.selectedFiles());

        sleep(1000); //имитация думания при подключении к БД, чтобы юзеру жизнь мёдом не казалась
    }

}
