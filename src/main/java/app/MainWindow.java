package app;

import app.backend.controllers.ConnectionController;
import app.widgets.LoadMoreRowsButton;
import app.widgets.MyToolBar;
import app.widgets.explorer.ConnectionStorageView;
import app.widgets.TableView;
import app.widgets.explorer.TreeMenu;
import app.widgets.tabs.InfoTab;
import io.qt.core.Qt;
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
        ConnectionController.init(menuController);
        initControls();
        this.show();
    }

    // Текстовое поле, от куда будем брать текст для вывода в консоль
    public final QTextEdit edit = new QTextEdit();
    public final QLabel tableMessage = new QLabel("");
    public QLabel label = new QLabel("", this);
    public QMenu popMenu = new QMenu("DropDown", this);//вот эту шляпу в отдельный класс-конструктор вынести огда пэкэджи заработают
    private final QLabel output = new QLabel();
    public MenuController menuController = new MenuController(this);
    public TableView tableView = new TableView(menuController, true);
    public TreeMenu treeViewMenu = new TreeMenu();
    public QTextEdit dbName = new QTextEdit();
    public QTextEdit dbInfo = new QTextEdit();
    private QPushButton addRowButton;
    private LoadMoreRowsButton moreRowsButton;
    public TableView tableViewMainTab = new TableView(menuController, false);
    public ConnectionStorageView connectionStorageView = new ConnectionStorageView(this);
    public QLabel currentTableName = new QLabel("");
    public InfoTab infoTab = new InfoTab();
    private final QSizePolicy fixedSizePolicy = fixedSizePolicy();

    private void initControls() {

        // Создаём контейнер для виджетов
        QLayout layout = new QGridLayout( this );
        QToolBar bigBar = new QToolBar();
        bigBar.setOrientation(Qt.Orientation.Horizontal);
        QToolBar rightBar = new QToolBar();
        rightBar.setOrientation(Qt.Orientation.Vertical);

        QFont font = new QFont();
        font.setPixelSize(12);
        tableMessage.setFont(font);

        for (String i : ConnectionController.getAllConnections()) {
            connectionStorageView.addConnection(i);
        }

        LoadMoreRowsButton moreRowsButtonSQL = new LoadMoreRowsButton(menuController, 2);
        //barPallet.setColor(QPalette.ColorRole.Window, QColor.fromRgb(210, 210, 255));
        //bigBar.setBackgroundRole(QPalette.ColorRole.Window);
        //bigBar.setAutoFillBackground(true);
        //barPallet.setColor(QPalette.ColorRole.Text, QColor.fromRgb(250, 250, 250));
        //bigBar.setPalette(barPallet);

        output.setText("Your query results will be here");

        bigBar.addWidget(addSplitter(10, 10));

        QSizePolicy expandingSizePolicy = expandingSizePolicy();

        rightBar.setSizePolicy(expandingSizePolicy);

        treeViewMenu.setSizePolicy(fixedSizePolicy);

        bigBar.setSizePolicy(expandingSizePolicy);

        //Просто кнопка.
        QPushButton runQuery = newButton("Run query", "run_clicked()");
        runQuery.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);

        addAllActionsToPopUpMenu();

        QPushButton hideButton = newButton("Clear text", "clear_button_clicked()");
        QPalette pal = newPallet(254, 20, 20);
        hideButton.setPalette(pal);

        runQuery.customContextMenuRequested.connect(menuController, "rightClick()");

        rightBar.addWidget(hideButton);
        rightBar.setMinimumWidth(300);
        edit.setMinimumWidth(300);
        rightBar.addWidget( edit );
        rightBar.addWidget(label);
        rightBar.addWidget( runQuery );
        rightBar.addWidget(output);
        rightBar.addWidget(tableView);
        rightBar.addWidget(moreRowsButtonSQL);

        MyToolBar upButtonsBar = new MyToolBar();
        QPushButton selectFileButton = newButton("Connect to DB", "connectToDBButtonClicked()");
        upButtonsBar.addSeparator();
        upButtonsBar.addWidgetAndSeparator(selectFileButton);
        upButtonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton closeConnectionButton = newButton("Close connection", "closeConnectionButtonClicked()");
        QPushButton b1 = new QPushButton("PopUp menu");
        b1.setMenu(popMenu);
        QPushButton reconnectToDBButton = newButton("Reconnect to DB", "reconnectToDBClicked()");
        upButtonsBar.addWidgetAndSeparator(reconnectToDBButton);
        upButtonsBar.addWidgetAndSeparator(closeConnectionButton);
        upButtonsBar.addWidgetAndSeparator(b1);

        bigBar.addWidget(rightBar);
        QTabWidget tabWidget = new QTabWidget();

        setDbNameAndDBInfo();
        moreRowsButton = new LoadMoreRowsButton(menuController, 1);
        addRowButton = newButton("Add row", "addRowButtonClicked()");

        QToolBar mainTab = mainTabInit();

        tabWidget.addTab(mainTab, "MainTab");
        tabWidget.addTab(infoTab, "Info");

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

        layout.addWidget(new QLabel(" "));

        QToolBar footerBar = new QToolBar();
        footerBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton saveChangesButton = newButton("Save changes", "saveChanges()");
        QPushButton discardChangesButton = newButton("Discard changes", "discardChanges()");
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

    private QSplitter addSplitter(int w, int h) {
        QSplitter splitter = new QSplitter();
        splitter.setFixedSize(w, h);
        splitter.setSizePolicy(fixedSizePolicy);
        return splitter;
    }

    private QSizePolicy fixedSizePolicy() {
        QSizePolicy sizePolicy = new QSizePolicy();
        sizePolicy.setVerticalPolicy(QSizePolicy.Policy.Expanding);
        sizePolicy.setHorizontalPolicy(QSizePolicy.Policy.Fixed);
        return sizePolicy;
    }

    private QSizePolicy expandingSizePolicy() {
        QSizePolicy sizePolicy = new QSizePolicy();
        sizePolicy.setHorizontalPolicy(QSizePolicy.Policy.Expanding);
        sizePolicy.setVerticalPolicy(QSizePolicy.Policy.Expanding);
        return sizePolicy;
    }

    private void addAllActionsToPopUpMenu() {
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
    }

    private QPalette newPallet(int r, int g, int b) {
        QPalette pal = new QPalette();
        pal.setColor(QPalette.ColorRole.ButtonText, new QColor(r, g, b));
        return pal;
    }

    private QPushButton newButton(String text, String signal) {
        QPushButton button = new QPushButton(text);
        button.clicked.connect(menuController, signal);
        return button;
    }

    private QSizePolicy textSizePolicyFixed() {
        QSizePolicy textPolicy = new QSizePolicy();
        textPolicy.setVerticalPolicy(QSizePolicy.Policy.Fixed);
        textPolicy.setHorizontalPolicy(QSizePolicy.Policy.Fixed);
        return textPolicy;
    }

    private void setDbNameAndDBInfo() {
        dbName.setReadOnly(true);
        dbInfo.setReadOnly(true);
        QSizePolicy textPolicy = textSizePolicyFixed();
        dbName.setSizePolicy(textPolicy);
        dbName.setFixedSize(250, 28);
        dbInfo.setSizePolicy(textPolicy);
        dbInfo.setFixedSize(250, 28);
    }

    private QToolBar mainTabInit() {
        QToolBar mainTab = new QToolBar();
        mainTab.setOrientation(Qt.Orientation.Vertical);
        mainTab.addWidget(new QLabel("DB name:"));
        mainTab.addWidget(dbName);
        mainTab.addWidget(new QLabel(" "));
        mainTab.addWidget(new QLabel("DB info:"));
        mainTab.addWidget(dbInfo);
        mainTab.addWidget(new QLabel(" "));
        mainTab.addWidget(currentTableName);
        mainTab.addWidget(tableViewMainTab);
        mainTab.addWidget(tableMessage);
        mainTab.addWidget(moreRowsButton);
        mainTab.addWidget(addRowButton);
        return mainTab;
    }

}