package app.widgets;

import app.MenuController;
import app.backend.entities.DataTable;
import io.qt.core.QModelIndex;
import io.qt.core.Qt;
import io.qt.gui.QAction;
import io.qt.gui.QCursor;
import io.qt.gui.QStandardItem;
import io.qt.gui.QStandardItemModel;
import io.qt.widgets.QMenu;
import io.qt.widgets.QTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableView extends QTableView {
    private int rowsToLoad = 100;
    private DataTable dataTable;
    private String tableName;
    private final QStandardItemModel emptyModel;
    private final Signal4<String, Integer, List<Integer>, List<String>> changeDataSignal = new Signal4<>();
    private final Signal2<String, Integer> deleteRowSignal = new Signal2<>();

    public TableView(MenuController menuController, boolean isResult) {

        if (!(isResult)) {
            changeDataSignal.connect(menuController, "changeData(String, Integer, List, List)");
            deleteRowSignal.connect(menuController, "deleteRow(String, Integer)");
            this.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
            this.customContextMenuRequested.connect(this, "contextMenuRequested()");
        }
        emptyModel = new QStandardItemModel();
        this.setShowGrid(true);
    }

    public void setTable(DataTable table) {

        dataTable = table;
        QStandardItemModel data = new QStandardItemModel();
        data.setHorizontalHeaderLabels(table.getColumnNames());

        for (List<String> row : table.getRows()) {
            List<QStandardItem> itemList = new ArrayList<>();
            for (String cell : row) {
                itemList.add(new QStandardItem(cell));
            }
            data.appendRow(itemList);
        }
        this.setModel(data);
    }

    public void setTableView(DataTable table, String tableName) {
        dataTable = table;
        this.tableName = tableName;
        QStandardItemModel data = new QStandardItemModel();
        data.dataChanged.connect(this, "dataChanged(QModelIndex)");
        data.setHorizontalHeaderLabels(table.getColumnNames());

        for (List<String> row : table.getRows()) {
            List<QStandardItem> itemList = new ArrayList<>();
            for (String cell : row) {
                itemList.add(new QStandardItem(cell));
            }
            data.appendRow(itemList);
        }
        this.setModel(data);
    }

    public void moreRows() {
        dataTable.getMoreRows(rowsToLoad);
        myUpdate();
    }

    void dataChanged(QModelIndex index) {

        List<Integer> list = new ArrayList<>();
        list.add(index.column());
        List<String> list1 = new ArrayList<>();
        list1.add(Objects.requireNonNull(this.model()).data(index).toString());
        //this.dataTable.changeRow(index.row(), list, list1);
        changeDataSignal.emit(tableName, index.row(), list, list1);

    }

    public void clear() {
        dataTable = null;
        this.setModel(emptyModel);
        tableName = null;
    }

    void contextMenuRequested() {
        QMenu contextMenu = new QMenu();
        QAction deleteRow = new QAction("Delete row");
        deleteRow.triggered.connect(this, "deleteRow()");
        contextMenu.addAction(deleteRow);
        contextMenu.popup(QCursor.pos());
    }

    void deleteRow() {
        var index = this.currentIndex();
        deleteRowSignal.emit(this.tableName, index.row());
        dataTable.deleteRow(index.row());
        myUpdate();
    }

    public void myUpdate() {
        setTableView(dataTable, tableName);
    }

    public void addRow(List<String> row) {
        dataTable.addRow(row);
        myUpdate();
    }

    public String getCurrentTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return dataTable.getColumnNames();
    }

    public String getLastId() {
        while (dataTable.getMoreRows(100)) {}
        return dataTable.getRows().get(dataTable.getRows().size() - 1).get(0);
    }

    public void changeRowsToLoadNumber(int number) {
        rowsToLoad = number;
    }

    public String getMessage() {
        return dataTable.getMessage();
    }

}
