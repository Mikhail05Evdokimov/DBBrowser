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
    private final QMenu popMenu;
    private final Signal1<String> signal1 = new Signal1<>();

    public ConnectionStorageView(MainWindow root) {
        this.root = root;
        connectionList = new ArrayList<>();
        this.setText("Current connection");
        popMenu = new QMenu("Current connection");
        this.setMenu(popMenu);
    }

    public void addConnection(String conName) {
        MyActionForConnectionStorage action = new MyActionForConnectionStorage(conName, root.menuController, this);
        connectionList.add(conName);
        popMenu.addAction(action);
        this.setText(conName);
    }







}
