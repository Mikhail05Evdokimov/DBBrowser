package app.backend.table;

import java.util.List;

public class Table {

    private final TableMetaData metaData;
    private final TableData data;

    public Table(List<String> columnNames, List<Row> rowList) {
        metaData = new TableMetaData(columnNames);
        data = new TableData(rowList);
    }

    public TableData getData() {
        return data;
    }

    public TableMetaData getMetaData() {
        return metaData;
    }
}
