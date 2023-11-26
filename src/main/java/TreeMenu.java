import io.qt.core.QDir;
import io.qt.core.QStringListModel;
import io.qt.gui.QFileSystemModel;
import io.qt.widgets.QTreeView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для представления файлов/БД в виде дерева
 * Для второго варианта надо написать много кода, который парсит стрктуру БД
 * Сейчас единственыый компонет, который не умирает после подключения к БД
 * По двойному клику
 */
public class TreeMenu extends QTreeView {

    QFileSystemModel model = new QFileSystemModel();
    QStringListModel model1 = new QStringListModel();
    private final MainWindow root;

    public TreeMenu(MainWindow root) {
        this.root = root;
        this.setMinimumWidth(300);
        model.setRootPath(QDir.currentPath());
        this.doubleClicked.connect(this, "treeClicked()");
        //this.doubleClicked.connect(root, "treeClicked()");

        List<String> stringList = new ArrayList<>();
        stringList.add("first");
        stringList.add("second");
        model1.setStringList(stringList);
        this.setModel(model);

        //this.setModel(model1);
    }

    void treeClicked() throws SQLException {
        System.out.println(model.filePath(this.currentIndex()));
        //String query = root.edit.toPlainText();

        //LocalStorage.setQuery(query);
        //LocalStorage.execQuery();
        //root.output.setText(query);
        //System.out.println(model1.data(this.currentIndex()));
    }

}
