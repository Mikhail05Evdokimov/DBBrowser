package app;

import app.backend.LocalStorage;
import app.backend.controllers.ConnectionController;
import app.backend.entities.ConnectionInfo;
import app.backend.entities.DataTable;
import app.backend.table.Table;
import app.widgets.dialogs.CheckBoxChecker;
import app.widgets.dialogs.CustomCheckBoxDialog;
import app.widgets.dialogs.FileDialog;
import io.qt.core.QObject;
import io.qt.gui.QCursor;
import io.qt.widgets.QCheckBox;
import io.qt.widgets.QMessageBox;

import java.io.IOException;
import java.sql.SQLException;

public class MenuController extends QObject {

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
                //app.backend.entities.Table rusultTable = ConnectionController.
                if (resultTable == null) {
                    root.label.setText(" Wrong SQL query");
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

    void connectToDBButtonClicked() {
        /*LocalStorage.closeConnection();
        new StartDialog(root.windowIcon());
        root.close();*/
        new FileDialog(this);
        //new FileDialog(root);
    }

    void fileChosen(String fileName) {
        ConnectionController.addConnection(ConnectionInfo.ConnectionType.SQLITE, fileName);
    }

    void showSchema(String conName) throws IOException {
        //root.treeViewMenu.setTreeModel(LocalStorage.showSchema());
        root.treeViewMenu.setTreeModel(ConnectionController.getSchema(conName), conName);
        System.out.println(ConnectionController.getSchema(conName));
        //System.out.println(root.dbName.toPlainText());
    }

    void showFiles() {
        root.treeViewMenu.setEmptyModel();
    }

    void closeConnectionButtonClicked() {
        ConnectionController.closeConnection(root.dbName.toPlainText());
        //LocalStorage.closeConnection();
    }

    void reconnectToDBClicked() throws SQLException, InterruptedException {
        ConnectionController.reconnectConnection(root.dbName.toPlainText());
        //LocalStorage.reconnectToDB();
    }

    void showDBInfo() {
        //List<String> list = LocalStorage.getDBName();
        String list = ConnectionController.getDBInfo(root.dbName.toPlainText());
        root.dbInfo.setText(list);
    }

    void newConnectionName(String name) {
        root.dbName.setText(name);
        root.connectionStorageView.addConnection(name);
    }

    void setTableDataView(DataTable table) {
        root.tableViewMainTab.setTableView(table);
    }

   void newCurrentConnectionName(String conName) throws IOException {
        root.dbName.setText(conName);
        root.treeViewMenu.newCurrentConnectionName(conName);
        showSchema(conName);
   }

   void deleteConnection(String conName) {
        root.connectionStorageView.deleteConnection(conName);
   }

}
