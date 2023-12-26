package app.backend.entities;

import java.util.ArrayList;
import java.util.List;

public class Key {
    private String name;
    private List<String> columns;

    public Key(String name, String column) {
        this.name = name;
        this.columns = new ArrayList<>(List.of(column));
    }

    public void addColumn(String column) {
        columns.add(column);
    }

    public String getName() {
        return name;
    }

    public List<String> getColumns() {
        return columns;
    }
}
