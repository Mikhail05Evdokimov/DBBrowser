package app.backend.entity;

import java.util.List;

public class Key {
    private String name;
    private Boolean primary;
    private List<Column> columns;

    public Key(String name, Boolean primary, List<Column> columns) {
        this.name = name;
        this.primary = primary;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
