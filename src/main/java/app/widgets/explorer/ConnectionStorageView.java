package app.widgets.explorer;

import app.MainWindow;
import io.qt.widgets.QMenu;
import io.qt.widgets.QPushButton;

import java.util.HashMap;
import java.util.Map;

public class ConnectionStorageView extends QPushButton {

    private final Map<String, ActionForConnectionStorage> connectionList;
    private final MainWindow root;
    private final QMenu popMenu;

    public ConnectionStorageView(MainWindow root) {
        this.root = root;
        connectionList = new HashMap<>();
        this.setText("Current connection");
        popMenu = new QMenu("Current connection");
        this.setMenu(popMenu);
    }

    public void addConnection(String conName) {
        if (!(connectionList.containsKey(conName))) {
            ActionForConnectionStorage action = new ActionForConnectionStorage(conName, root.menuController, this);
            connectionList.put(conName, action);
            popMenu.addAction(connectionList.get(conName));
            this.setText(conName);
        }
    }

    public void deleteConnection(String conName) {
        //popMenu.removeAction(connectionList.get(conName));
        //connectionList.remove(conName);
        //this.setText("");
    }

    public String getCurrentConnection() {
        return this.text();
    }





}
