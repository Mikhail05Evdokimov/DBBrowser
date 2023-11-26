import app.MainWindow;
import io.qt.widgets.QApplication;
import io.qt.widgets.QWidget;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main( String[] args ) throws IOException, SQLException {

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

/*tree menu не в отдельном потоке, так как оно может писать в output, принадлежащий mainThread, а другое
потоки этого не могут.
Работа с базой данных не создаёт дэдлок в потоке, так как после работы с ней поток продолжает
выполнение всего, кроме UI
File dialog тоже не виноват, если сделать без него, то всё равно всё ломается
setEnabled(true) к основному окну не помогает ==> оно всё ещё активно
 */
