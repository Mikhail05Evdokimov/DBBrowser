import app.IconLoader;
import app.widgets.dialogs.start.OnlineStartDialog;
import io.qt.widgets.QApplication;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) throws IOException {

        // Создаём Qt приложение, чтобы инициализировать библиотеку
        // и графическую подсистему.

        QApplication app = QApplication.initialize(args);

        IconLoader iconLoader = new IconLoader();
        OnlineStartDialog onlineStartDialog = new OnlineStartDialog(iconLoader.loadIcon("../icon.ico"));

        QApplication.exec();

        QApplication.shutdown();

    }
}


