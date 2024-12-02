package ru.lc208.circulum.controllers;

import java.lang.reflect.Field;

public class EntityFieldUpdaterImpl implements EntityFieldUpdater{

    @Override
    public void setFieldValue(Object entity, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(entity, value);
    }

    @Override
    public Object getFieldValue(Object entity, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(entity);
    }
}
