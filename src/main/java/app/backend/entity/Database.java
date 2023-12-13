package app.backend.entity;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private String name;
    private final List<Schema> listOfSchemas;

    public Database(String name) {
        this.name = name;
        this.listOfSchemas = new ArrayList<>();
    }
    
    public Schema getSchema(String name){
        return listOfSchemas.stream()
                .filter(element -> name.equals(element.getName()))
                .findFirst().orElse(null);
    }

    public void setSchemas(List<Schema> schemas){
        listOfSchemas.addAll(schemas);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
