package app.widgets;

import io.qt.widgets.QToolBar;
import io.qt.widgets.QWidget;

public class MyToolBar extends QToolBar {

    public void addWidgetAndSeparator(QWidget item) {
        this.addWidget(item);
        this.addSeparator();
    }

}
