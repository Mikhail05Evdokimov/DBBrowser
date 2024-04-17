import app.IconLoader;
import app.widgets.dialogs.OfflineStartDialog;
import app.widgets.dialogs.OnlineStartDialog;
import io.qt.widgets.QApplication;

import java.io.IOException;

public class Main {

    public static void main( String[] args ) throws IOException {

        // Создаём Qt приложение, что бы инициализировать библиотеку
        // и графическую подсистему.

        QApplication app = QApplication.initialize(args);

        IconLoader iconLoader = new IconLoader();
        OnlineStartDialog onlineStartDialog = new OnlineStartDialog(iconLoader.loadIcon("../icon.ico"));

        QApplication.exec();

        QApplication.shutdown();

    }
}


