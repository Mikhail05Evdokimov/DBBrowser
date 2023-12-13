package app.backend.entity;

public class Tab {
    private String name;
    private Object linkToMainObject;

    public Tab(String name, Object linkToMainObject) {
        this.name = name;
        this.linkToMainObject = linkToMainObject;
    }

    public String getName() {
        return name;
    }

    public Object getLinkToMainObject() {
        return linkToMainObject;
    }
}
