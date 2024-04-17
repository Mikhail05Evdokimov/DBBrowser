package app.widgets.dialogs;

import io.qt.core.Qt;
import io.qt.widgets.QLabel;
import io.qt.widgets.QToolBar;
import io.qt.widgets.QWidget;

public class InputItem extends QToolBar {

    public InputItem(String text, QWidget widget) {

            setOrientation(Qt.Orientation.Horizontal);
            addWidget(new QLabel(text));
            addWidget(widget);

    }

}
