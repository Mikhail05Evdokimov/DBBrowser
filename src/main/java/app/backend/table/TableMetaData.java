package app.backend.table;

import java.util.ArrayList;
import java.util.List;

public class TableMetaData {

    private final List<String> columnNames;

    public TableMetaData(List<String> inputColumnNames) {
        columnNames = new ArrayList<>();
        columnNames.addAll(inputColumnNames);
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

}
