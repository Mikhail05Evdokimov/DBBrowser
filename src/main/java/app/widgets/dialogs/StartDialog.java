package app.widgets.dialogs;

import app.MainWindow;
import app.backend.LocalStorage;
import io.qt.core.Qt;
import io.qt.gui.QIcon;
import io.qt.widgets.*;

import java.io.IOException;
import java.sql.SQLException;

public class StartDialog extends QDialog {

    private final QTextEdit userInput;
    private final QIcon icon;

    public StartDialog(QIcon icon) {
        this.icon = icon;
        this.setWindowIcon(icon);
        QLayout layout = new QGridLayout( this );
        QLabel label = new QLabel("Please choose the Database file");
        this.setWindowTitle("Welcome to DB browser");

        userInput = new QTextEdit();
        userInput.setMaximumHeight(25);
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

        buttonsBar.addWidget(connectButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(cancelButton);

        layout.addWidget(label);
        layout.addWidget(userInputBar);
        layout.addWidget(buttonsBar);

        this.show();

    }

    void chooseFileClicked() {
        new FileDialog(this);
    }

    void connectClicked() throws IOException, SQLException {
        String filePath = this.userInput.toPlainText();
        if (!(filePath.isEmpty())) {
            this.close();
            new MainWindow(icon);
            LocalStorage.createConnection(filePath);
        }
    }

    void cancelClicked() {
        this.close();
    }

    void fileChosen(String filePath) {
        this.userInput.setText(filePath);
    }

}
