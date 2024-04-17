package app.widgets.dialogs;

import app.MainWindow;
import app.backend.controllers.ConnectionController;
import app.backend.controllers.StorageController;
import app.backend.entities.ConnectionInfo;
import io.qt.core.Qt;
import io.qt.gui.QIcon;
import io.qt.widgets.*;

import java.io.IOException;

public class StartDialog extends QDialog {

    private final QTextEdit userInput;
    private final QIcon icon;
    private QTextEdit host;
    private QTextEdit port;
    private QTextEdit dbName;
    private QTextEdit login;
    private QTextEdit password;
    private final QTabWidget tabBar;

    public StartDialog(QIcon icon) {
        this.icon = icon;
        this.setWindowIcon(icon);
        QLayout layout = new QGridLayout( this );
        QLabel label = new QLabel("Please choose the Database file");
        this.setWindowTitle("Welcome to DB browser");
        StorageController.init();

        tabBar = new QTabWidget();
        QToolBar SQLiteTab = new QToolBar();
        QToolBar postgresTab = new QToolBar();

        userInput = new QTextEdit();
        userInput.setMaximumHeight(27);
        QToolBar userInputBar = newInputItem("file path  ", userInput);

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

        initPostgres();

        QToolBar hostInputBar = newInputItem("host:  ", host);

        QToolBar portInputBar = newInputItem("port:  ", port);

        QToolBar dbNameInputBar = newInputItem("dbName:  ", dbName);

        QToolBar loginInputBar = newInputItem("login:  ", login);

        QToolBar passwordInputBar = newInputItem("password:  ", password);

        QToolBar postgresBar = new QToolBar();
        postgresBar.setOrientation(Qt.Orientation.Vertical);
        postgresBar.addWidget(hostInputBar);
        postgresBar.addWidget(portInputBar);
        postgresBar.addWidget(dbNameInputBar);
        postgresBar.addWidget(loginInputBar);
        postgresBar.addWidget(passwordInputBar);

        layout.addWidget(label);
        SQLiteTab.addWidget(userInputBar);
        postgresTab.addWidget(postgresBar);
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
        else {
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
            new MainWindow(icon);
        }
        catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    void cancelClicked() {
        this.close();
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

    private QPushButton newButton(String text, String signal) {
        QPushButton button = new QPushButton(text);
        button.clicked.connect(this, signal);
        return button;
    }

    private QToolBar newInputItem(String text, QWidget widget) {
        QToolBar toolBar = new QToolBar();
        toolBar.setOrientation(Qt.Orientation.Horizontal);
        toolBar.addWidget(new QLabel(text));
        toolBar.addWidget(widget);
        return toolBar;
    }

}
