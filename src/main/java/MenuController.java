import app.widgets.dialogs.CustomCheckBoxDialog;
import io.qt.gui.QCursor;
import io.qt.widgets.QCheckBox;
import io.qt.widgets.QMessageBox;
import app.widgets.dialogs.CheckBoxChecker;


public class MenuController {
    private static MainWindow root;

    public MenuController(MainWindow root) {
        MenuController.root = root;
    }

    void act1() {
        root.label.setText(" DON'T TOUCH ME!!!");
        root.label.show();
    }

    void rightClick() {
        root.popMenu.popup(QCursor.pos());
    }

    void on_button1_clicked()
    {
        if (root.edit.toPlainText().isEmpty()) {
            root.label.setText(" Empty text box. Write something.");
        }
        else {
            root.label.setText(" Your query done");
            //LocalStorage.setQuery(root.edit.toPlainText());

            //System.out.println( root.edit.toPlainText() );
            //if (root.edit.toPlainText().equals("select * from table")) {
            //    root.output.setText("1.   data_1\n2.   data_2\n3.   data_3");
            //}
        }
        root.label.show();
        root.edit.clear();
    }

    void on_button2_clicked()
    {
        root.label.clear();
        root.edit.clear();
    }

    void callTextBox()
    {
        QMessageBox.StandardButton reply =
            QMessageBox.question(root.window(), "Tile", "Cringe text box",
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No));
        if (reply == QMessageBox.StandardButton.Yes) {
            System.out.println("User said Yes");
        }
        else {
            System.out.println("User said NO");
        }
    }

    void callDefaultCheckBox() {
        QMessageBox messageBox = new QMessageBox();
        messageBox.setText("Cringe CheckBox");
        messageBox.setWindowTitle("Title");
        QCheckBox checkBox1 = new QCheckBox();
        checkBox1.setText("Point1");
        messageBox.setCheckBox(checkBox1);
        messageBox.setStandardButtons(QMessageBox.StandardButton.Abort, QMessageBox.StandardButton.Apply);
        CheckBoxChecker check1 = new CheckBoxChecker();
        checkBox1.stateChanged.connect(check1, "inverse()");

        int reply = messageBox.exec();

        if (QMessageBox.StandardButton.resolve(reply) == QMessageBox.StandardButton.Apply) {
            if (check1.getValue()) {
                System.out.println("User pick it");
            } else {
                System.out.println("User DIDN'T");
            }
        }
    }

    void callCustomCheckBox()
    {
        CustomCheckBoxDialog messageBox = new CustomCheckBoxDialog(root);
        //root.setEnabled(false);
        messageBox.open();

    }

    void selectFileButtonClicked() throws InterruptedException {
        FileDialog dialog = new FileDialog(root);

    }

}
