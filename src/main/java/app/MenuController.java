package app;

import app.backend.LocalStorage;
import app.backend.controllers.ConnectionController;
import app.backend.entities.ConnectionInfo;
import app.backend.entities.DataTable;
import app.backend.table.Table;
import app.widgets.dialogs.AddRowDialog;
import app.widgets.dialogs.CheckBoxChecker;
import app.widgets.dialogs.CustomCheckBoxDialog;
import app.widgets.dialogs.FileDialog;
import io.qt.core.QObject;
import io.qt.gui.QCursor;
import io.qt.widgets.QCheckBox;
import io.qt.widgets.QMessageBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
        new CustomCheckBoxDialog(root);
        root.setEnabled(false);
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

    void clearWorkArea() {
        root.treeViewMenu.setEmptyModel();
        root.dbName.clear();
        root.tableViewMainTab.clear();
    }

    void closeConnectionButtonClicked() {
        ConnectionController.closeConnection(root.dbName.toPlainText());
        root.dbName.clear();
        root.dbInfo.clear();
        root.tableViewMainTab.clear();
    }

    void reconnectToDBClicked() throws SQLException, InterruptedException {
        ConnectionController.reconnectConnection(root.connectionStorageView.getCurrentConnection());
        //LocalStorage.reconnectToDB();
    }

    void showDBInfo() {
        //List<String> list = LocalStorage.getDBName();
        String list = ConnectionController.getDBInfo(root.dbName.toPlainText());
        root.dbInfo.setText(list);
    }

    void newConnectionName(String name) {
        root.dbName.setText(name);
        root.tableViewMainTab.clear();
        root.connectionStorageView.addConnection(name);
    }

    void setTableDataView(DataTable table, String tableName) {
        root.tableViewMainTab.setTableView(table, tableName);
        root.showTableViewButtons();
        //root.showTableViewButtons();
    }

   void newCurrentConnectionName(String conName) throws IOException {
        root.dbName.setText(conName);
        root.treeViewMenu.newCurrentConnectionName(conName);
        root.tableViewMainTab.clear();
        showSchema(conName);
   }

   void deleteConnection(String conName) {
        root.connectionStorageView.deleteConnection(conName);
   }

   void moreRows() {
        root.tableViewMainTab.moreRows();
   }

   void saveChanges() {
        ConnectionController.saveChanges(root.dbName.toPlainText());
   }

   void discardChanges() {
        ConnectionController.discardChanges(root.dbName.toPlainText(), root.tableViewMainTab.getCurrentTableName());
   }

   void changeData(String tableName, Integer row, List<Integer> columns, List<String> data) {
        ConnectionController.changeData(root.dbName.toPlainText(), tableName, row, columns, data);
   }

   void deleteRow(String tableName, Integer row) {
       ConnectionController.deleteRow(this.root.connectionStorageView.getCurrentConnection(), tableName, row);
   }

   void addRowButtonClicked() {
       new AddRowDialog(this, root);
   }

}
