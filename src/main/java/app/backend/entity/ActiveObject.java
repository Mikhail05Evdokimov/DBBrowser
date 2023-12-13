package app.backend.entity;

public class ActiveObject {
    private Object activeObject;

    public ActiveObject(Object activeObject) {
        this.activeObject = activeObject;
    }

    public void setActiveObject(Object activeObject) {
        this.activeObject = activeObject;
    }

    public Object getActiveObject() {
        return activeObject;
    }


}
