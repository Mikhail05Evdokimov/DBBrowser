package app.backend;

import app.MenuController;
import io.qt.core.QObject;

public class Signaller extends QObject {

    private final Signal0 signalToShowTree = new Signal0();
    private final Signal0 signalToHideTree = new Signal0();
    private final Signal0 signalToShowDBInfo = new Signal0();

    public Signaller(MenuController controller) {
        connect(this, "signalToShowTree", controller, "showSchema()");
        connect(this, "signalToHideTree", controller, "showFiles()");
        connect(this, "signalToShowDBInfo", controller, "showDBInfo()");
    }

    public void emitSignalToTree(TreeSignals type) {
        switch (type) {
            case SHOW -> signalToShowTree.emit();
            case HIDE -> signalToHideTree.emit();
        }
    }

    public void emitSignalToDBInfo() {
        signalToShowDBInfo.emit();
    }

}

enum TreeSignals{
    SHOW, HIDE;
    }
