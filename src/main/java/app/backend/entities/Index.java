package app.backend.entities;

import java.util.LinkedList;

public class Index {
    private String name;
    private boolean unique;
    private LinkedList<Column> columnLinkedList;
    private int statusDDL;

    public Index(String name, boolean unique, LinkedList<Column> columnLinkedList) {
        this.name = name;
        this.unique = unique;
        this.columnLinkedList = columnLinkedList;
        this.statusDDL = 0;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    public LinkedList<Column> getColumnLinkedList() {
        return columnLinkedList;
    }

    public int getStatusDDL() {
        return statusDDL;
    }

    public void setStatusDDL(int statusDDL) {
        this.statusDDL = statusDDL;
    }
}