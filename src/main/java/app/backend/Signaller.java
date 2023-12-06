package app.backend;

import app.MenuController;
import io.qt.core.QObject;

public class Signaller extends QObject {

    private final Signal0 signalToTree = new Signal0();

    public Signaller(MenuController controller) {
        connect(this, "signalToTree", controller, "showSchema()");
    }

    public void emitSignalToTree() {
        signalToTree.emit();
    }

}
