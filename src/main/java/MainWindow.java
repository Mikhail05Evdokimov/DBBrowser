import app.backend.LocalStorage;
import io.qt.core.Qt;
import io.qt.gui.*;
import io.qt.widgets.*;

import java.io.IOException;
import java.util.Objects;

/**
 * Класс главного окна, инициализирует все компоненты, вид окошка и всё такое.
 * Много тестового кода
 * Есть штуки, которые надо вынести в отдельные классы и создавать их
 * здесь вызовом конструктора
 */
public class MainWindow extends QWidget {

    public MainWindow() throws IOException {
        setWindowTitle( "DB browser" );
        QPixmap pixmap = new QPixmap();
        pixmap.loadFromData(Objects.requireNonNull(this.getClass().getResourceAsStream("looool.png")).readAllBytes());
        QIcon icon = new QIcon();
        icon.addPixmap(pixmap);
        setWindowIcon(icon);
        LocalStorage.setOutput(output);
        initControls();
    }

    // Текстовое поле, от куда будем брать текст для вывода в консоль
    final QTextEdit edit = new QTextEdit();
    QLabel label = new QLabel("", this);
    QMenu popMenu = new QMenu("DropDown", this);//вот эту шляпу в отдельный класс-конструктор вынести
    QTextEdit output = new QTextEdit();
    MenuController menuController = new MenuController(this);

    private void initControls() throws IOException {


        // Создаём контейнер для виджетов
        QLayout layout = new QGridLayout( this );
        QToolBar bigBar = new QToolBar();
        bigBar.setOrientation(Qt.Orientation.Horizontal);
        QToolBar rightBar = new QToolBar();
        rightBar.setOrientation(Qt.Orientation.Vertical);

        TreeMenu treeViewMenu = new TreeMenu(this);
        bigBar.addWidget(treeViewMenu);

        output.setReadOnly(true);
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
        QPushButton sendMessage = new QPushButton( "Send message" );

        sendMessage.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        QAction act1 = new QAction("act1");
        QPixmap pixmap = new QPixmap();
        pixmap.loadFromData(Objects.requireNonNull(this.getClass().getResourceAsStream("looool.png")).readAllBytes());
        act1.setIcon(new QIcon(pixmap));
        act1.triggered.connect(menuController, "act1()");
        popMenu.addAction(act1);
        popMenu.addSeparator();
        QAction callTextBox = new QAction("callTextBox");
        callTextBox.triggered.connect(menuController, "callTextBox()");
        popMenu.addAction(callTextBox);
        QAction callCustomCheckBox = new QAction("callCustomCheckBox");
        callCustomCheckBox.triggered.connect(menuController, "callCustomCheckBox()");
        popMenu.addAction(callCustomCheckBox);
        //QAction callDefaultCheckBox = new QAction("callDefaultCheckBox");
        //callDefaultCheckBox.triggered.connect(menuController, "callDefaultCheckBox()");
        //popMenu.addAction(callDefaultCheckBox);

        QPushButton hideButton = new QPushButton( "Clear text" );
        QPalette pal = new QPalette();
        pal.setColor(QPalette.ColorRole.ButtonText, new QColor(254, 20, 20));
        hideButton.setPalette(pal);

        //QMdiSubWindow subWindow = new QMdiSubWindow();
        //subWindow.setWindowTitle("SuBOne");
        //subWindow.setWidget(new QPushButton( "None text" ));
        //subWindow.setKeyboardPageStep(2);
        //subWindow.show();

        //QMdiArea area = new QMdiArea();
        //area.setActiveSubWindow(subWindow);
        //area.setAutoFillBackground(true);
        //area.show();

        sendMessage.clicked.connect( menuController, "on_button1_clicked()" );
        hideButton.clicked.connect(menuController, "on_button2_clicked()" );
        sendMessage.customContextMenuRequested.connect(menuController, "rightClick()");

        rightBar.addWidget(hideButton);
        rightBar.addWidget( edit );
        rightBar.addWidget(label);
        rightBar.addWidget( sendMessage );

        QToolBar bar = new QToolBar();
        QPushButton selectFileButton = new QPushButton("selectFile");
        selectFileButton.clicked.connect(menuController, "selectFileButtonClicked()");
        bar.addSeparator();
        bar.addWidget(selectFileButton);
        bar.addSeparator();
        bar.setOrientation(Qt.Orientation.Horizontal);
        QLabel l = new QLabel("---------");
        QPushButton b1 = new QPushButton("omg");
        b1.setMenu(popMenu);
        bar.addWidget(l);
        bar.addSeparator();
        bar.addWidget(b1);
        bar.addSeparator();

        bigBar.addWidget(rightBar);
        //bigBar.addWidget(splitter1);
        bigBar.addSeparator();
        //bigBar.addWidget(splitter2);
        //bigBar.addWidget(output);
        //layout.addWidget(bigBar);
        layout.addWidget(output);
        QSpacerItem midSpacer = new QSpacerItem(10, 10);
        layout.addItem(midSpacer);
        layout.addWidget(bar);

    }

}
