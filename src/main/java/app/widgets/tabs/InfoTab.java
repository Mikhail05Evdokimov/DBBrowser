package app.widgets.tabs;

import io.qt.core.Qt;
import io.qt.widgets.*;

public class InfoTab extends QToolBar {

    private final QTextEdit output;
    private final InfoSwitcher infoSwitcher;
    private String ddlText;
    private String keysText;
    private String foreignKeysText;

    public InfoTab () {

        infoSwitcher = new InfoSwitcher(this);
        addWidget(infoSwitcher);

        setOrientation(Qt.Orientation.Vertical);
        output = new QTextEdit();
        output.setReadOnly(true);

        addWidget(output);

    }

    public void setOutput(String ddl1, String keys, String foreignKeys) {
        ddlText = ddl1;
        output.setText(ddlText);
        keysText = keys;
        foreignKeysText = foreignKeys;
    }

    void ddl() {
        infoSwitcher.setText("DDL");
        output.setText(ddlText);
    }

    void keys() {
        infoSwitcher.setText("Keys");
        output.setText(keysText);
    }

    void foreignKeys() {
        infoSwitcher.setText("Foreign keys");
        output.setText(foreignKeysText);
    }

}
