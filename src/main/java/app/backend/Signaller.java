package app.backend;

import app.MenuController;
import app.backend.entities.DataTable;
import app.backend.entities.Table;
import io.qt.core.QObject;

public class Signaller extends QObject {

    private final Signal0 signalToShowTree = new Signal0();
    private final Signal0 signalToHideTree = new Signal0();
    private final Signal0 signalToShowDBInfo = new Signal0();
    private final Signal1<String> signalNewConnectionName = new Signal1<>();
    private final Signal1<DataTable> signalGetTableData = new Signal1<>();

    public Signaller(MenuController controller) {
        connect(this, "signalToShowTree", controller, "showSchema()");
        connect(this, "signalToHideTree", controller, "showFiles()");
        connect(this, "signalToShowDBInfo", controller, "showDBInfo()");
        connect(this, "signalNewConnectionName(String)", controller, "newConnectionName(String)");
        connect(this, "signalGetTableData(DataTable)", controller, "setTableDataView(DataTable)");
    }

    public void emitSignalToTree(TreeSignals type) {
        switch (type) {
            case SHOW -> signalToShowTree.emit();
            case HIDE -> signalToHideTree.emit();
        }
    }

    public void emitSignalToAddConnectionName(String name) {
        signalNewConnectionName.emit(name);
    }

    public void emitSignalToDBInfo() {
        signalToShowDBInfo.emit();
    }

    public void emitSignalToGetTableData(DataTable table) {
        signalGetTableData.emit(table);
    }

}

