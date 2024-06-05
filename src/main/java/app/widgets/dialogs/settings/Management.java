package app.widgets.dialogs.settings;

import app.widgets.MyToolBar;
import io.qt.core.Qt;
import io.qt.widgets.QLabel;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QSizePolicy;
import io.qt.widgets.QToolBar;

public class Management extends MyToolBar {

    public Management() {
        setOrientation(Qt.Orientation.Vertical);
        addWidget(new QLabel("Your companies:"));
        addWidget(companies());
        QPushButton addCompanyButton = new QPushButton("+");
        addCompanyButton.clicked.connect(this, "addCompanyClicked()");
        addWidget(addCompanyButton);
    }

    private QToolBar companies() {
        QToolBar comps = new QToolBar();
        //comps.sizePolicy().setHorizontalPolicy(QSizePolicy.Policy.Expanding);
        comps.addWidget(new QPushButton("Company1"));
        //get company list and make button for each
        return comps;
    }

    void addCompanyClicked() {

    }

}
