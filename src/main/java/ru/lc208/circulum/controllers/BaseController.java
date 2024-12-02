package ru.lc208.circulum.controllers;

import javafx.fxml.FXML;

public interface BaseController {

    @FXML
    void get();

    @FXML
    void add();

    @FXML
    void update();

    @FXML
    void delete();
}
