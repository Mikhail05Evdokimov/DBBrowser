package app;

import app.backend.LocalStorage;
import app.widgets.TableView;
import app.widgets.TreeMenu;
import app.widgets.dialogs.StartDialog;
import io.qt.core.*;
import io.qt.gui.*;
import io.qt.widgets.*;

import java.io.IOException;


/**
 * Класс главного окна, инициализирует все компоненты, вид окошка и всё такое.
 * Много тестового кода
 * Есть штуки, которые надо вынести в отдельные классы и создавать их
 * здесь вызовом конструктора
 */
public class MainWindow extends QWidget {

    public MainWindow(QIcon newIcon) throws IOException {
        setWindowTitle( "DB browser" );
        setWindowIcon(newIcon);
        LocalStorage.setOutput(output);
        LocalStorage.setMenuController(menuController);
        initControls();
        this.show();
    }

    // Текстовое поле, от куда будем брать текст для вывода в консоль
    public final QTextEdit edit = new QTextEdit();
    public QLabel label = new QLabel("", this);
    public QMenu popMenu = new QMenu("DropDown", this);//вот эту шляпу в отдельный класс-конструктор вынести огда пэкэджи заработают
    public QLabel output = new QLabel();
    MenuController menuController = new MenuController(this);
    TableView tableView = new TableView();
    TreeMenu treeViewMenu = new TreeMenu();

    private void initControls() {

        // Создаём контейнер для виджетов
        QLayout layout = new QGridLayout( this );
        QToolBar bigBar = new QToolBar();
        bigBar.setOrientation(Qt.Orientation.Horizontal);
        QToolBar rightBar = new QToolBar();
        rightBar.setOrientation(Qt.Orientation.Vertical);

        bigBar.addWidget(treeViewMenu);

        //bigBar.addWidget(tableView);

        //output.setReadOnly(true);
        output.setText("Your query results will be here");

        QSplitter splitter1 = new QSplitter();
        splitter1.setFixedSize(10, 10);
        QSplitter splitter2 = new QSplitter();
        splitter2.setFixedSize(7, 7);
        bigBar.addWidget(splitter1);
        bigBar.addSeparator();
        bigBar.addWidget(splitter2);

       // QTabWidget tabWidget = new QTabWidget();
        //bigBar.addWidget(output);
        //tabWidget.addTab(bigBar, "bigBar");

        //tabWidget.addTab(output, "output");

        //layout.addWidget(tabWidget);
        layout.addWidget(bigBar);

        //Просто кнопка.
        QPushButton runQuery = new QPushButton( "Run query" );

        runQuery.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        QAction act1 = new QAction("act1");
        act1.triggered.connect(menuController, "act1()");
        popMenu.addAction(act1);
        popMenu.addSeparator();
        QAction callTextBox = new QAction("callTextBox");
        callTextBox.triggered.connect(menuController, "callTextBox()");
        popMenu.addAction(callTextBox);
        QAction callCustomCheckBox = new QAction("callCustomCheckBox");
        callCustomCheckBox.triggered.connect(menuController, "callCustomCheckBox()");
        popMenu.addAction(callCustomCheckBox);
        QAction callDefaultCheckBox = new QAction("callDefaultCheckBox");
        callDefaultCheckBox.triggered.connect(menuController, "callDefaultCheckBox()");
        popMenu.addAction(callDefaultCheckBox);
        QAction showSchema = new QAction("showSchema");
        showSchema.triggered.connect(menuController, "showSchema()");
        popMenu.addAction(showSchema);

        QPushButton hideButton = new QPushButton( "Clear text" );
        QPalette pal = new QPalette();
        pal.setColor(QPalette.ColorRole.ButtonText, new QColor(254, 20, 20));
        hideButton.setPalette(pal);

        runQuery.clicked.connect(menuController, "run_clicked()" );
        hideButton.clicked.connect(menuController, "clear_button_clicked()" );
        runQuery.customContextMenuRequested.connect(menuController, "rightClick()");

        rightBar.addWidget(hideButton);
        rightBar.addWidget( edit );
        rightBar.addWidget(label);
        rightBar.addWidget( runQuery );

        QToolBar bottomButtonsBar = new QToolBar();
        QPushButton selectFileButton = new QPushButton("Connect to DB");
        selectFileButton.clicked.connect(menuController, "selectFileButtonClicked()");
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.addWidget(selectFileButton);
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton closeConnectionButton = new QPushButton("Close connection");
        closeConnectionButton.clicked.connect(menuController, "closeConnectionButtonClicked()");
        QPushButton b1 = new QPushButton("PopUp menu");
        b1.setMenu(popMenu);
        bottomButtonsBar.addWidget(closeConnectionButton);
        bottomButtonsBar.addSeparator();
        QPushButton reconnectToDBButton = new QPushButton("Reconnect to DB");
        reconnectToDBButton.clicked.connect(menuController, "reconnectToDBClicked()");
        bottomButtonsBar.addWidget(reconnectToDBButton);
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.addWidget(b1);
        bottomButtonsBar.addSeparator();

        bigBar.addWidget(rightBar);
        //bigBar.addWidget(splitter1);
        //bigBar.addSeparator();
        //bigBar.addWidget(splitter2);
        //bigBar.addWidget(output);
        //layout.addWidget(bigBar);
        layout.addWidget(output);
        layout.addWidget(tableView);
        QSpacerItem midSpacer = new QSpacerItem(10, 10);
        layout.addItem(midSpacer);
        layout.addWidget(bottomButtonsBar);

    }

}
