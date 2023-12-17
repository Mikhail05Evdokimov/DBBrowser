package ru.nsu.dbb.entities;

import java.util.LinkedList;

public class Index {
    private String name;
    private boolean unique;
    private LinkedList<Column> columnLinkedList;

    public Index(String name, boolean unique, LinkedList<Column> columnLinkedList) {
        this.name = name;
        this.unique = unique;
        this.columnLinkedList = columnLinkedList;
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
}
