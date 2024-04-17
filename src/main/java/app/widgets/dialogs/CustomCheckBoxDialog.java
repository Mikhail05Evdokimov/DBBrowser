package app.widgets.dialogs;

import io.qt.widgets.*;

import static io.qt.core.Qt.WindowType.WindowStaysOnTopHint;

public class CustomCheckBoxDialog extends QDialog {

    CheckBoxChecker check2;
    QTextEdit inputText;
    QCheckBox checkBox1;
    QWidget root;

    public CustomCheckBoxDialog(QWidget root) {
        this.root = root;
        this.finished.connect(this, "enableControl()");
        this.setWindowFlag(WindowStaysOnTopHint);
        QLabel label = new QLabel("Cringe check box");

        this.setWindowTitle("Title");
        checkBox1 = new QCheckBox();
        checkBox1.setText("Point1");
        QCheckBox checkBox2 = new QCheckBox();
        checkBox2.setText("Point2");

        inputText = new QTextEdit();

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
        layout.addWidget(inputText);

        QToolBar buttonsBar = new QToolBar();
        buttonsBar.addWidget(applyButton);
        buttonsBar.addSeparator();
        buttonsBar.addWidget(abortButton);

        layout.addWidget(buttonsBar);
        this.setLayout(layout);
        this.open();
    }

    void applyClicked() {

        if (checkBox1.getChecked()) {
            System.out.println("User pick first");
        }
        if (check2.getValue()) {
            System.out.println("User pick second");
        }
        if (!(checkBox1.getChecked() | check2.getValue())) {
            System.out.println("User DIDN'T");
        }
        System.out.println(inputText.toPlainText());
        this.close();
    }

    void abortClicked() {
        this.close();
    }

    void enableControl() {
        root.setEnabled(true);
    }

}
