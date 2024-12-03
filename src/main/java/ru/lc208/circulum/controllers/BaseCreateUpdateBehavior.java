package ru.lc208.circulum.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.lc208.circulum.entities.Competition;
import ru.lc208.circulum.util.WindowTools;

import java.util.*;

public class BaseCreateUpdateBehavior implements CreateUpdate{

    Map<String, Object> fieldMap = new HashMap<>();
    Session session;
    EntityFieldUpdater updater = new EntityFieldUpdaterImpl();

    public BaseCreateUpdateBehavior(Session session)
    {
        this.session = session;
    }


    public void setField(String fieldName, Object field)
    {
        fieldMap.put(fieldName, field);
    }


    @Override
    public void add(Object entity) {
        if (fieldMap.values().stream().allMatch(Objects::nonNull)) {
            System.out.println("Ошибка: Все поля должны быть заполнены");
            return;
        }
        Transaction transaction;

        try {
            transaction = session.beginTransaction();

            fieldMap.forEach((key, value) -> {
                try {
                    updater.setFieldValue(entity, key, value);
                } catch (NoSuchFieldException e) {
                    WindowTools.showAlert("ERROR","Ошибка при добавлении объекта 1", Alert.AlertType.ERROR);

                } catch (IllegalAccessException e) {
                    WindowTools.showAlert("ERROR","Ошибка при добавлении объекта 2", Alert.AlertType.ERROR);
                }
            });
            session.persist(entity);
            transaction.commit();
            System.out.println("Объект добавлен");
        } catch (Exception e) {
            WindowTools.showAlert("ERROR",e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void update(Object entity) {

    }

}
