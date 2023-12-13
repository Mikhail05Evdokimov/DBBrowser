package app.backend.entity;

public class ForeignKey {
    private String name;
    private Table targetTable;
    private Column targetColumn;
    private Column childColumn;

    public ForeignKey(String name, Table targetTable, Column targetColumn, Column childColumn) {
        this.name = name;
        this.targetTable = targetTable;
        this.targetColumn = targetColumn;
        this.childColumn = childColumn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(Table targetTable) {
        this.targetTable = targetTable;
    }

    public Column getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(Column targetColumn) {
        this.targetColumn = targetColumn;
    }

    public Column getChildColumn() {
        return childColumn;
    }

    public void setChildColumn(Column childColumn) {
        this.childColumn = childColumn;
    }
}
