package app.widgets.dialogs;

import io.qt.widgets.*;

import static io.qt.core.Qt.WindowType.WindowStaysOnTopHint;

public class CustomCheckBoxDialog extends QDialog {

    CheckBoxChecker check1;
    CheckBoxChecker check2;
    QWidget root;

    public CustomCheckBoxDialog(QWidget root) {
        this.root = root;
        this.finished.connect(this, "enableControl()");
        this.setWindowFlag(WindowStaysOnTopHint);
        QLabel label = new QLabel("Cringe check box");

        this.setWindowTitle("Title");
        QCheckBox checkBox1 = new QCheckBox();
        checkBox1.setText("Point1");
        QCheckBox checkBox2 = new QCheckBox();
        checkBox2.setText("Point2");

        check1 = new CheckBoxChecker();
        checkBox1.stateChanged.connect(check1, "inverse()");
        check2 = new CheckBoxChecker();
        checkBox2.stateChanged.connect(check2, "inverse()");

        QPushButton applyButton = new QPushButton("Apply");
        applyButton.clicked.connect(this, "applyClicked()");

        QPushButton abortButton = new QPushButton("Abort");
        abortButton.clicked.connect(this, "abortClicked()");

        QLayout layout = new QGridLayout(this);
        layout.addWidget(label);
        layout.addWidget(checkBox1);
        layout.addWidget(checkBox2);

        //QDialogButtonBox box = new QDialogButtonBox(Qt.Orientation.Horizontal);
        //box.addButton(applyButton, QDialogButtonBox.ButtonRole.AcceptRole);
        //box.addButton(abortButton, QDialogButtonBox.ButtonRole.RejectRole);

        QToolBar buttonsBar = new QToolBar();
        buttonsBar.addWidget(applyButton);
        buttonsBar.addSeparator();
        buttonsBar.addWidget(abortButton);

        layout.addWidget(buttonsBar);
        this.setLayout(layout);

    }

    void applyClicked() {
        if (check1.getValue()) {
            System.out.println("User pick fist");
        }
        if (check2.getValue()) {
            System.out.println("User pick second");
        }
        if (!(check1.getValue() | check2.getValue())) {
            System.out.println("User DIDN'T");
        }
        this.close();
    }

    void abortClicked() {
        this.close();
    }

    void enableControl() {
        root.setEnabled(true);
    }

}
