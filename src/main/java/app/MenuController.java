package app;

import app.backend.LocalStorage;
import app.backend.table.Table;
import app.widgets.dialogs.CheckBoxChecker;
import app.widgets.dialogs.CustomCheckBoxDialog;
import app.widgets.dialogs.FileDialog;
import io.qt.gui.QCursor;
import io.qt.widgets.QCheckBox;
import io.qt.widgets.QMessageBox;

import java.sql.SQLException;

public class MenuController {

    private final MainWindow root;

    public MenuController(MainWindow mainWindow) {
        root = mainWindow;
    }

    void run_clicked() throws SQLException {
        if (root.edit.toPlainText().isEmpty()) {
            root.label.setText(" Empty text box. Write something.");
        }
        else {
            if (LocalStorage.getFilePath() == null) {
                root.label.setText("No connection to database");
            }
            else {
                LocalStorage.setQuery(root.edit.toPlainText());
                Table resultTable = LocalStorage.execQuery();
                if (resultTable == null) {
                    root.label.setText("Wrong SQL query");
                }
                else {
                    root.tableView.setTable(resultTable);
                    root.label.setText(" Your query done");
                }
            }
        }
        //root.edit.clear();
    }

    void act1() {
        root.label.setText(" DON'T TOUCH ME!!!");
    }

    void callTextBox() {
        QMessageBox.StandardButton reply =
            QMessageBox.question(root, "Tile", "Cringe text box",
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No));
        if (reply == QMessageBox.StandardButton.Yes) {
            System.out.println("User said Yes");
        }
        else {
            System.out.println("User said NO");
        }
    }

    void callCustomCheckBox() {
        CustomCheckBoxDialog messageBox = new CustomCheckBoxDialog(root);
        root.setEnabled(false);
        messageBox.open();
    }

    //Вот эта штука на фоне моей кастомной вообще юзлес, наверное уберём её.
    void callDefaultCheckBox() {
        QMessageBox messageBox = new QMessageBox();
        messageBox.setText("Cringe CheckBox");
        messageBox.setWindowTitle("Title");
        QCheckBox checkBox1 = new QCheckBox();
        checkBox1.setText("Point1");
        messageBox.setCheckBox(checkBox1);
        messageBox.setStandardButtons(QMessageBox.StandardButton.Apply, QMessageBox.StandardButton.Abort);
        CheckBoxChecker check1 = new CheckBoxChecker();
        checkBox1.stateChanged.connect(check1, "inverse()");

        int reply = messageBox.exec();

        if (QMessageBox.StandardButton.resolve(reply) == QMessageBox.StandardButton.Apply) {
            if (check1.getValue()) {
                System.out.println("User pick it");
            } else {
                System.out.println("User DIDN'T");
            }
        }
    }

    void clear_button_clicked() {
        root.label.clear();
        root.edit.clear();
    }

    void rightClick() {
        root.popMenu.popup(QCursor.pos());
    }

    void selectFileButtonClicked() {
        new FileDialog(root);
    }

    void showSchema() throws SQLException {
        root.treeViewMenu.setTreeModel(LocalStorage.showSchema());
    }

    void closeConnectionButtonClicked() {
        LocalStorage.closeConnection();
    }

}
