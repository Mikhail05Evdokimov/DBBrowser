package app.widgets.dialogs.settings;

import app.widgets.MyToolBar;
import app.widgets.dialogs.ErrorDialog;
import io.qt.core.Qt;
import io.qt.widgets.*;

public class ChangePasswordDialog extends QDialog {

    public QTextEdit passwordOld;
    public QTextEdit passwordNew;

    public ChangePasswordDialog() {

        QToolBar mainBar = new QToolBar();
        initInputs();
        mainBar.setOrientation(Qt.Orientation.Vertical);
        mainBar.addWidget(new QLabel("Enter your old password"));
        mainBar.addWidget(passwordOld);
        mainBar.addWidget(new QLabel("Enter new password"));
        mainBar.addWidget(passwordNew);
        MyToolBar buttonsBar = new MyToolBar();
        buttonsBar.setOrientation(Qt.Orientation.Horizontal);
        buttonsBar.addWidgetAndSeparator(newButton("Cancel", "cancelClicked()"));
        buttonsBar.addWidget(newButton("Approve", "approveClicked()"));
        mainBar.addWidget(buttonsBar);
        QGridLayout layout = new QGridLayout();
        layout.addWidget(mainBar);
        this.setLayout(layout);
        this.show();
    }

    private void initInputs() {
        passwordOld = new QTextEdit();
        passwordOld.setText("");
        passwordOld.setMaximumHeight(27);
        passwordNew = new QTextEdit();
        passwordNew.setText("");
        passwordNew.setMaximumHeight(27);
    }

    private QPushButton newButton(String text, String signal) {
        QPushButton button = new QPushButton(text);
        button.clicked.connect(this, signal);
        return button;
    }

    void cancelClicked() {
        this.close();
    }

    void approveClicked() {
        if (!passwordOld.toPlainText().equals("") && !passwordNew.toPlainText().equals("")) {
            //some logic with server
            this.close();
        }
        else {
            new ErrorDialog("Empty input");
        }
    }

}
