package app.backend.table;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private final List<String> rowData;

    public Row(List<String> stringList) {
        rowData = new ArrayList<>();
        rowData.addAll(stringList);
    }

    public List<String> getRowData() {
        return rowData;
    }

}
