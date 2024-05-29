package app.widgets.dialogs.start;

import io.qt.gui.QIcon;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDialog;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QTextEdit;

public abstract class StartDialog extends QDialog {

    public QIcon icon;
    public QTextEdit login;
    public QTextEdit password;

    public QPushButton newButton(String text, String signal) {
        QPushButton button = new QPushButton(text);
        button.clicked.connect(this, signal);
        return button;
    }

    void cancelClicked() {
        this.close();
        QApplication.exit();
    }

    void skip() {}

    void connectClicked() {}

}
