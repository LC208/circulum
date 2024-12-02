package ru.lc208.circulum.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public interface CRUD {
    void get();

    void add();

    void update();

    void delete();

    void setTextField(String fieldName, TextField field);

}


