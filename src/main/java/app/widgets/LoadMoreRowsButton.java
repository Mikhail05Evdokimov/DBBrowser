package app.widgets;

import app.MenuController;
import io.qt.core.Qt;
import io.qt.gui.QAction;
import io.qt.gui.QCursor;
import io.qt.widgets.QMenu;
import io.qt.widgets.QPushButton;

public class LoadMoreRowsButton extends QPushButton {

    private final MenuController controller;

    public LoadMoreRowsButton(MenuController controller) {
        this.controller = controller;
        clicked.connect(controller, "moreRows()");
        setText("Load more rows");
        setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        customContextMenuRequested.connect(this, "popUp()");
    }

    void popUp() {
        QMenu contextMenu = new QMenu();
        QAction changeLoadRowsNumber = new QAction("Change Load-Rows number");
        changeLoadRowsNumber.triggered.connect(controller, "changeLoadRowsNumberClicked()");
        contextMenu.addAction(changeLoadRowsNumber);
        contextMenu.popup(QCursor.pos());
    }

}
