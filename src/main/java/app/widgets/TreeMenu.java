package app.widgets;

import app.backend.LocalStorage;
import io.qt.core.*;
import io.qt.gui.QFileSystemModel;
import io.qt.gui.QStandardItem;
import io.qt.gui.QStandardItemModel;
import io.qt.widgets.QLabel;
import io.qt.widgets.QTreeView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Класс для представления файлов/БД в виде дерева
 * Для второго варианта надо написать много кода, который парсит стрктуру БД
 * Сейчас единственыый компонет, который не умирает после подключения к БД
 * По двойному клику
 */
public class TreeMenu extends QTreeView {

    private QStandardItemModel dbModel;
    private final QFileSystemModel fileModel;
    private final QStandardItemModel treeStringListModel;
    public final Signal0 signal0 = new Signal0();

    public TreeMenu() {
        this.setMinimumWidth(300);
        fileModel = new QFileSystemModel();
        fileModel.setRootPath(QDir.currentPath());
        dbModel = new QStandardItemModel();
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

        //connect(this, "signal0", this, "helloWorld()");
    }

    public void setTreeModel(List<String> stringList) {
        dbModel = new QStandardItemModel();
        QStandardItem invisibleRootItem = dbModel.invisibleRootItem();
        assert invisibleRootItem != null;
        for (String i : stringList) {
            QStandardItem item = new QStandardItem(i);
            item.setChild(0, new QStandardItem());
            invisibleRootItem.appendRow(item);
        }
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


    // Закрыть список детей
    void collapse1(QModelIndex index) {
        System.out.println(dbModel.data(index) + " Collapsed" );
    }

    // Открыть список детей
    void expanded1(QModelIndex index) throws SQLException {
        List<String> children = LocalStorage.getChildren(dbModel.data(index).toString());
        int i = 0;
        for (String child : children) {
            Objects.requireNonNull(Objects.requireNonNull(dbModel.invisibleRootItem()).child(index.row())).setChild(i, new QStandardItem(child));
            i++;
        }
        System.out.println(dbModel.data(index) + " Expanded" );

    }

}
