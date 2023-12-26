package app.widgets.dialogs;

import app.MainWindow;
import app.backend.controllers.ConnectionController;
import app.backend.controllers.StorageController;
import app.backend.entities.ConnectionInfo;
import io.qt.core.Qt;
import io.qt.gui.QIcon;
import io.qt.widgets.*;

import java.io.IOException;
import java.sql.SQLException;

public class StartDialog extends QDialog {

    private final QTextEdit userInput;
    private final QIcon icon;
    private QTextEdit host;
    private QTextEdit port;
    private QTextEdit dbName;
    private QTextEdit login;
    private QTextEdit password;
    QTabWidget tabBar;

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
        QToolBar userInputBar = new QToolBar();
        userInputBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton chooseFileButton = new QPushButton("Choose file");
        chooseFileButton.clicked.connect(this, "chooseFileClicked()");
        userInputBar.addWidget(new QLabel("file path  "));
        userInputBar.addWidget(userInput);
        QSplitter splitter = new QSplitter();
        splitter.setFixedSize(5, 5);
        userInputBar.addWidget(splitter);
        userInputBar.addWidget(chooseFileButton);

        QToolBar buttonsBar = new QToolBar();
        buttonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton connectButton = new QPushButton("Connect to DB");
        QPushButton cancelButton = new QPushButton("Cancel");
        connectButton.clicked.connect(this, "connectClicked()");
        cancelButton.clicked.connect(this, "cancelClicked()");
        QPushButton continueButton = new QPushButton("Skip and continue");
        continueButton.clicked.connect(this, "skip()");

        buttonsBar.addWidget(continueButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(connectButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(cancelButton);

        initPostgres();
        QToolBar hostInputBar = new QToolBar();
        hostInputBar.setOrientation(Qt.Orientation.Horizontal);
        hostInputBar.addWidget(new QLabel("host:  "));
        hostInputBar.addWidget(host);

        QToolBar portInputBar = new QToolBar();
        portInputBar.setOrientation(Qt.Orientation.Horizontal);
        portInputBar.addWidget(new QLabel("port:  "));
        portInputBar.addWidget(port);

        QToolBar dbNameInputBar = new QToolBar();
        dbNameInputBar.setOrientation(Qt.Orientation.Horizontal);
        dbNameInputBar.addWidget(new QLabel("dbName:  "));
        dbNameInputBar.addWidget(dbName);

        QToolBar loginInputBar = new QToolBar();
        loginInputBar.setOrientation(Qt.Orientation.Horizontal);
        loginInputBar.addWidget(new QLabel("login:  "));
        loginInputBar.addWidget(login);

        QToolBar passwordInputBar = new QToolBar();
        passwordInputBar.setOrientation(Qt.Orientation.Horizontal);
        passwordInputBar.addWidget(new QLabel("password:  "));
        passwordInputBar.addWidget(password);

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
        //layout.addWidget(userInputBar);
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

    void connectClicked() throws IOException, SQLException {

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

}
