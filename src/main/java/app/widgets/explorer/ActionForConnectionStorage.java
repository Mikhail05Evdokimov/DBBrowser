package app.widgets.explorer;

import app.MenuController;
import app.backend.controllers.StorageController;
import io.qt.gui.QAction;

public class ActionForConnectionStorage extends QAction {

    private final Signal1<String> clickedSignal;
    private final Signal0 clickedOnDisconnectedSignsl;
    private final ConnectionStorageView root;

    public ActionForConnectionStorage(String name, MenuController controller, ConnectionStorageView root) {
        this.setText(name);
        this.root = root;
        clickedSignal = new Signal1<>();
        clickedOnDisconnectedSignsl = new Signal0();
        clickedSignal.connect(controller, "newCurrentConnectionName(String)");
        clickedOnDisconnectedSignsl.connect(controller, "clearWorkArea()");
        this.triggered.connect(this, "triggered()");
    }

    void triggered() {
        String conName = this.getText();
        root.setText(conName);
        if (StorageController.connectionStorage.getConnection(conName).isConnected()) {
            clickedSignal.emit(conName);
        }
        else {
            clickedOnDisconnectedSignsl.emit();
        }
    }

}
