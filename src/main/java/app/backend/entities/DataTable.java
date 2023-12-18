package app.backend.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
    private List<String> columnNames;
    private List<List<String>> rows;
    private String message;
    private ResultSet resultSet;

    public DataTable(List<String> columnNames, List<List<String>> rows, String message, ResultSet resultSet) {
        this.columnNames = columnNames;
        this.rows = rows;
        this.message = message;
        this.resultSet = resultSet;
    }

    public void changeRow(int rowNumber, List<Integer> columnNumbers, List<String> values) {
        int i = 0;
        for (int col : columnNumbers) {
            rows.get(rowNumber).set(col, values.get(i++));
        }
    }

    public void deleteRow(int index) {
        rows.remove(index);
    }

    public void getMoreRows(int rowsToGet) {
        try {
            int rowsGot = 0;
            int columnsNumber = columnNames.size();

            long startTime = System.currentTimeMillis();

            while (rowsGot < rowsToGet && resultSet.next()) {
                rowsGot++;
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    row.add(resultSet.getString(i));
                }
                rows.add(row);
            }

            long executionTime = System.currentTimeMillis() - startTime;
            message = "Rows: " + rowsGot + ", Time: " + executionTime + " millis";
        } catch (SQLException e) {
            throw new RuntimeException("Can't get more rows");
        }
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public String getMessage() {
        return message;
    }
}
