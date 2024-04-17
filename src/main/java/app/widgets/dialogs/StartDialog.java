package app.widgets.dialogs;

import io.qt.gui.QIcon;
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
    }

    void skip() {}

    void connectClicked() {}

}
