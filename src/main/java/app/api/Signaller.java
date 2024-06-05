package app.api;

import app.widgets.dialogs.start.OnlineStartDialog;
import io.qt.core.QObject;

public class Signaller extends QObject {

    private final Signal1<String> signal1 = new Signal1<>();


    public void emitSignal(OnlineStartDialog controller, String res) {
        signal1.connect(controller, "result(String)");
        signal1.emit(res);
    }

}


