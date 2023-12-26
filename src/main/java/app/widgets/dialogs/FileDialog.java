package app.widgets.dialogs;

import app.MenuController;
import io.qt.core.QDir;
import io.qt.widgets.QFileDialog;
import io.qt.widgets.QWidget;

import static java.lang.Thread.sleep;

/**
 * Класс для создания диалога с выбором файла базы данных.
 * После выбора файла передаёт путь к нему в LocalStorage
 * через метод LocalStorage.setFilePath
 */
public class FileDialog extends QFileDialog {

    private final Signal1<String> signalWithFilePath = new Signal1<>();

    public FileDialog(QWidget root) {
        //QDir dir = QDir.current();
        QDir dir = new QDir("C:\\Users\\its\\Desktop\\Project");
        this.setDirectory(dir);
        this.fileSelected.connect(this, "fileSelected()");
        connect(this, "signalWithFilePath(String)", root, "fileChosen(String)");
        this.open();
    }

    public FileDialog(MenuController root) {
        QDir dir = new QDir("C:\\Users\\its\\Desktop\\Project");
        this.setDirectory(dir);
        this.fileSelected.connect(this, "fileSelected()");
        connect(this, "signalWithFilePath(String)", root, "fileChosen(String)");
        this.open();
    }

    void fileSelected() {

       // System.out.println(selectedFiles());
        signalWithFilePath.emit(this.selectedFiles().first());
        //LocalStorage.createConnection(this.selectedFiles());
    }

}
