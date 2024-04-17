package app.widgets.dialogs;

import io.qt.core.Qt;
import io.qt.gui.QIcon;
import io.qt.widgets.*;

import java.util.ArrayList;
import java.util.List;

public class OnlineStartDialog extends StartDialog {

    public OnlineStartDialog(QIcon icon) {
        this.icon = icon;
        this.setWindowIcon(icon);
        QLayout layout = new QGridLayout( this );
        QLabel label = new QLabel("Please sign-in or switch to offline mode");
        this.setWindowTitle("Welcome to DB browser");

        QToolBar buttonsBar = new QToolBar();
        buttonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton connectButton = newButton("Log in", "connectClicked()");
        QPushButton cancelButton = newButton("Cancel", "cancelClicked()");
        QPushButton continueButton = newButton("Work offline", "skip()");

        buttonsBar.addWidget(continueButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(connectButton);
        buttonsBar.addWidget(new QSplitter());
        buttonsBar.addWidget(cancelButton);

        layout.addWidget(label);
        QToolBar logInTab = logInTab();
        layout.addWidget(logInTab);

        layout.addWidget(buttonsBar);

        this.show();

    }

    private void initLogin() {
        login = new QTextEdit();
        login.setMaximumHeight(27);
        password = new QTextEdit();
        password.setMaximumHeight(27);
    }

    private QToolBar logInTab() {

        initLogin();

        List<QToolBar> toolBars = new ArrayList<>(4);
        toolBars.add(new InputItem("login:  ", login));
        toolBars.add(new InputItem("password:  ", password));

        QToolBar logInBar = new QToolBar();
        logInBar.setOrientation(Qt.Orientation.Vertical);

        for (QToolBar toolBar : toolBars) {
            logInBar.addWidget(toolBar);
        }

        return logInBar;
    }

    void connectClicked() {

    }

    void skip() {
        this.close();
        new OfflineStartDialog(this.icon);
    }

}
