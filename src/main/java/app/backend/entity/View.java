package app.backend.entity;

import java.util.List;

public class View {
    private String name;
    private String definition;
    private List<Row> data;

    public View(String name, String definition, List<Row> data) {
        this.name = name;
        this.definition = definition;
        this.data = data;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<Row> getData() {
        return data;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
