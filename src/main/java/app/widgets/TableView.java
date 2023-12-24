package app.widgets;

import app.MenuController;
import app.backend.entities.DataTable;
import app.backend.table.Row;
import app.backend.table.Table;
import io.qt.core.QModelIndex;
import io.qt.gui.QStandardItem;
import io.qt.gui.QStandardItemModel;
import io.qt.widgets.QTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableView extends QTableView {
    private DataTable dataTable;
    private String tableName;
    private final MenuController controller;
    private final QStandardItemModel emptyModel;
    private final Signal4<String, Integer, List<Integer>, List<String>> changeDataSignal = new Signal4<>();

    public TableView(MenuController menuController) {
        controller = menuController;
        changeDataSignal.connect(controller, "changeData(String, Integer, List, List)");
        emptyModel = new QStandardItemModel();
        this.setShowGrid(true);
    }

    public void setTable(Table table) {

        QStandardItemModel data = new QStandardItemModel();
        data.dataChanged.connect(this, "dataChanged(QModelIndex)");
        data.setHorizontalHeaderLabels(table.getMetaData().getColumnNames());

        for (Row row : table.getData().getRowList()) {
            List<QStandardItem> itemList = new ArrayList<>();
            for (String cell : row.getRowData()) {
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
        dataTable.getMoreRows(100);
        setTableView(dataTable, tableName);
    }

    void dataChanged(QModelIndex index) {
        //System.out.println(Objects.requireNonNull(this.model()).data(index));
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
    }



}
