package ru.lc208.circulum.gui;
import jakarta.persistence.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.stat.SessionStatistics;
import ru.lc208.circulum.controllers.ConnectionController;
import ru.lc208.circulum.controllers.CreateUpdate;
import ru.lc208.circulum.entities.Competition;
import ru.lc208.circulum.entities.StudyProgram;
import ru.lc208.circulum.util.WindowTools;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MainScene{

    private final Class<?>[] entityClasses = {StudyProgram.class, Competition.class};

    private final String[] tabNames = {"StudyProgram", "Competition"};

    private Session session;
    private CreateUpdate behavior;


    public MainScene()
    {
        this.session = ConnectionController.getSessionFactory().openSession();
    }

    private boolean isBlocked = false;

    public void show(Stage currentStage)
    {
        TabPane tabPane = new TabPane();

        for (int i = 0; i < entityClasses.length; i++) {
            Tab tab = new Tab(tabNames[i]);  // Имя вкладки из массива tabNames
            tab.setContent(createTabContent(entityClasses[i]));  // Создаем контент вкладки
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }

        Scene scene = new Scene(tabPane, 800, 600);
        currentStage.setScene(scene);
        currentStage.setTitle("Main");
        currentStage.show();
    }

    private <T> VBox createTabContent(Class<T> clazz) {
        TableView<T> tableView = createTableView(clazz);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button editButton = new Button("Edit");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(event -> {
            if(!isBlocked) showInputForm(clazz, tableView);
        });

        refreshButton.setOnAction(event -> refreshTable(clazz,tableView));

        deleteButton.setOnAction(event -> {
            try {
                deleteRecord(clazz, tableView);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        editButton.setOnAction(event -> {
            if(!isBlocked){
                ObservableList<T> selectedItems = tableView.getSelectionModel().getSelectedItems();
                T entityToEdit;
                if (selectedItems.size() == 1) {
                    entityToEdit = selectedItems.getFirst();
                } else {
                    WindowTools.showAlert("Уведомление", "Выберите только одну сущность", Alert.AlertType.INFORMATION);
                    return;
                }
                showEditForm(clazz, entityToEdit, tableView);
            }
        });

        HBox hbox = new HBox(10, addButton, deleteButton, editButton, refreshButton);
        hbox.setAlignment(Pos.CENTER);
        return new VBox(10,tableView , hbox);
    }


    private <T> void refreshTable(Class<T> clazz, TableView<T> tableView)
    {
        ObservableList<T> data = loadData(clazz);
        tableView.setItems(data);
    }

    private static boolean isEntity(Class<?> clazz) {
        return clazz.isAnnotationPresent(Entity.class); // Проверяем, является ли класс сущностью
    }

    private <T> TableView<T> createTableView(Class<T> clazz) {
        TableView<T> tableView = new TableView<>();

        for (var field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            TableColumn<T, ?> column = createColumn(field);
            if (column != null) {
                tableView.getColumns().add(column);
            }
        }

        ObservableList<T> data = loadData(clazz);
        tableView.setItems(data);

        return tableView;
    }

    private <T> TableColumn<T, ?> createColumn(java.lang.reflect.Field field) {
        TableColumn<T, ?> column = new TableColumn<>(field.getName());

        if (field.getType() == Integer.class || field.getType() == int.class) {
            column.setCellValueFactory(cellData -> ((ObservableValue)new SimpleIntegerProperty((Integer) getFieldValue(cellData.getValue(), field)).asObject()));
        } else if (field.getType() == String.class) {
            column.setCellValueFactory(cellData -> ((ObservableValue)new SimpleStringProperty((String) getFieldValue(cellData.getValue(), field))));
        } else if(isEntity(field.getType())){
            System.out.println();
            column.setCellValueFactory(cellData -> {
                Object fieldValue = getFieldValue(cellData.getValue(), field);

                if (fieldValue instanceof HibernateProxy) {
                    Hibernate.initialize(fieldValue);
                }
                return (ObservableValue)new SimpleStringProperty(Objects.requireNonNull(fieldValue).toString());
            });
        } else if(field.getType().isAssignableFrom(Set.class))
        {
            column.setCellValueFactory(cellData -> ((ObservableValue)new SimpleStringProperty((String) ((Set)getFieldValue(cellData.getValue(), field)).stream().map(Object::toString).collect(Collectors.joining(", ")))));
        }

        return column;
    }

    private <T> Object getFieldValue(T entity, java.lang.reflect.Field field) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> ObservableList<T> loadData(Class<T> clazz) {
        List<T> entityList = null;
        try {
            session.beginTransaction();
            entityList = session.createQuery("from " + clazz.getName(), clazz).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert entityList != null;
        return FXCollections.observableArrayList(entityList);
    }

    private <T> void showInputForm(Class<T> clazz, TableView<T> tableView)
    {
        Stage formStage = new Stage();
        formStage.setTitle("Create" + clazz.getSimpleName());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        VBox inputFields = new VBox(10);

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }

            Label label = new Label(field.getName());
            Control inputControl = createInputControl(field);
            inputFields.getChildren().addAll(label, inputControl);
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            T entity = createEntityFromInputs(clazz, inputFields);
            if (entity != null) {
                saveRecord(entity);
                tableView.setItems(loadData(clazz));
                formStage.close();
                isBlocked = false;
            }
        });

        vbox.getChildren().addAll(inputFields, saveButton);

        Scene scene = new Scene(vbox, 400, 400);
        formStage.setScene(scene);
        formStage.show();
        isBlocked = true;
        formStage.setOnCloseRequest(windowEvent -> isBlocked = false );
    }


    private <T> void showEditForm(Class<T> clazz, T entityToEdit, TableView<T> tableView) {

        Stage formStage = new Stage();
        formStage.setTitle("Edit " + clazz.getSimpleName());



        VBox vbox = new VBox(10);  // VBox для размещения всех полей
        vbox.setPadding(new Insets(20));

        // Список для хранения компонентов ввода
        VBox inputFields = new VBox(10);

        // Получаем все поля класса с помощью рефлексии
        Field[] fields = clazz.getDeclaredFields();

        // Создаем элементы UI для каждого поля
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }

            Label label = new Label(field.getName());

            // Создаем элементы ввода на основе типа поля
            Control inputControl = createInputControl(field);

            // Если сущность редактируется, заполняем поля значениями из entityToEdit
            if (entityToEdit != null) {
                fillFieldWithExistingData(field, entityToEdit, inputControl);
            }

            inputFields.getChildren().addAll(label, inputControl);
        }

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(event -> {
            T entity = createEntityFromInputs(clazz, inputFields);
            if (entity != null) {
                updateRecord(entity);
                tableView.setItems(loadData(clazz));
                formStage.close();
                isBlocked = false;
            }
        });

        vbox.getChildren().addAll(inputFields, saveButton);

        Scene scene = new Scene(vbox, 400, 400);
        formStage.setScene(scene);
        formStage.show();
        isBlocked = true;
        formStage.setOnCloseRequest(windowEvent -> isBlocked = false );
    }

    private static <T> void fillFieldWithExistingData(Field field, T entityToEdit, Control inputControl) {
        try {
            Object value = field.get(entityToEdit);

            if (field.getType() == String.class) {
                ((TextField) inputControl).setText((String) value);
            } else if (field.getType() == int.class || field.getType() == Integer.class) {
                ((TextField) inputControl).setText(String.valueOf(value));
            }else if (field.getType().isAssignableFrom(Set.class)) {
                if (inputControl instanceof ListView) {
                    ListView<Object> listView = (ListView<Object>) inputControl;
                    listView.getItems().setAll((Set<?>) value); // Заполняем ListView элементами из Set
                }
            }else if(isEntity(field.getType()))
            {
                ((ComboBox<Object>) inputControl).setValue(value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static <T> T createEntityFromInputs(Class<T> clazz, VBox inputFields) {
        try {
            T entity = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            int index = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    continue;
                }
                Control inputControl = (Control) inputFields.getChildren().get(index * 2 + 1);

                if (Set.class.isAssignableFrom(field.getType())) {
                    ListView<?> listView = (ListView<?>) inputControl;
                    ObservableList selectedItems = listView.getSelectionModel().getSelectedItems();
                    field.set(entity,new HashSet<>(selectedItems));
                } else if (field.getType() == String.class) {
                    field.set(entity, ((TextField) inputControl).getText());
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(entity, Integer.parseInt(((TextField) inputControl).getText()));
                }else if(isEntity(field.getType()) && inputControl instanceof ComboBox)
                {
                    field.set(entity, ((ComboBox<Object>) inputControl).getValue());
                }
                index++;
            }

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Control createInputControl(Field field) {
        // В зависимости от типа поля, создаем соответствующий элемент UI
        if (Set.class.isAssignableFrom(field.getType())) {
            return createListViewForSet(field);  // Для Set или других коллекций используем ComboBox
        }
        else if (field.getType() == String.class) {
            return new TextField();
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            return new TextField();  // Можно заменить на Spinner для чисел
        }else if (isEntity(field.getType()))
        {
            ComboBox<Object> comboBox = new ComboBox<>();
            // Загрузить данные из базы
            comboBox.getItems().addAll(loadData(field.getType()));
            return comboBox;
        }
        else {
            return new TextField();  // По умолчанию используем TextField
        }
    }

    private ListView<?> createListViewForSet(Field field) {
        // Получаем тип элемента коллекции (например, Theme из Set<Theme>)
        Class<?> elementType = getElementType(field);

        // Создаем ListView для связанной сущности (например, Theme или другая сущность)
        ListView<Object> listView = new ListView<>();

        // Загружаем все объекты сущности из базы данных в зависимости от типа
        List<?> items = loadData(elementType);

        // Устанавливаем ListView для множественного выбора
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Добавляем все элементы в ListView
        listView.getItems().addAll(items);

        return listView;
    }


    private ComboBox<?> createComboBoxForSet(Field field) {
        // Получаем тип элемента коллекции (например, Theme из Set<Theme>)
        Class<?> elementType = getElementType(field);

        // Создаем ComboBox для связанной сущности (например, Theme или другая сущность)
        ComboBox<Object> comboBox = new ComboBox<>();

        // Загружаем все объекты сущности из базы данных в зависимости от типа
        List<?> items = loadData(elementType);
        comboBox.getItems().addAll(items);

        return comboBox;
    }

    private Class<?> getElementType(Field field) {
        Type fieldType = field.getGenericType();

        if (fieldType instanceof ParameterizedType paramType) {
            Type[] typeArgs = paramType.getActualTypeArguments();

            if (typeArgs.length > 0) {
                Type elementType = typeArgs[0];
                if (elementType instanceof Class<?>) {
                    return (Class<?>) elementType;
                }
            }
        }

        return null;
    }


    // Метод для удаления записи
    private <T> void deleteRecord(Class<T> clazz, TableView<T> tableView) throws InterruptedException {
        // Получаем все выбранные элементы
        ObservableList<T> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if (!selectedItems.isEmpty()) {
            // Удаляем все выбранные записи из базы данных
            for (T item : selectedItems) {
                deleteRecordFromDB(item);  // Удаляем запись из БД
            }
            // Удаляем их из таблицы
//            tableView.getItems().removeAll(selectedItems);
        }
        tableView.setItems(loadData(clazz));
    }

//    // Метод для редактирования записей
//    private <T> void editRecord(Class<T> clazz, TableView<T> tableView) {
//        // Получаем все выбранные элементы
//        ObservableList<T> selectedItems = tableView.getSelectionModel().getSelectedItems();
//
//        if (!selectedItems.isEmpty()) {
//            // Пример изменения для каждой выбранной записи
//            for (T selectedItem : selectedItems) {
////                if (clazz == Competition.class) {
////                    Person person = (Person) selectedItem;
////                    person.setName("Edited Person");
////                }
//            }
//
//            // Обновляем все измененные записи в базе данных
//            for (T selectedItem : selectedItems) {
//                updateRecord(selectedItem);
//            }
//
//            // Обновляем таблицу
//            tableView.refresh();
//        }
//    }

    // Сохранение записи в базе данных
    private <T> void saveRecord(T record) {
        try {
            session.beginTransaction();
            session.save(record);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    // Удаление записи из базы данных
    private <T> void deleteRecordFromDB(T record) {
        
        SessionStatistics stats = session.getStatistics();

        // Включаем сбор статистики
        try {
            session.beginTransaction();
//            session.lock(record, LockMode.PESSIMISTIC_WRITE);
//            System.out.println(record);
//            Object obj = session.get(record.getClass(), Integer.parseInt(record.toString()));
//            session.flush();
//            session.clear();
            session.remove(record);
            session.getTransaction().commit();
        } catch (OptimisticLockException e) {
            System.err.println("Optimistic lock exception: " + e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        
    }

    // Обновление записи в базе данных
    private <T> void updateRecord(T record) {
        
        try {
            session.beginTransaction();
            session.update(record);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
