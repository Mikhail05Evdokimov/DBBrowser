package app.widgets.explorer;

import app.MainWindow;
import io.qt.widgets.QMenu;
import io.qt.widgets.QPushButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionStorageView extends QPushButton {

    private Map<String, ActionForConnectionStorage> connectionList;
    private final MainWindow root;
    private final QMenu popMenu;
    private final Signal1<String> signal1 = new Signal1<>();

    public ConnectionStorageView(MainWindow root) {
        this.root = root;
        connectionList = new HashMap<>();
        this.setText("Current connection");
        popMenu = new QMenu("Current connection");
        this.setMenu(popMenu);
    }

    public void addConnection(String conName) {
        ActionForConnectionStorage action = new ActionForConnectionStorage(conName, root.menuController, this);
        connectionList.put(conName, action);
        popMenu.addAction(connectionList.get(conName));
        this.setText(conName);
    }

    public void deleteConnection(String conName) {
        popMenu.removeAction(connectionList.get(conName));
        connectionList.remove(conName);
        this.setText(conName);
    }





}
