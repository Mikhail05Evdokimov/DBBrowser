package app.widgets.explorer;

import app.IconLoader;
import app.backend.controllers.ConnectionController;
import io.qt.core.QModelIndex;
import io.qt.core.QStringListModel;
import io.qt.gui.*;
import io.qt.widgets.QTreeView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private final QStringListModel emptyModel;
    public final Signal0 signal0 = new Signal0();

    public TreeMenu() {
        this.setMinimumWidth(300);
        emptyModel = new QStringListModel();
        List<String> emptyTreeMessage = new ArrayList<>();
        emptyTreeMessage.add("Current connection isn't active");
        emptyModel.setStringList(emptyTreeMessage);
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

        QStandardItem tables = new QStandardItem("Tables");
        tables.setEditable(false);
        QStandardItem indexes = new QStandardItem("Indexes");
        indexes.setEditable(false);
        QStandardItem views = new QStandardItem("Views");
        views.setEditable(false);

        invisibleRootItem.appendRow(tables);
        invisibleRootItem.appendRow(indexes);
        invisibleRootItem.appendRow(views);

        for (String i : stringList) {
            QStandardItem item = new QStandardItem(i);
            item.setEditable(false);
            item.setChild(0, new QStandardItem());
            item.setIcon(loadIcon());
            tables.appendRow(item);
        }

        var ind = ConnectionController.getIndexes(conName);
        for (String i : ind) {
            QStandardItem item = new QStandardItem(i);
            item.setEditable(false);
            indexes.appendRow(item);
        }
        ind = ConnectionController.getView(conName);
        for (String i : ind) {
            QStandardItem item = new QStandardItem(i);
            item.setEditable(false);
            indexes.appendRow(item);
        }

        this.setModel(dbModel);
        this.doubleClicked.connect(this, "treeClicked(QModelIndex)");

    }

    public void setEmptyModel() {
        this.setModel(emptyModel);
    }

    public void newCurrentConnectionName(String conName) {
        Objects.requireNonNull(dbModel.invisibleRootItem()).setData(conName);
    }

    public QIcon loadIcon() throws IOException {
        return new IconLoader().loadIcon("../table.png");
    }

    void treeClicked(QModelIndex index) {

        ConnectionController.getContentInTable(Objects.requireNonNull(dbModel.invisibleRootItem()).data().toString(), dbModel.data(index).toString());
    }

    // Закрыть список детей
    void collapse1(QModelIndex index) {
       // System.out.println(dbModel.data(index) + " Collapsed" );
    }

    // Открыть список детей
    void expanded1(QModelIndex index) throws SQLException {

        if (!(index.equals(Objects.requireNonNull(Objects.requireNonNull(dbModel.invisibleRootItem()).child(0)).index()))) {
            if (!(index.equals(Objects.requireNonNull(Objects.requireNonNull(dbModel.invisibleRootItem()).child(1)).index()))) {
                if (!(index.equals(Objects.requireNonNull(Objects.requireNonNull(dbModel.invisibleRootItem()).child(2)).index()))) {
                    loadContent(index);
                }
            }
        }

    }

    private void loadContent(QModelIndex index) {
        List<String> children = ConnectionController.getColumns((String) Objects.requireNonNull(dbModel.invisibleRootItem()).data(), dbModel.data(index).toString());//LocalStorage.getChildren(dbModel.data(index).toString());
        int i = 0;
        for (String child : children) {
            QStandardItem childItem = new QStandardItem(child);
            childItem.setEditable(false);
            Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(dbModel.invisibleRootItem()).child(0)).child(index.row())).setChild(i, childItem);
            i++;
        }
    }

}
