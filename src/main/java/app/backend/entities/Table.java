package app.backend.entities;

import java.util.List;

public class Table {
    private String name;
    private String definition;
    private List<Column> columnList;
    private List<Index> indexList;
    private List<Key> keyList;
    private List<ForeignKey> foreignKeyList;
    private DataTable dataTable;

    public Table(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public List<String> getColumnList() {
        return columnList.stream().map(Column::getName).toList();
    }

    public Column getColumn(String columnName) {
        return columnList.stream().filter(x -> x.getName().equals(columnName)).findFirst().orElse(null);
    }

    public List<Column> getColumns() {
        return columnList;
    }

    public void setIndexList(List<Index> indexList) {
        this.indexList = indexList;
    }

    public List<String> getIndexList() {
        return indexList.stream().map(Index::getName).toList();
    }

    public Index getIndex(String indexName) {
        return indexList.stream().filter(x -> x.getName().equals(indexName)).findFirst().orElse(null);
    }

    public List<Index> getIndexes() {
        return indexList;
    }

    public void setKeyList(List<Key> keyList) {
        this.keyList = keyList;
    }

    public List<String> getKeyList() {
        return keyList.stream().map(Key::getName).toList();
    }

    public Key getKey(String keyName) {
        return keyList.stream().filter(x -> x.getName().equals(keyName)).findFirst().orElse(null);
    }

    public void setForeignKeyList(List<ForeignKey> foreignKeyList) {
        this.foreignKeyList = foreignKeyList;
    }

    public List<String> getForeignKeyList() {
        return foreignKeyList.stream().map(ForeignKey::getName).toList();
    }

    public ForeignKey getForeignKey(String foreignKeyName) {
        return foreignKeyList.stream().filter(x -> x.getName().equals(foreignKeyName)).findFirst().orElse(null);
    }
}