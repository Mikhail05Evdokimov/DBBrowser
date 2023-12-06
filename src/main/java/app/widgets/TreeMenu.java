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

    private QStringListModel dbModel;
    private final QFileSystemModel model;
    public final Signal0 signal0 = new Signal0();

    public TreeMenu() {
        this.setMinimumWidth(300);
        model = new QFileSystemModel();
        model.setRootPath(QDir.currentPath());
        this.doubleClicked.connect(this, "treeClicked()");
        this.setModel(model);
        //this.setModel(model1);
        connect(this, "signal0", this, "helloWorld()");
    }

    public void setTreeModel(List<String> stringList) {
        dbModel = new QStringListModel();
        dbModel.setStringList(stringList);
        this.setModel(dbModel);
    }

    public void setFileModel() {
        this.setModel(model);
    }

    void treeClicked() {
        System.out.println(model.filePath(this.currentIndex()));
        signal0.emit();
        //String query = root.edit.toPlainText();
        //LocalStorage.setQuery(query);
        //LocalStorage.execQuery();
        //root.output.setText(query);
        //System.out.println(model1.data(this.currentIndex()));
    }

    void helloWorld() {
        dbModel = new QStringListModel();
        List<String> list = new ArrayList<>();
        list.add("SJJJJJJJ");
        dbModel.setStringList(list);
        this.setModel(dbModel);
        System.out.println("HELLO");
    }

}
