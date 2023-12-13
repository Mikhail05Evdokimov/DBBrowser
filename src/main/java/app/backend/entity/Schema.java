package app.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Schema {
    private String name;
    private final List<Table> tables;
    private final List<Index> allIndexes;
    private final List<View> views;

    public Schema(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.allIndexes = new ArrayList<>();
        this.views = new ArrayList<>();
    }

    public Table getTable(String name){
        return tables.stream()
                .filter(element -> name.equals(element.getName()))
                .findFirst().orElse(null);
    }

    public Index getIndex(String name){
        return allIndexes.stream()
                .filter(element -> name.equals(element.getName()))
                .findFirst().orElse(null);
    }

    public View getView(String name){
        return views.stream()
                .filter(element -> name.equals(element.getName()))
                .findFirst().orElse(null);
    }

    public void setTables(List<Table> newTables) {
        tables.addAll(newTables);
    }

    public void setIndexes(List<Index> newIndexes) {
        allIndexes.addAll(newIndexes);
    }

    public void setViews(List<View> newViews) {
        views.addAll(newViews);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
