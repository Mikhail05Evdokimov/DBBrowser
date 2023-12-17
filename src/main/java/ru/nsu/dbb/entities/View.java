package ru.nsu.dbb.entities;

public class View {
    private String name;
    private String definition;

    public View(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }
}
