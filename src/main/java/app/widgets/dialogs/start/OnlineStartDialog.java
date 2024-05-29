package app.widgets.dialogs.start;

import app.MainWindow;
import app.api.ApiCalls;
import app.backend.controllers.StorageController;
import app.styles.WoodPallet;
import app.widgets.dialogs.ErrorDialog;
import app.widgets.dialogs.InputItem;
import io.qt.core.QRect;
import io.qt.core.Qt;
import io.qt.gui.QIcon;
import io.qt.widgets.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnlineStartDialog extends StartDialog {

    public OnlineStartDialog(QIcon icon) {
        StorageController.init();

        this.icon = icon;
        this.setWindowIcon(icon);
        //this.setStyle(new QCommonStyle());
        //this.setPalette(new WoodPallet());
        QLayout layout = new QGridLayout( this );
        QLabel label = new QLabel("Please sign-in or switch to offline mode");
        this.setWindowTitle("Welcome to DB browser");

        QToolBar buttonsBar = new QToolBar();
        buttonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton connectButton = newButton("Log in", "connectClicked()");
        QPushButton cancelButton = newButton("Cancel", "cancelClicked()");
        QPushButton continueButton = newButton("Work offline", "skip()");
        //cancelButton.setPalette(new WoodPallet());

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

    private void initInputs() {
        login = new QTextEdit();
        login.setMaximumHeight(27);
        password = new QTextEdit();
        password.setMaximumHeight(27);
    }

    private QToolBar logInTab() {

        initInputs();

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

        if (!login.toPlainText().equals("") && !password.toPlainText().equals("")) {
            ApiCalls.signIn(this, login.toPlainText(), password.toPlainText());
        }

    }

    void skip() {
        this.close();
        new OfflineStartDialog(this.icon);
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

    void result(String res) {
        if(Objects.equals(res, "0")) {
            comeToMain();
        }
        else {
            if(Objects.equals(res, "1")) {
                new ErrorDialog("Wrong login/password");
            }
            else {
                new ErrorDialog("Cannot connect to server");
            }
        }
    }

}
