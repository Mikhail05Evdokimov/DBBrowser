package app.backend.entities;

import java.util.ArrayList;
import java.util.List;

public class ForeignKey {
    private String name;
    private List<String> childColumns;
    private String parentTable;
    private List<String> parentColumns;

    public ForeignKey(String name, String childColumn, String parentTable, String parentColumn) {
        this.name = name;
        this.childColumns = new ArrayList<>(List.of(childColumn));
        this.parentTable = parentTable;
        this.parentColumns = new ArrayList<>(List.of(parentColumn));
    }

    public void addChildColumn(String columnName) {
        childColumns.add(columnName);
    }

    public void addParentColumn(String columnName) {
        parentColumns.add(columnName);
    }

    public String getName() {
        return name;
    }

    public List<String> getChildColumns() {
        return childColumns;
    }

    public String getParentTable() {
        return parentTable;
    }

    public List<String> getParentColumns() {
        return parentColumns;
    }
}
