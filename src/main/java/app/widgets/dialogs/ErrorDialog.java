package app.widgets.dialogs;

import io.qt.widgets.*;

public class ErrorDialog extends QDialog {

    public ErrorDialog(String text) {
        this.setWindowTitle("Error");
        QLayout layout = new QGridLayout();
        QLabel label = new QLabel(text);
        layout.addWidget(label);
        layout.addWidget(new QLabel(""));
        QPushButton ok = new QPushButton("OK");
        ok.clicked.connect(this, "ok()");
        layout.addWidget(ok);
        this.setLayout(layout);
        this.open();
    }

    void ok() {
        this.close();
    }

}
