package app.widgets;

import io.qt.core.*;
import io.qt.gui.QFileSystemModel;
import io.qt.gui.QStandardItem;
import io.qt.gui.QStandardItemModel;
import io.qt.widgets.QLabel;
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
    private final QFileSystemModel fileModel;
    private final QStandardItemModel treeStringListModel;
    public final Signal0 signal0 = new Signal0();

    public TreeMenu() {
        this.setMinimumWidth(300);
        fileModel = new QFileSystemModel();
        fileModel.setRootPath(QDir.currentPath());

        //this.doubleClicked.connect(this, "treeClicked()");
        //this.setModel(model);

        treeStringListModel = new QStandardItemModel();
        QStandardItem rootItem = treeStringListModel.invisibleRootItem();
        QStandardItem item1 = new QStandardItem("Hello");
        QStandardItem item2 = new QStandardItem("World");
        QStandardItem child1 = new QStandardItem("child1");
        QStandardItem child2 = new QStandardItem("child2");
        QStandardItem child0 = new QStandardItem("child0");
        QStandardItem child10 = new QStandardItem("child10");
        item2.setChild(0, child1);
        item2.setChild(1, child2);
        item1.setChild(0, child0);
        child1.setChild(0, child10);
        this.collapsed.connect(this, "collapse1(QModelIndex)");
        this.expanded.connect(this, "expanded1(QModelIndex)");
        assert rootItem != null;
        rootItem.appendRow(item1);
        rootItem.appendRow(item2);
        this.setModel(treeStringListModel);

        connect(this, "signal0", this, "helloWorld()");
    }

    public void setTreeModel(List<String> stringList) {
        dbModel = new QStringListModel();
        dbModel.setStringList(stringList);
        this.setModel(dbModel);
        this.doubleClicked.connect(this, "treeClicked()");
    }

    public void setFileModel() {
        this.setModel(fileModel);
    }

    void treeClicked() {
        //System.out.println(model.filePath(this.currentIndex()));
        //signal0.emit();
        //System.out.println(this.currentIndex().data());
        //System.out.println(this.currentIndex().row());
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

    // Закрыть список детей
    void collapse1(QModelIndex index) {
        System.out.println(treeStringListModel.data(index) + " Collapsed " );
    }

    // Открыть список детей
    void expanded1(QModelIndex index) {
        System.out.println(treeStringListModel.data(index) + " Expanded " );
        //this.setCurrentIndex(this.expanded);
    }

}
