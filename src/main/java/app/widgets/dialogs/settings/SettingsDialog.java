package app.widgets.dialogs.settings;

import app.widgets.MyToolBar;
import io.qt.gui.QIcon;
import io.qt.widgets.QDialog;
import io.qt.widgets.QGridLayout;
import io.qt.widgets.QTabWidget;
import io.qt.widgets.QToolBar;

public class SettingsDialog extends QDialog {

    public SettingsDialog(QIcon icon) {
        setWindowIcon(icon);
        QTabWidget tabs = new QTabWidget();
        QToolBar accountTab = new AccountSettings();
        QToolBar managementTab = new MyToolBar();
        tabs.addTab(accountTab, "Account");
        tabs.addTab(managementTab, "Management");
        QGridLayout layout = new QGridLayout();
        layout.addWidget(tabs);
        setLayout(layout);
        this.show();
    }



}
