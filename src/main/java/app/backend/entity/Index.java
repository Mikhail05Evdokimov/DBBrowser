package app.backend.entity;

import java.util.LinkedList;

public class Index {
    private String name;
    private LinkedList<Column> columns;
    private Boolean unique;

    public Index(String name, LinkedList<Column> columns, Boolean unique) {
        this.name = name;
        this.columns = columns;
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Column> getColumns() {
        return columns;
    }

    public void setColumns(LinkedList<Column> columns) {
        this.columns = columns;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
