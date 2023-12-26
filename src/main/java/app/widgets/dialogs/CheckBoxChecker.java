package app.widgets.dialogs;

public class CheckBoxChecker {
    private boolean value = false;

    void inverse() {
        value = !value;
    }

    public boolean getValue() {
        return value;
    }

}
