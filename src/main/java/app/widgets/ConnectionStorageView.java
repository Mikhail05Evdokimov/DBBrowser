package app.widgets;

import app.MainWindow;
import com.sun.tools.javac.Main;
import io.qt.gui.QAction;
import io.qt.widgets.QMainWindow;
import io.qt.widgets.QMenu;
import io.qt.widgets.QPushButton;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStorageView extends QPushButton {

    private List<String> connectionList;
    private final MainWindow root;
    private QMenu popMenu;
    private final Signal1<String> signal1 = new Signal1<>();

    public ConnectionStorageView(MainWindow root) {
        this.root = root;
        connectionList = new ArrayList<>();
        this.setText("Current connection");
        popMenu = new QMenu("Current connection");
        this.setMenu(popMenu);
    }

    public void addConnection(String conName) {
        QAction action = new QAction(conName);
        action.triggered.connect(this, "newAction()");
        connectionList.add(conName);
        popMenu.addAction(action);
        connect(this, "signal1(String)", root.menuController, "newCurrentConnectionName(String)");
        this.setText(conName);
    }

    void newAction() {
        signal1.emit(connectionList.get(connectionList.size() - 1));
    }





}
