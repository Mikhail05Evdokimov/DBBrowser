package app.widgets.dialogs.settings;

import app.api.UserDataRepository;
import app.widgets.MyToolBar;
import app.widgets.dialogs.InputItem;
import io.qt.core.Qt;
import io.qt.widgets.QGridLayout;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QTextEdit;
import io.qt.widgets.QToolBar;

import java.util.ArrayList;
import java.util.List;

public class AccountSettings extends MyToolBar {

    public QTextEdit login;
    public QTextEdit password;

    public AccountSettings() {

        this.addWidget(logInTab());

    }

    private void initInputs() {
        login = new QTextEdit();
        login.setText(UserDataRepository.userData.login);
        login.setReadOnly(true);
        login.setMaximumHeight(27);
        password = new QTextEdit();
        password.setReadOnly(true);
        String passwordLine = UserDataRepository.userData.password; // **********
        password.setText(passwordLine);
        password.setMaximumHeight(27);
    }

    private QToolBar logInTab() {

        initInputs();

        List<QToolBar> toolBars = new ArrayList<>(4);
        toolBars.add(new InputItem("login:  ", login));
        toolBars.add(new InputItem("password:  ", password));
        QPushButton changePasswordButton = new QPushButton("Change password");
        changePasswordButton.clicked.connect(this, "changePassword()");

        QToolBar loginPasswordInBar = new QToolBar();
        loginPasswordInBar.setOrientation(Qt.Orientation.Vertical);

        for (QToolBar toolBar : toolBars) {
            loginPasswordInBar.addWidget(toolBar);
        }
        loginPasswordInBar.addWidget(changePasswordButton);

        return loginPasswordInBar;
    }

    void changePassword() {
        new ChangePasswordDialog();
    }

}
