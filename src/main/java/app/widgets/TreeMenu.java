package app.widgets;

import app.IconLoader;
import app.backend.LocalStorage;
import app.backend.controllers.ConnectionController;
import app.backend.entities.Connection;
import io.qt.core.QDir;
import io.qt.core.QModelIndex;
import io.qt.core.Qt;
import io.qt.gui.*;
import io.qt.widgets.QTreeView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
    public final Signal0 signal0 = new Signal0();

    public TreeMenu() { //////////// При измеении имени чего-либо в TreeView пусть оно записвает изменения в локальное отображение базы
        this.setMinimumWidth(300);
        fileModel = new QFileSystemModel();
        fileModel.setRootPath(QDir.currentPath());
        dbModel = new QStandardItemModel();
        //this.doubleClicked.connect(this, "treeClicked()");
        //this.setModel(model);

        this.collapsed.connect(this, "collapse1(QModelIndex)");
        this.expanded.connect(this, "expanded1(QModelIndex)");

        //connect(this, "signal0", this, "helloWorld()");
    }

    public void setTreeModel(List<String> stringList, String conName) throws IOException {
        dbModel = new QStandardItemModel();
        QStandardItem invisibleRootItem = dbModel.invisibleRootItem();
        assert invisibleRootItem != null;
        invisibleRootItem.setData(conName);
        for (String i : stringList) {
            QStandardItem item = new QStandardItem(i);
            item.setEditable(false);
            item.setChild(0, new QStandardItem());
            item.setIcon(loadIcon());
            invisibleRootItem.appendRow(item);
        }
        //dbModel.dataChanged.connect(this, "renamingInTree(QModelIndex)");
        //dbModel.itemChanged.connect(this, "itemChanged()");
        this.setModel(dbModel);
        this.doubleClicked.connect(this, "treeClicked(QModelIndex)");

    }

    public void setFileModel() {
        this.setModel(fileModel);
    }

    public void newCurrentConnectionName(String conName) {
        Objects.requireNonNull(dbModel.invisibleRootItem()).setData(conName);
    }

    public QIcon loadIcon() throws IOException {
        return new IconLoader().loadIcon("../table.png");
    }

    void treeClicked(QModelIndex index) {

        ConnectionController.getContentInTable(Objects.requireNonNull(dbModel.invisibleRootItem()).data().toString(), dbModel.data(index).toString());

        //System.out.println(dbModel.data(index).toString());
        //System.out.println(index);
        //signal0.emit();
        //System.out.println(this.currentIndex().data());
        //System.out.println(this.currentIndex().row());
        //System.out.println(model1.data(this.currentIndex()));
    }

    void renamingInTree(QModelIndex index) {
        //System.out.println(this.currentIndex());

        System.out.println("new name: " + dbModel.data(index));
        }

    // Закрыть список детей
    void collapse1(QModelIndex index) {
        System.out.println(dbModel.data(index) + " Collapsed" );
    }

    // Открыть список детей
    void expanded1(QModelIndex index) throws SQLException {
        List<String> children = ConnectionController.getColumns((String) Objects.requireNonNull(dbModel.invisibleRootItem()).data(), dbModel.data(index).toString());//LocalStorage.getChildren(dbModel.data(index).toString());
        int i = 0;
        for (String child : children) {
            QStandardItem childItem = new QStandardItem(child);
            childItem.setEditable(false);
            Objects.requireNonNull(Objects.requireNonNull(dbModel.invisibleRootItem()).child(index.row())).setChild(i, childItem);
            i++;
        }
        System.out.println(dbModel.data(index) + " Expanded" );

    }

}
