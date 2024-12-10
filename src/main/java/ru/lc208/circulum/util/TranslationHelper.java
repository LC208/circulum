package ru.lc208.circulum.util;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.util.stream.Collectors;

public class TranslationHelper {

    public static void applyTranslations(Parent root) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof Parent) {
                applyTranslations((Parent) node); // Рекурсивно обрабатывать дочерние элементы
            }

            if (node instanceof Label label && label.getId() != null) {
                label.setText(Translation.translate(label.getId()));
            } else if (node instanceof Button button && button.getId() != null) {
                button.setText(Translation.translate(button.getId()));
            } else if (node instanceof TextField textField && textField.getId() != null) {
                textField.setPromptText(Translation.translate(textField.getId()));
            } else if (node instanceof TabPane tabPane) {
                for (Tab tab : tabPane.getTabs()) {
                    if (tab.getId() != null) {
                        tab.setText(Translation.translate(tab.getId()));
                    }
                }
            } else if (node instanceof TableView<?> tableView) {
                for (TableColumn<?, ?> column : tableView.getColumns()) {
                    if (column.getId() != null) {
                        column.setText(Translation.translate(column.getText()));
                    }
                }
            }

            if (node instanceof Control control) {
                ContextMenu contextMenu = control.getContextMenu();
                if (contextMenu != null) {
                    translateContextMenu(contextMenu);
                }
            }
//            else if (node instanceof ListView<?> listView && listView.getId() != null) {
//                translateListViewItems(listView);
//            }
        }
    }

    private static void translateContextMenu(ContextMenu contextMenu) {
        for (MenuItem item : contextMenu.getItems()) {
            if (item.getId() != null) {
                item.setText(Translation.translate(item.getId()));
            }
        }
    }


//    private static <T> void translateListViewItems(ListView<T> listView) {
//        var items = listView.getItems();
//        if (items != null && !items.isEmpty() && items.get(0) instanceof String) {
//            listView.setItems(FXCollections.observableArrayList(
//                    items.stream()
//                            .map(item -> Translation.translate(item.toString()))
//                            .collect(Collectors.toList())
//            ));
//        }
//    }
}
