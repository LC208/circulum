package ru.lc208.circulum.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public interface CreateUpdate {

    void add(Object entity);

    void update(Object entity);

    void setField(String fieldName, Object value);

}


