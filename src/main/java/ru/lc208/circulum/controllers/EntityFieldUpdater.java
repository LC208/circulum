package ru.lc208.circulum.controllers;

public interface EntityFieldUpdater {
    void setFieldValue(Object entity, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException;
    Object getFieldValue(Object entity, String fieldName) throws IllegalAccessException, NoSuchFieldException;
}
