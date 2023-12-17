package ru.nsu.dbb.entities;

public class View {
    private String name;
    private String definition;
    private DataTable dataTable;

    public View(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }
}
