package app.widgets.dialogs;

import app.MainWindow;
import app.backend.controllers.ConnectionController;
import app.backend.controllers.StorageController;
import app.backend.entities.ConnectionInfo;
import io.qt.core.Qt;
import io.qt.gui.QIcon;
import io.qt.widgets.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OfflineStartDialog extends StartDialog {

    private final QTextEdit userInput;
    private QTextEdit host;
    private QTextEdit port;
    private QTextEdit dbName;
    private final QTabWidget tabBar;

    public OfflineStartDialog(QIcon icon) {
        this.icon = icon;
        this.setWindowIcon(icon);
        QLayout layout = new QGridLayout( this );
        QLabel label = new QLabel("Please choose the Database file");
        this.setWindowTitle("Welcome to DB browser");
        StorageController.init();

        tabBar = new QTabWidget();
        QToolBar SQLiteTab = new QToolBar();

        userInput = new QTextEdit();
        userInput.setMaximumHeight(27);
        QToolBar userInputBar = new InputItem("file path  ", userInput);

        QPushButton chooseFileButton = newButton("Choose file", "chooseFileClicked()");

        QSplitter splitter = new QSplitter();
        splitter.setFixedSize(5, 5);
        userInputBar.addWidget(splitter);
        userInputBar.addWidget(chooseFileButton);

        QToolBar buttonsBar = new QToolBar();
        buttonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton connectButton = newButton("Connect to DB", "connectClicked()");
        QPushButton cancelButton = newButton("Cancel", "cancelClicked()");
        QPushButton continueButton = newButton("Skip and continue", "skip()");

        buttonsBar.addWidget(continueButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(connectButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(cancelButton);

        QToolBar postgresTab = postgresTab();

        layout.addWidget(label);
        SQLiteTab.addWidget(userInputBar);

        tabBar.addTab(SQLiteTab, "SQLite");
        tabBar.addTab(postgresTab, "Postgres");
        layout.addWidget(tabBar);
        layout.addWidget(buttonsBar);

        this.show();

    }

    private void initPostgres() {
        host = new QTextEdit();
        host.setMaximumHeight(27);
        port = new QTextEdit();
        port.setMaximumHeight(27);
        dbName = new QTextEdit();
        dbName.setMaximumHeight(27);
        login = new QTextEdit();
        login.setMaximumHeight(27);
        password = new QTextEdit();
        password.setMaximumHeight(27);
    }

    void chooseFileClicked() {
        new FileDialog(this);
    }

    void connectClicked() {
        StorageController.connectionStorage.removeConnection(this.userInput.toPlainText());

        if (tabBar.currentIndex() == 0) {
            connectToSQLite();
        }
        if (tabBar.currentIndex() == 1) {
            connectToPostgres();
        }
    }

    private void connectToSQLite() {
        String filePath = this.userInput.toPlainText();
        if (!(filePath.isEmpty())) {
            comeToMain();
            ConnectionController.addConnection(ConnectionInfo.ConnectionType.SQLITE, filePath);
        }
    }

    private void connectToPostgres() {
        String host = this.host.toPlainText();
        if (!(host.isEmpty())) {
            comeToMain();
            ConnectionController.addConnection(ConnectionInfo.ConnectionType.POSTGRESQL, host,
                this.port.toPlainText(), this.dbName.toPlainText(), this.login.toPlainText(),
                this.password.toPlainText());
        }
    }

    private void comeToMain() {
        this.close();
        try {
            new MainWindow(this.icon);
        }
        catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    void fileChosen(String filePath) {
        if (filePath.endsWith(".db")) {
            this.userInput.setText(filePath);
        }
        else {
            new ErrorDialog("Chosen file is not a database file.");
        }
    }

    void skip() {
        comeToMain();
    }

    private QToolBar postgresTab() {
        initPostgres();

        List<QToolBar> toolBars = new ArrayList<>(10);
        toolBars.add(new InputItem("host:  ", host));
        toolBars.add(new InputItem("port:  ", port));
        toolBars.add(new InputItem("dbName:  ", dbName));
        toolBars.add(new InputItem("login:  ", login));
        toolBars.add(new InputItem("password:  ", password));

        QToolBar postgresBar = new QToolBar();
        postgresBar.setOrientation(Qt.Orientation.Vertical);

        for (QToolBar toolBar : toolBars) {
            postgresBar.addWidget(toolBar);
        }

        return postgresBar;
    }

}
