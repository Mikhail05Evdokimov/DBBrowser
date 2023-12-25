import app.IconLoader;
import app.widgets.dialogs.StartDialog;
import io.qt.widgets.QApplication;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) throws IOException {

        // Создаём Qt приложение, что бы инициализировать библиотеку
        // и графическую подсистему.

        QApplication app = QApplication.initialize(args);

        IconLoader iconLoader = new IconLoader();
        StartDialog startDialog = new StartDialog(iconLoader.loadIcon("../icon.ico"));
        //QWidget window = new MainWindow(iconLoader.loadIcon("../icon.ico"));

        //window.show();

        QApplication.exec();

        QApplication.shutdown();

    }
}


