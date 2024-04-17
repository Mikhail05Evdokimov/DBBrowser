package app.widgets.dialogs;

import app.MenuController;
import io.qt.widgets.*;
import org.apache.commons.lang3.StringUtils;

public class ChangeLoadRowsDialog extends QDialog {

    private final MenuController menuController;
    private final QTextEdit input;

    public ChangeLoadRowsDialog(MenuController controller) {
        menuController = controller;
        this.setWindowTitle("Change Load-Rows number");

        QLayout layout = new QGridLayout(this);
        layout.addWidget(new QLabel("How many rows do you want to load\nwhen the button is clicked?"));
        layout.addWidget(new QLabel(""));

        input = new QTextEdit();
        input.setFixedHeight(28);
        input.setText("100");
        layout.addWidget(input);

        layout.addWidget(new QLabel(""));

        QPushButton cancelButton = new QPushButton("Cancel");
        QPushButton addButton = new QPushButton("Apply");

        addButton.clicked.connect(this, "applyClicked()");
        cancelButton.clicked.connect(this, "abortClicked()");

        layout.addWidget(addButton);
        layout.addWidget(cancelButton);

        this.setLayout(layout);
        this.open();
    }

    void applyClicked() {

        if (input.toPlainText().isEmpty()) {
            input.setText("Write number here");
        }
        else {
            if (!(StringUtils.isNumeric(input.toPlainText()))) {
                input.setText("Input should be numeric");
            }
            else {
                menuController.changeLoadRowsNumber(Integer.parseInt(input.toPlainText()));
                this.close();
            }
        }
    }

    void abortClicked() {
        this.close();
    }

}
