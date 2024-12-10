package ru.lc208.circulum.gui;
import jakarta.persistence.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.TableViewSkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.lc208.circulum.controllers.ConnectionController;
import ru.lc208.circulum.entities.*;
import ru.lc208.circulum.util.TranslationHelper;
import ru.lc208.circulum.util.WindowTools;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.lc208.circulum.util.WindowTools.showAlert;

public class MainScene{

    private static Class<?>[] entityClasses = {Competition.class, Department.class, Direction.class, Faculty.class, Gear.class, Section.class, Speciality.class, StudyProgram.class, Subject.class, Teacher.class, Theme.class,StudyPlan.class, WorkType.class};

    private static Session session = null;
    private Stage formStage;

    private TabPane tabPane;
    private Map<Tab, Class> tabMap = new HashMap<>();
    private Map<Class, TableView> classMap = new HashMap<>();
    private boolean studyPlan = false;


    public MainScene()
    {
        session = ConnectionController.getSessionFactory().openSession();
        if(checkCurrentUser().contains("study_plan"))
        {
            entityClasses = new Class[]{StudyPlan.class, WorkType.class};
            studyPlan = true;
        }
    }

    private boolean isBlocked = false;


    private static List<String> checkCurrentUser()
    {
        Transaction transaction = session.beginTransaction();

        try {
            // SQL-запрос для получения родительской роли текущей роли
            List<String> parentRoles = session.createNativeQuery(
                    "SELECT r.rolname AS parent_role " +
                            "FROM pg_auth_members m " +
                            "JOIN pg_roles r ON m.roleid = r.oid " +
                            "WHERE m.member = (SELECT oid FROM pg_roles WHERE rolname = current_user)"
            ).getResultList();

            // Выводим родительские роли



            transaction.commit();
            return parentRoles;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            showAlert("Внимание", "Ошибка при проверке роли", Alert.AlertType.INFORMATION);
        }
        return null;
    }

    public void show(Stage currentStage)
    {

        TabPane tabPane = new TabPane();

        for (int i = 0; i < entityClasses.length; i++) {
            Tab tab = new Tab(entityClasses[i].getSimpleName());
            tab.setId(entityClasses[i].getSimpleName());// Имя вкладки из массива tabNames
            VBox tabContent = createTabContent(entityClasses[i]);

            tab.setContent(tabContent);  // Создаем контент вкладки
            tab.setClosable(false);
            tabMap.put(tab, entityClasses[i]);
            tabPane.getTabs().add(tab);
            TranslationHelper.applyTranslations(tabContent);
        }
//        refreshTable(entityClasses[0], classMap.get(entityClasses[0]));
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (_, _, t1) -> refreshTable(tabMap.get(t1), classMap.get(tabMap.get(t1)))
        );
        VBox main = new VBox(10, tabPane);
        TranslationHelper.applyTranslations(main);
        Scene scene = new Scene(main, 800, 600);
        currentStage.setScene(scene);
        currentStage.setTitle("Main");
        currentStage.show();
        currentStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                refreshTable(tabMap.get(tabPane.getSelectionModel().getSelectedItem()), classMap.get(tabMap.get(tabPane.getSelectionModel().getSelectedItem())));
            }
        });
    }


    private double getTextWidth(String text) {
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
        return tempText.getLayoutBounds().getWidth();
    }

    private <T>void resizeColumnsToFitContent(TableView<T> tableView) {
        for (TableColumn<T, ?> column : tableView.getColumns()) {
            column.setPrefWidth(0);
            for (T person : tableView.getItems()) {
                // Получаем значение для ячейки в столбце
                Object cellValue = column.getCellData(person);
                double width = getCellTextWidth(cellValue != null ? cellValue.toString() : "");
                if (width > column.getWidth()) {
                    column.setPrefWidth(width + 10); // +10 для добавления отступа
                }
            }

            double headerWidth = getTextWidth(column.getText());
            if (headerWidth > column.getPrefWidth()) {
                column.setMinWidth(headerWidth + 15);  // +10 для отступа
            }
        }
    }

    private double getCellTextWidth(String text) {
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
        return tempText.getLayoutBounds().getWidth();
    }

    private <T> VBox createTabContent(Class<T> clazz) {
        TableView<T> tableView = createTableView(clazz);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem addButton = new MenuItem("Add");
        MenuItem deleteButton = new MenuItem("Delete");
        MenuItem editButton = new MenuItem("Edit");

        addButton.setId("add");
        deleteButton.setId("delete");
        editButton.setId("edit");

        ContextMenu contextMenu = new ContextMenu(addButton,deleteButton,editButton);

        addButton.setOnAction(event -> {
            if(!isBlocked) showInputForm(clazz);
        });

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
                showEditForm(clazz, entityToEdit);
            }
        });

        tableView.setContextMenu(contextMenu);
        classMap.put(clazz, tableView);
        return new VBox(10,tableView);
    }


    private <T> void refreshTable(Class<T> clazz, TableView<T> tableView)
    {
        session.beginTransaction();
        session.clear();
        session.getTransaction().commit();
        ObservableList<T> data = loadData(clazz);
        tableView.getItems().clear();
        tableView.setItems(data);
        resizeColumnsToFitContent(tableView);
    }

    private static <T> void refreshList(Class<T> clazz, ListView<T> listView)
    {
        session.beginTransaction();
        session.clear();
        session.getTransaction().commit();
        ObservableList<T> data = loadData(clazz);
        listView.getItems().clear();
        listView.setItems(data);
    }

    private static boolean isEntity(Class<?> clazz) {
        return clazz.isAnnotationPresent(Entity.class); // Проверяем, является ли класс сущностью
    }

    private <T> TableView<T> createTableView(Class<T> clazz) {
        TableView<T> tableView = new TableView<>();
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        for (var field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            TableColumn<T, ?> column = createColumn(field);
            if (column != null) {
                tableView.getColumns().add(column);
            }
        }
//        ObservableList<T> data = loadData(clazz);
//        tableView.setItems(data);

        return tableView;
    }

    public static Object getId(Object entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Class<?> clazz = entity.getClass();
        try {
            // Пытаемся найти поле id
            Field idField = null;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase("id")) {
                    idField = field;
                    break;
                }
            }

            if (idField == null) {
                throw new IllegalStateException("No field named 'id' found in " + clazz.getName());
            }

            idField.setAccessible(true); // Делаем поле доступным
            return idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access id field in " + clazz.getName(), e);
        }
    }

    private <T> TableColumn<T, ?> createColumn(Field field) {
        TableColumn<T, ?> column = new TableColumn<>(field.getName());
        column.setId(field.getName());
        if (field.getType() == Integer.class || field.getType() == int.class) {
            column.setCellValueFactory(cellData -> ((ObservableValue)new SimpleIntegerProperty((Integer) getFieldValue(cellData.getValue(), field)).asObject()));
        } else if (field.getType() == String.class) {
            column.setCellValueFactory(cellData -> ((ObservableValue)new SimpleStringProperty((String) getFieldValue(cellData.getValue(), field))));
        } else if(field.getType().isAssignableFrom(Set.class))
        {
            column.setCellValueFactory(cellData -> ((ObservableValue)new SimpleStringProperty((String) ((Set)getFieldValue(cellData.getValue(), field)).stream().map(Object::toString).collect(Collectors.joining(", ")))));
        }else if(isEntity(field.getType())){
            column.setCellValueFactory(cellData -> {
                Object fieldValue = getFieldValue(cellData.getValue(), field);
                return (ObservableValue)new SimpleStringProperty(Objects.requireNonNull(fieldValue).toString());
            });
        }

        return column;
    }

    private <T> Object getFieldValue(T entity, Field field) {
        try {
            field.setAccessible(true);
//            if(classMap.size() == 4)
//            {
//                refreshTable(field.getType(), classMap.get(field.getType()));
//            }
//            Class entClazz;
//            if(Set.class.isAssignableFrom(field.getType()))
//            {
//                entClazz = getElementType(field);
//                loadData(entClazz);
//            }else if(isEntity(field.getType()))
//            {
//                entClazz = field.getType();
//                loadData(entClazz);
//            }

            return field.get(entity);
        } catch (IllegalAccessException e) {
            showAlert("Внимание", "Ошибка при получении значений таблицы", Alert.AlertType.INFORMATION);
            return null;
        }
    }

    private static <T> ObservableList<T> loadData(Class<T> clazz) {
        List<T> entityList = null;
        try {
            session.beginTransaction();
            entityList = session.createQuery("from " + clazz.getName(), clazz).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            showAlert("Внимание", "Ошибка при загрузке данных", Alert.AlertType.INFORMATION);
        }
        assert entityList != null;
        return FXCollections.observableArrayList(entityList);
    }

    private static <T> StringBuilder getStringBuilder(Class<T> clazz) {
        StringBuilder queryBuilder = new StringBuilder("FROM " + clazz.getSimpleName());

        // Получаем все поля класса
        Field[] fields = clazz.getDeclaredFields();

        // Обрабатываем поля для поиска связей
        for (Field field : fields) {
            // Проверяем, является ли поле связью с другой сущностью
            if (field.isAnnotationPresent(ManyToOne.class) ||
                    field.isAnnotationPresent(OneToMany.class) ||
                    field.isAnnotationPresent(ManyToMany.class)) {

                // Добавляем LEFT JOIN FETCH для этой связи
                queryBuilder.append("LEFT JOIN FETCH ").append(field.getName());
            }
        }
        return queryBuilder;
    }

    private <T> void showInputForm(Class<T> clazz)
    {
        TableView tableView = classMap.get(clazz);
        formStage = new Stage();
        formStage.setTitle("Create " + clazz.getSimpleName());


        VBox inputFields = new VBox(10);

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }

            Label label = new Label(field.getName());
            Control inputControl = createInputControl(field);
            if(inputControl instanceof ListView<?>)
            {
                Class entClazz;
                if(Set.class.isAssignableFrom(field.getType()))
                {
                    entClazz = getElementType(field);
                }else if(isEntity(field.getType()))
                {
                    entClazz = field.getType();
                } else {
                    entClazz = null;
                }
                MenuItem addButton = new MenuItem("Добавить");
                MenuItem editButton = new MenuItem("Изменить");

                ContextMenu contextMenu = new ContextMenu(addButton,editButton);
                addButton.setOnAction(event -> {
                    formStage.close();
                    showInputForm(entClazz);
                    formStage.setOnCloseRequest(e -> {
                        isBlocked = false;
                        showInputForm(clazz);
                    });
                });

                editButton.setOnAction(event -> {
                    ObservableList<T> selectedItems = ((ListView) inputControl).getSelectionModel().getSelectedItems();
                    T entityToEdit;
                    if (selectedItems.size() == 1) {
                        entityToEdit = selectedItems.getFirst();
                    } else {
                        WindowTools.showAlert("Уведомление", "Выберите только одну сущность", Alert.AlertType.INFORMATION);
                        return;
                    }
                    formStage.close();
                    showEditForm(entClazz, entityToEdit);
                    formStage.setOnCloseRequest(e -> {
                        isBlocked = false;
                        showInputForm(clazz);
                    });
                });

                if(!studyPlan){
                    inputControl.setContextMenu(contextMenu);
                }
            }
            inputFields.getChildren().addAll(label, inputControl);
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            T entity = createEntityFromInputs(clazz, inputFields);
            if (entity != null) {
                saveRecord(entity);
                refreshTable(clazz,tableView);
                formStage.close();
//                formStage.getOnCloseRequest().handle(new WindowEvent(formStage, Event.ANY));
                isBlocked = false;
            }
        });

        HBox buttons = new HBox(saveButton);
        buttons.setAlignment(Pos.CENTER);
        VBox content = new VBox(inputFields, buttons);
        content.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
//        formStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                // Действия при возвращении на сцену
//                System.out.println("Scene returned to focus.");
//            }
//        });
        formStage.setScene(scene);
        formStage.show();
        isBlocked = true;
        formStage.setOnCloseRequest(windowEvent -> isBlocked = false );
    }



    private <T> void showEditForm(Class<T> clazz, T entityToEdit) {
        TableView<T> tableView = classMap.get(clazz);
        formStage = new Stage();
        formStage.setTitle("Edit " + clazz.getSimpleName());

        int entityId;
        int index = tableView.getItems().indexOf(entityToEdit);

        // Список для хранения компонентов ввода
        VBox inputFields = new VBox(10);

        // Получаем все поля класса с помощью рефлексии
        Field[] fields = clazz.getDeclaredFields();

        // Создаем элементы UI для каждого поля
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                try {
                    entityId = (int) field.get(entityToEdit);
                } catch (IllegalAccessException _) {
                }
                continue;
            }

            Label label = new Label(field.getName());

            // Создаем элементы ввода на основе типа поля
            Control inputControl = createInputControl(field);
            if(inputControl instanceof ListView<?>)
            {
                Class entClazz;
                if(Set.class.isAssignableFrom(field.getType()))
                {
                    entClazz = getElementType(field);
                }else if(isEntity(field.getType()))
                {
                    entClazz = field.getType();
                } else {
                    entClazz = null;
                }
                MenuItem addButton = new MenuItem("Добавить");
                MenuItem editButton = new MenuItem("Изменить");

                ContextMenu contextMenu = new ContextMenu(addButton,editButton);
                addButton.setOnAction(event -> {
                    formStage.close();
                    showInputForm(entClazz);
                    formStage.setOnCloseRequest(e -> {
                        isBlocked = false;
                        showEditForm(clazz, entityToEdit);
                    });
                });

                editButton.setOnAction(event -> {
                    ObservableList<T> selectedItems = ((ListView) inputControl).getSelectionModel().getSelectedItems();
                    T entityToEd;
                    if (selectedItems.size() == 1) {
                        entityToEd = selectedItems.getFirst();
                    } else {
                        WindowTools.showAlert("Уведомление", "Выберите только одну сущность", Alert.AlertType.INFORMATION);
                        return;
                    }
                    formStage.close();
                    showEditForm(entClazz, entityToEd);
                    formStage.setOnCloseRequest(e -> {
                        isBlocked = false;
//                        session.beginTransaction();
//                        session.update(entityToEdit);
//                        session.getTransaction().commit();
                        showEditForm(clazz, entityToEdit);
                    });
                });

                if(!studyPlan){
                    inputControl.setContextMenu(contextMenu);
                }
            }
            // Если сущность редактируется, заполняем поля значениями из entityToEdit
//            refreshList();
            if (entityToEdit != null) {
                fillFieldWithExistingData(field, entityToEdit, inputControl);
            }

            inputFields.getChildren().addAll(label, inputControl);
        }

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(event -> {

            T entity = updateEntityFromInputs(entityToEdit, inputFields);
            if (entity != null) {
                updateRecord(entity);
                refreshTable(clazz,tableView);
                formStage.close();
//                formStage.getOnCloseRequest().handle(new WindowEvent(formStage, Event.ANY));
                isBlocked = false;
            }
        });

        HBox buttons = new HBox(saveButton);
        buttons.setAlignment(Pos.CENTER);
        VBox content = new VBox(inputFields, buttons);
        content.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
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
            }
            else if (field.getType().isAssignableFrom(Set.class)) {
                if (inputControl instanceof ListView) {
                    ListView<Object> listView = (ListView<Object>) inputControl;
                    listView.getItems().stream().filter(n-> ((Set)value).stream().anyMatch(n::equals)).forEach(l -> listView.getSelectionModel().select(l));
                }
            }
            else if(isEntity(field.getType()))
            {
                if (inputControl instanceof ListView) {
//                    refreshList(field.getType(),listView);
                    int index = 0;
                    for(Object obj : ((ListView<Object>) inputControl).getItems())
                    {
                        if(obj == value)
                        {
                            ((ListView<Object>) inputControl).getSelectionModel().select(index);
                        }
                        index++;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            showAlert("Внимание", "Проблема при заполнении полей", Alert.AlertType.INFORMATION);
        }
    }


    private static <T> T updateEntityFromInputs(T entityToEdit, VBox inputFields) {
        try {
            Field[] fields = entityToEdit.getClass().getDeclaredFields();

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
                    field.set(entityToEdit,new HashSet<>(selectedItems));
                } else if (field.getType() == String.class) {
                    field.set(entityToEdit, ((TextField) inputControl).getText());
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(entityToEdit, Integer.parseInt(((TextField) inputControl).getText()));
                }else if(isEntity(field.getType()))
                {
                    ListView<?> listView = (ListView<?>) inputControl;
                    Object selectedItem = listView.getSelectionModel().getSelectedItem();
                    field.set(entityToEdit,selectedItem);
                }
                index++;
            }

            return entityToEdit;
        }catch (NumberFormatException e) {
            showAlert("Внимание", "В поле нужно ввести числовое значение", Alert.AlertType.INFORMATION);
            return null;
        }
        catch (Exception e) {
            showAlert("Внимание", "Ошибка перед обновлением записи", Alert.AlertType.INFORMATION);
            return null;
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
                }else if(isEntity(field.getType()))
                {
                    ListView<?> listView = (ListView<?>) inputControl;
                    Object selectedItems = listView.getSelectionModel().getSelectedItem();
                    field.set(entity,selectedItems);
                }
                index++;
            }

            return entity;
        } catch (Exception e) {
            showAlert("Внимание", "Ошибка перед созданием записи", Alert.AlertType.INFORMATION);
            return null;
        }
    }

    private Control createInputControl(Field field) {
        if (Set.class.isAssignableFrom(field.getType())) {
            return createListViewForSet(field);
        }
        else if (field.getType() == String.class) {
            return new TextField();
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            return new TextField();
        }else if (isEntity(field.getType()))
        {
            return createListViewForEntity(field);
        }
        else {
            return new TextField();
        }
    }


    private <T> ListView<?> createListViewForEntity(Field field) {
        Class elementType = field.getType();

        ListView listView = new ListView<>();
//        TableView tableView = classMap.get(elementType);

        List items = loadData(elementType);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(items != null)
        {
            listView.getItems().addAll(items);
        }

        return listView;
    }


    private <T> ListView<?> createListViewForSet(Field field) {
        // Получаем тип элемента коллекции (например, Theme из Set<Theme>)
        Class elementType = getElementType(field);
        // Создаем ListView для связанной сущности (например, Theme или другая сущность)
        ListView listView = new ListView<>();

        // Загружаем все объекты сущности из базы данных в зависимости от типа
        List items = loadData(elementType);

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

    private static Class<?> getElementType(Field field) {
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
        refreshTable(clazz,tableView);
    }


    // Сохранение записи в базе данных
    private <T> void saveRecord(T record) {
        try {
            session.beginTransaction();
            session.save(record);
            session.getTransaction().commit();
        } catch (Exception e) {
            showAlert("Внимание", "Не удалось сохранить запись", Alert.AlertType.INFORMATION);
        }

    }

    private <T> void deleteRecordFromDB(T record) {
        try {
            session.beginTransaction();
            session.remove(record);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            showAlert("Внимание", "Не удалось удалить запись", Alert.AlertType.INFORMATION);
        }

    }

    private <T> void updateRecord(T record) {
        try {
            session.beginTransaction();
            session.merge(record);
            session.getTransaction().commit();
        } catch (Exception e) {
            showAlert("Внимание", "Не удалось обновить запись", Alert.AlertType.INFORMATION);
        }

    }

}
