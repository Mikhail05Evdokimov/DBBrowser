package app.widgets;

import io.qt.core.QDir;
import io.qt.core.QStringListModel;
import io.qt.gui.QFileSystemModel;
import io.qt.widgets.QTreeView;

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
    QStringListModel dbModel;

    public TreeMenu() {
        this.setMinimumWidth(300);
        model.setRootPath(QDir.currentPath());
        this.doubleClicked.connect(this, "treeClicked()");
        this.setModel(model);
        //this.setModel(model1);
    }

    public void setTreeModel(List<String> stringList) {
        dbModel = new QStringListModel();
        dbModel.setStringList(stringList);
        this.setModel(dbModel);
    }

    void treeClicked() {
        //System.out.println(model.filePath(this.currentIndex()));
        //String query = root.edit.toPlainText();
        //LocalStorage.setQuery(query);
        //LocalStorage.execQuery();
        //root.output.setText(query);
        //System.out.println(model1.data(this.currentIndex()));
    }

}
