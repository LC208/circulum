package ru.lc208.circulum.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.lc208.circulum.entities.Competition;
import ru.lc208.circulum.util.WindowTools;

public class CompetitionController implements BaseController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;

    private final CRUD crudOperations;


    public CompetitionController() {
        crudOperations = new BaseCRUDBehavior(ConnectionController.getSession(), new Competition());
    }


    @Override
    public void get() {
        crudOperations.get();
    }

    @Override
    public void add() {
        crudOperations.setTextField("name",nameField);
        crudOperations.setTextField("description",descriptionField);
        crudOperations.add();
    }

    @Override
    public void update() {
        crudOperations.update();
    }

    @Override
    public void delete() {
        crudOperations.delete();
    }
}
