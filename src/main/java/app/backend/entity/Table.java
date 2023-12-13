package app.backend.entity;

import io.qt.core.QString;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private List<Column> columns;
    private List<Key> keys;
    private List<ForeignKey> foreignKeys;
    private List<Index> indexes;
    private List<Check> checks;
    private List<Row> data;
    private String DDL;

    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.checks = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public List<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<ForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public void setChecks(List<Check> checks) {
        this.checks = checks;
    }

    public List<Row> getData() {
        return data;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }

    public String getDDL() {
        return DDL;
    }

    public void setDDL(String DDL) {
        this.DDL = DDL;
    }
}
