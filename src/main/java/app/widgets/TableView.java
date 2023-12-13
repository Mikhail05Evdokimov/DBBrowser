package app.widgets;

import app.backend.table.Row;
import app.backend.table.Table;
import io.qt.core.Qt;
import io.qt.gui.QStandardItem;
import io.qt.gui.QStandardItemModel;
import io.qt.widgets.QHeaderView;
import io.qt.widgets.QTableView;

import java.util.ArrayList;
import java.util.List;

public class TableView extends QTableView {

    public TableView() {
        this.setShowGrid(true);
    }

    public void setTable(Table table) {

        QStandardItemModel data = new QStandardItemModel();
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

}
