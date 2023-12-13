package ru.nsu.dbb.entities;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private String name;
    private List<Schema> schemaList;

    public Database(String name) {
        this.name = name;
        this.schemaList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
