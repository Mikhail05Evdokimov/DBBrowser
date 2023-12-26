package app.backend.entities;

public class View {
    private String name;
    private String definition;
    private DataTable dataTable;
    private int statusDDL;

    public View(String name, String definition) {
        this.name = name;
        this.definition = definition;
        this.statusDDL = 0;
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

    public int getStatusDDL() {
        return statusDDL;
    }

    public void setStatusDDL(int statusDDL) {
        this.statusDDL = statusDDL;
    }
}