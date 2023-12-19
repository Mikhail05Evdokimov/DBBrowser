package app.widgets;

import app.MenuController;
import io.qt.QtPrimitiveType;
import io.qt.gui.QAction;

public class MyActionForConnectionStorage extends QAction {

    private final Signal1<String> clickedSignal;
    private final ConnectionStorageView root;

    public MyActionForConnectionStorage(String name, MenuController controller, ConnectionStorageView root) {
        this.setText(name);
        this.root = root;
        clickedSignal = new Signal1<>();
        clickedSignal.connect(controller, "newCurrentConnectionName(String)");
        this.triggered.connect(this, "triggered()");
    }

    void triggered() {
        root.setText(this.getText());
        clickedSignal.emit(this.getText());
    }

}
