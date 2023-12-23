package app.widgets.explorer;

import app.MenuController;
import io.qt.gui.QAction;

public class ActionForConnectionStorage extends QAction {

    private final Signal1<String> clickedSignal;
    private final ConnectionStorageView root;

    public ActionForConnectionStorage(String name, MenuController controller, ConnectionStorageView root) {
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
