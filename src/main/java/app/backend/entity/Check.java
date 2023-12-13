package app.backend.entity;

public class Check {
    private String name;
    private String predicate;

    public Check(String name, String predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }
}
