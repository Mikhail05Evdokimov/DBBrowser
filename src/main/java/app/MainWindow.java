package app;

import app.backend.LocalStorage;
import app.widgets.TableView;
import app.widgets.TreeMenu;
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
    QTextEdit dbInfo = new QTextEdit();

    private void initControls() {

        // Создаём контейнер для виджетов
        QLayout layout = new QGridLayout( this );
        QToolBar bigBar = new QToolBar();
        bigBar.setOrientation(Qt.Orientation.Horizontal);
        QToolBar rightBar = new QToolBar();
        rightBar.setOrientation(Qt.Orientation.Vertical);

        //bigBar.addWidget(treeViewMenu);
        QPalette barPallet = new QPalette();
        barPallet.setColor(QPalette.ColorRole.Window, QColor.fromRgb(210, 210, 255));
        bigBar.setBackgroundRole(QPalette.ColorRole.Window);
        //bigBar.setAutoFillBackground(true);
        //barPallet.setColor(QPalette.ColorRole.Text, QColor.fromRgb(250, 250, 250));
        bigBar.setPalette(barPallet);

        //bigBar.addWidget(tableView);

        //output.setReadOnly(true);
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
        rightBar.setMinimumWidth(300);
        edit.setMinimumWidth(300);
        rightBar.addWidget( edit );
        rightBar.addWidget(label);
        rightBar.addWidget( runQuery );
        rightBar.addWidget(output);
        rightBar.addWidget(tableView);

        QToolBar bottomButtonsBar = new QToolBar();
        QPushButton selectFileButton = new QPushButton("Connect to DB");
        selectFileButton.clicked.connect(menuController, "connectToDBButtonClicked()");
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.addWidget(selectFileButton);
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.setOrientation(Qt.Orientation.Horizontal);
        QPushButton closeConnectionButton = new QPushButton("Close connection");
        closeConnectionButton.clicked.connect(menuController, "closeConnectionButtonClicked()");
        QPushButton b1 = new QPushButton("PopUp menu");
        b1.setMenu(popMenu);
        QPushButton reconnectToDBButton = new QPushButton("Reconnect to DB");
        reconnectToDBButton.clicked.connect(menuController, "reconnectToDBClicked()");
        bottomButtonsBar.addWidget(reconnectToDBButton);
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.addWidget(closeConnectionButton);
        bottomButtonsBar.addSeparator();
        bottomButtonsBar.addWidget(b1);
        bottomButtonsBar.addSeparator();

        bigBar.addWidget(rightBar);
        //bigBar.addWidget(output);
        //layout.addWidget(bigBar);
        QTabWidget tabWidget = new QTabWidget();

        QToolBar mainTab = new QToolBar();
        mainTab.setOrientation(Qt.Orientation.Vertical);
        //mainTab.addWidget(new QLabel("Hello world"));
        mainTab.addWidget(new QLabel("Info:"));
        dbInfo.setReadOnly(true);
        QSizePolicy textPolicy = new QSizePolicy();
        textPolicy.setVerticalPolicy(QSizePolicy.Policy.Fixed);
        textPolicy.setHorizontalPolicy(QSizePolicy.Policy.Fixed);
        dbInfo.setSizePolicy(textPolicy);
        dbInfo.setFixedSize(150, 28);
        mainTab.addWidget(dbInfo);
        tabWidget.addTab(mainTab, "MainTab");

        QToolBar sqlTab = new QToolBar();
        sqlTab.setOrientation(Qt.Orientation.Vertical);
        sqlTab.addWidget(bigBar);
        //sqlTab.addWidget(output);
        //sqlTab.addWidget(tableView);
        tabWidget.addTab(sqlTab, "SQL");

        QToolBar veryBigBar = new QToolBar();
        veryBigBar.setOrientation(Qt.Orientation.Horizontal);
        veryBigBar.addWidget(treeViewMenu);
        veryBigBar.addWidget(tabWidget);

        layout.addWidget(bottomButtonsBar);
        tabWidget.sizePolicy().setVerticalPolicy(QSizePolicy.Policy.Expanding);
        veryBigBar.setSizePolicy(expandingSizePolicy);
        layout.addWidget(veryBigBar);
        QSpacerItem midSpacer = new QSpacerItem(10, 10);
        midSpacer.sizePolicy().setVerticalPolicy(QSizePolicy.Policy.Fixed);
        //layout.addItem(midSpacer);
        //layout.addWidget(bottomButtonsBar);

    }

}
