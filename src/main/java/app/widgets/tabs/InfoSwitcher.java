package app.widgets.tabs;

import app.MainWindow;
import io.qt.gui.QAction;
import io.qt.widgets.QMenu;
import io.qt.widgets.QPushButton;

import java.util.ArrayList;
import java.util.List;

public class InfoSwitcher extends QPushButton {

    public InfoSwitcher(InfoTab root) {
        List<QAction> infoList = new ArrayList<>();
        this.setText("DDL");
        QMenu popMenu = new QMenu("Info");
        this.setMenu(popMenu);

        QAction ddl = new QAction("DDL");
        ddl.triggered.connect(root, "ddl()");

        QAction keys = new QAction("Keys");
        keys.triggered.connect(root, "keys()");

        QAction foreignKeys = new QAction("Foreign keys");
        foreignKeys.triggered.connect(root, "foreignKeys()");

        popMenu.addAction(ddl);
        popMenu.addAction(keys);
        popMenu.addAction(foreignKeys);
    }

}
