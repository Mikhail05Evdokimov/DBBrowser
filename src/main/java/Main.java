import app.MainWindow;
import io.qt.widgets.QApplication;
import io.qt.widgets.QWidget;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) throws IOException {

        // Создаём Qt приложение, что бы инициализировать библиотеку
        // и графическую подсистему.

        //app.DBThread dbThread = new app.DBThread(args);
        //dbThread.start();

        QApplication app = QApplication.initialize(args);

        IconLoader iconLoader = new IconLoader();
        QWidget window = new MainWindow(iconLoader.loadIcon());

        window.show();

        QApplication.exec();

        QApplication.shutdown();
    }
}


