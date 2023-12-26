package app.backend.entities;

import java.util.ArrayList;
import java.util.List;

public class Schema {
    private String name;
    private List<Table> tableList;
    private List<View> viewList;
    private List<Index> indexList;

    public Schema(String name) {
        this.name = name;
        tableList = new ArrayList<>();
        viewList = new ArrayList<>();
        indexList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Table getTable(String name) {
        return tableList.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Table> getTables() {
        return tableList;
    }

    public List<String> getTableList() {
        return tableList.stream().map(Table::getName).toList();
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    // Views
    public View getView(String name) {
        return viewList.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    public List<View> getViews() {
        return viewList;
    }

    public List<String> getViewList() {
        return viewList.stream().map(View::getName).toList();
    }

    public void setViewList(List<View> viewList) {
        this.viewList = viewList;
    }

    // ндексы
    public Index getIndex(String name) {
        return indexList.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Index> getIndexes() {
        return indexList;
    }

    public List<String> getIndexList() {
        return indexList.stream().map(Index::getName).toList();
    }

    public void setIndexList(List<Index> indexList) {
        this.indexList = indexList;
    }
}