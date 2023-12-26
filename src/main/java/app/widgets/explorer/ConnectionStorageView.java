package app.widgets.explorer;

import app.MainWindow;
import app.backend.controllers.ConnectionController;
import io.qt.core.QPoint;
import io.qt.core.Qt;
import io.qt.gui.QAction;
import io.qt.gui.QCursor;
import io.qt.widgets.QMenu;
import io.qt.widgets.QPushButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConnectionStorageView extends QPushButton {

    private final Map<String, ActionForConnectionStorage> connectionList;
    private final MainWindow root;
    private final QMenu popMenu;
    private QPoint point;

    public ConnectionStorageView(MainWindow root) {
        this.root = root;
        connectionList = new HashMap<>();
        this.setText("Current connection");
        popMenu = new QMenu("Current connection");
        popMenu.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        popMenu.customContextMenuRequested.connect(this, "contextMenu(QPoint)");
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

    public void deleteConnection() {
        var conName = Objects.requireNonNull(popMenu.actionAt(point)).text();
        popMenu.removeAction(connectionList.get(conName));
        connectionList.remove(conName);
        this.setText("");
        ConnectionController.closeConnection(conName);
    }

    public String getCurrentConnection() {
        return this.text();
    }

    void contextMenu(QPoint cPoint) {
        point = cPoint;
        QMenu contextMenu = new QMenu();
        QAction delete = new QAction("Delete connection");
        delete.triggered.connect(this, "deleteConnection()");
        contextMenu.addAction(delete);
        contextMenu.popup(QCursor.pos());
    }



}
