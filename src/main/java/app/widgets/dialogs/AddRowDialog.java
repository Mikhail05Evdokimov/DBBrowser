package app.widgets.dialogs;

import app.MainWindow;
import app.backend.controllers.ConnectionController;
import io.qt.core.Qt;
import io.qt.widgets.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class AddRowDialog extends QDialog {

    private final MainWindow mainWindow;
    private final List<QTextEdit> inputList;

    public AddRowDialog(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        inputList = new ArrayList<>();
        this.setWindowTitle("Add row");
        this.setWindowIcon(mainWindow.windowIcon());

        QLayout layout = new QGridLayout(this);
        layout.addWidget(new QLabel("Enter data for a new row"));
        layout.addWidget(new QLabel(""));
        QToolBar inputFields = new QToolBar();
        inputFields.setOrientation(Qt.Orientation.Horizontal);

        List<String> columns = mainWindow.tableViewMainTab.getColumns();
        int j = 0;
        for (String i : columns) {
            var input = new QTextEdit();
            input.setMaximumHeight(28);
            if (j == 0) {
                input.setText(String.valueOf((Integer.parseInt(mainWindow.tableViewMainTab.getLastId()) + 1)));
                j++;
            }
            var miniBar = new QToolBar();
            miniBar.setOrientation(Qt.Orientation.Vertical);
            miniBar.addWidget(new QLabel(i));
            miniBar.addWidget(input);
            inputList.add(input);
            inputFields.addWidget(miniBar);
        }

        QScrollArea scrollArea = new QScrollArea();
        scrollArea.setWidget(inputFields);

        layout.addWidget(scrollArea);
        layout.addWidget(new QLabel(""));

        QPushButton cancelButton = new QPushButton("Cancel");
        QPushButton addButton = new QPushButton("Add row");

        addButton.clicked.connect(this, "applyClicked()");
        cancelButton.clicked.connect(this, "abortClicked()");

        layout.addWidget(addButton);
        layout.addWidget(cancelButton);

        this.setLayout(layout);
        this.open();
    }

    void applyClicked() {
        var idField = inputList.get(0);
        if (idField.toPlainText().isEmpty()) {
            idField.setText("ID should be not null");
        }
        else {
            if (!(StringUtils.isNumeric(idField.toPlainText()))) {
                idField.setText("ID should be numeric");
            }
            else {
                List<String> resultList = new ArrayList<>();
                for (QTextEdit i : inputList) {
                    resultList.add(i.toPlainText());
                }
                var dt = ConnectionController.addData(mainWindow.dbName.toPlainText(), mainWindow.tableViewMainTab.getCurrentTableName(), resultList);
                mainWindow.tableViewMainTab.addRow(resultList);
                this.close();
            }
        }
    }

    void abortClicked() {
        this.close();
    }

}
