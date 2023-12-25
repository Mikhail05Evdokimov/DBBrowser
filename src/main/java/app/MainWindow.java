package app;

import app.backend.LocalStorage;
import app.backend.controllers.ConnectionController;
import app.widgets.explorer.ConnectionStorageView;
import app.widgets.TableView;
import app.widgets.explorer.TreeMenu;
import io.qt.core.Qt;
import io.qt.gui.QAction;
import io.qt.gui.QColor;
import io.qt.gui.QIcon;
import io.qt.gui.QPalette;
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
        ConnectionController.init(menuController);
        initControls();
        this.show();
    }

    // Текстовое поле, от куда будем брать текст для вывода в консоль
    public final QTextEdit edit = new QTextEdit();
    public QLabel label = new QLabel("", this);
    public QMenu popMenu = new QMenu("DropDown", this);//вот эту шляпу в отдельный класс-конструктор вынести огда пэкэджи заработают
    public QLabel output = new QLabel();
    public MenuController menuController = new MenuController(this);
    TableView tableView = new TableView(menuController);
    TreeMenu treeViewMenu = new TreeMenu();
    public QTextEdit dbName = new QTextEdit();
    QTextEdit dbInfo = new QTextEdit();
    public QPushButton addRowButton;
    public QPushButton moreRowsButton;
    public TableView tableViewMainTab = new TableView(menuController);
    ConnectionStorageView connectionStorageView = new ConnectionStorageView(this);

    private void initControls() {

        // Создаём контейнер для виджетов
        QLayout layout = new QGridLayout( this );
        QToolBar bigBar = new QToolBar();
        bigBar.setOrientation(Qt.Orientation.Horizontal);
        QToolBar rightBar = new QToolBar();
        rightBar.setOrientation(Qt.Orientation.Vertical);

        //QPalette barPallet = new QPalette();
        //barPallet.setColor(QPalette.ColorRole.Window, QColor.fromRgb(210, 210, 255));
        //bigBar.setBackgroundRole(QPalette.ColorRole.Window);
        //bigBar.setAutoFillBackground(true);
        //barPallet.setColor(QPalette.ColorRole.Text, QColor.fromRgb(250, 250, 250));
        //bigBar.setPalette(barPallet);

        output.setText("Your query results will be here");

        QSizePolicy fixedSizePolicy = new QSizePolicy();
        fixedSizePolicy.setVerticalPolicy(QSizePolicy.Policy.Expanding);
        fixedSizePolicy.setHorizontalPolicy(QSizePolicy.Policy.Fixed);

        QSplitter splitter1 = new QSplitter();
        splitter1.setFixedSize(10, 10);
        splitter1.setSizePolicy(fixedSizePolicy);
        bigBar.addWidget(splitter1);

        QSizePolicy expandingSizePolicy = new QSizePolicy();
        expandingSizePolicy.setHorizontalPolicy(QSizePolicy.Policy.Expanding);
        expandingSizePolicy.setVerticalPolicy(QSizePolicy.Policy.Expanding);

        rightBar.setSizePolicy(expandingSizePolicy);

        treeViewMenu.setSizePolicy(fixedSizePolicy);

        bigBar.setSizePolicy(expandingSizePolicy);

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
        //QAction showSchema = new QAction("showSchema");
        //showSchema.triggered.connect(menuController, "showSchema(String)");
        //popMenu.addAction(showSchema);

        QPushButton hideButton = new QPushButton( "Clear text" );
        QPalette pal = new QPalette();
        pal.setColor(QPalette.ColorRole.ButtonText, new QColor(254, 20, 20));
        hideButton.setPalette(pal);

        runQuery.clicked.connect(menuController, "run_clicked()" );
        hideButton.clicked.connect(menuController, "clear_button_clicked()" );
        runQuery.customContextMenuRequested.connect(menuController, "rightClick()");

        rightBar.addWidget(hideButton);
        rightBar.setMinimumWidth(300);
        edit.setMinimumWidth(300);
        rightBar.addWidget( edit );
        rightBar.addWidget(label);
        rightBar.addWidget( runQuery );
        rightBar.addWidget(output);
        rightBar.addWidget(tableView);

        QToolBar upButtonsBar = new QToolBar();
        QPushButton selectFileButton = new QPushButton("Connect to DB");
        selectFileButton.clicked.connect(menuController, "connectToDBButtonClicked()");
        upButtonsBar.addSeparator();
        upButtonsBar.addWidget(selectFileButton);
        upButtonsBar.addSeparator();
        upButtonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton closeConnectionButton = new QPushButton("Close connection");
        closeConnectionButton.clicked.connect(menuController, "closeConnectionButtonClicked()");
        QPushButton b1 = new QPushButton("PopUp menu");
        b1.setMenu(popMenu);
        QPushButton reconnectToDBButton = new QPushButton("Reconnect to DB");
        reconnectToDBButton.clicked.connect(menuController, "reconnectToDBClicked()");
        upButtonsBar.addWidget(reconnectToDBButton);
        upButtonsBar.addSeparator();
        upButtonsBar.addWidget(closeConnectionButton);
        upButtonsBar.addSeparator();
        upButtonsBar.addWidget(b1);
        upButtonsBar.addSeparator();

        bigBar.addWidget(rightBar);
        QTabWidget tabWidget = new QTabWidget();

        QToolBar mainTab = new QToolBar();
        mainTab.setOrientation(Qt.Orientation.Vertical);
        mainTab.addWidget(new QLabel("DB name:"));
        dbName.setReadOnly(true);
        dbInfo.setReadOnly(true);
        QSizePolicy textPolicy = new QSizePolicy();
        textPolicy.setVerticalPolicy(QSizePolicy.Policy.Fixed);
        textPolicy.setHorizontalPolicy(QSizePolicy.Policy.Fixed);
        dbName.setSizePolicy(textPolicy);
        dbName.setFixedSize(250, 28);
        dbInfo.setSizePolicy(textPolicy);
        dbInfo.setFixedSize(250, 28);
        mainTab.addWidget(dbName);
        mainTab.addWidget(new QLabel(" "));
        mainTab.addWidget(new QLabel("DB info:"));
        mainTab.addWidget(dbInfo);
        mainTab.addWidget(new QLabel(" "));
        mainTab.addWidget(tableViewMainTab);

        moreRowsButton = new QPushButton("Load more rows");
        moreRowsButton.clicked.connect(menuController, "moreRows()");

        addRowButton = new QPushButton("Add row");
        addRowButton.clicked.connect(menuController, "addRowButtonClicked()");

        //hideTableViewButtons();
        mainTab.addWidget(moreRowsButton);
        mainTab.addWidget(addRowButton);

        tabWidget.addTab(mainTab, "MainTab");

        QToolBar sqlTab = new QToolBar();
        sqlTab.setOrientation(Qt.Orientation.Vertical);
        sqlTab.addWidget(bigBar);
        tabWidget.addTab(sqlTab, "SQL");

        QToolBar veryBigBar = new QToolBar();
        veryBigBar.setOrientation(Qt.Orientation.Horizontal);
        QToolBar explorer = new QToolBar();
        explorer.setOrientation(Qt.Orientation.Vertical);
        explorer.addWidget(connectionStorageView);
        explorer.addWidget(treeViewMenu);
        veryBigBar.addWidget(explorer);
        veryBigBar.addWidget(tabWidget);

        layout.addWidget(upButtonsBar);
        tabWidget.sizePolicy().setVerticalPolicy(QSizePolicy.Policy.Expanding);
        veryBigBar.setSizePolicy(expandingSizePolicy);
        layout.addWidget(veryBigBar);

        QSplitter downSplitter = new QSplitter();
        downSplitter.setFixedSize(5, 5);
        downSplitter.sizePolicy().setVerticalPolicy(QSizePolicy.Policy.Fixed);
        layout.addWidget(downSplitter);

        QToolBar footerBar = new QToolBar();
        footerBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton saveChangesButton = new QPushButton("Save changes");
        QPushButton discardChangesButton = new QPushButton("Discard changes");
        saveChangesButton.clicked.connect(menuController, "saveChanges()");
        discardChangesButton.clicked.connect(menuController, "discardChanges()");
        footerBar.addWidget(saveChangesButton);
        footerBar.addWidget(discardChangesButton);

        hideTableViewButtons();

        layout.addWidget(footerBar);
    }

    public void hideTableViewButtons () {
        moreRowsButton.setDisabled(true);
        addRowButton.setDisabled(true);
    }

    public void showTableViewButtons () {
        moreRowsButton.setDisabled(false);
        addRowButton.setDisabled(false);
    }

}
