package app.api;

import app.MenuController;
import app.widgets.dialogs.OnlineStartDialog;
import io.qt.core.QObject;

public class Signaller extends QObject {

    private final Signal1<String> signal1 = new Signal1<>();


    public void emitSignal(OnlineStartDialog controller, String res) {
        connect(this, "signal1(String)", controller, "result(String)");
        signal1.emit(res);
    }

}


