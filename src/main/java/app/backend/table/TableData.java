package app.backend.table;

import java.util.ArrayList;
import java.util.List;

public class TableData {

    private final List<Row> rowList;

    public TableData(List<Row> inputRowList) {
        rowList = new ArrayList<>();
        rowList.addAll(inputRowList);
    }

    public List<Row> getRowList() {
        return rowList;
    }

}
