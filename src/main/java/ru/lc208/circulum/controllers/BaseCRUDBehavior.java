package ru.lc208.circulum.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.lc208.circulum.entities.Competition;
import ru.lc208.circulum.util.WindowTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseCRUDBehavior implements CRUD{

    Map<String, TextField> fieldMap = new HashMap<>();
    Session session;
    EntityFieldUpdater updater = new EntityFieldUpdaterImpl();
    Object entity;

    public BaseCRUDBehavior(Session session, Object entity)
    {
        this.session = session;
        this.entity = entity;
    }


    public void setTextField(String fieldName, TextField field)
    {
        fieldMap.put(fieldName, field);
    }

    @Override
    public void get() {

    }

    @Override
    public void add() {
        if (fieldMap.values().stream().allMatch(value -> value.getText().isEmpty())) {
            System.out.println("Ошибка: Все поля должны быть заполнены");
            return;
        }

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            fieldMap.forEach((key, value) -> {
                try {
                    updater.setFieldValue(entity, key, value.getText());
                } catch (NoSuchFieldException e) {
                    WindowTools.showAlert("ERROR","Ошибка при добавлении объекта 1", Alert.AlertType.ERROR);

                } catch (IllegalAccessException e) {
                    WindowTools.showAlert("ERROR","Ошибка при добавлении объекта 2", Alert.AlertType.ERROR);
                }
            });
            session.save(entity);
            transaction.commit();
            System.out.println("Объект добавлен");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            WindowTools.showAlert("ERROR","Ошибка при добавлении объекта", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
