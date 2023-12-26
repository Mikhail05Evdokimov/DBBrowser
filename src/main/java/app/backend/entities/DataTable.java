package app.backend.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
    private List<String> columnNames;
    private List<List<String>> rows;
    private int rowsCount;
    private long executionTime;
    private String message;
    private ResultSet resultSet;

    public DataTable(List<String> columnNames, List<List<String>> rows, ResultSet resultSet, int rowsCount, long executionTime) {
        this.columnNames = columnNames;
        this.rows = rows;
        this.rowsCount = rowsCount;
        this.executionTime = executionTime;
        this.message = "Rows: " + rowsCount + ", Time: " + executionTime + " millis";
        this.resultSet = resultSet;
    }

    public DataTable(String message) {
        this.message = message;

    }

    public void changeRow(int rowNumber, List<Integer> columnNumbers, List<String> values) {
        int i = 0;
        for (int col : columnNumbers) {
            rows.get(rowNumber).set(col, values.get(i++));
        }
    }

    public void addRow(List<String> values) {
        rows.add(values);
    }

    public void deleteRow(int index) {
        rows.remove(index);
    }

    public boolean getMoreRows(int rowsToGet) {
        if (rows == null) {
            return false;
        }
        try {
            int rowsGot = 0;
            int columnsNumber = columnNames.size();

            long startTime = System.currentTimeMillis();
            int j = 0;
            while (rowsGot < rowsToGet && resultSet.next()) {
                rowsGot++;
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    row.add(resultSet.getString(i));
                }
                rows.add(row);
                j++;
            }

            this.rowsCount += rowsGot;
            this.executionTime = this.executionTime + System.currentTimeMillis() - startTime;
            message = "Rows: " + rowsCount + ", Time: " + executionTime + " millis";
            return j != 0;
        } catch (SQLException e) {
            return false;
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
