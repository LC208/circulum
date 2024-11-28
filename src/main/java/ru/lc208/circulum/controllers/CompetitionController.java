package ru.lc208.circulum.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CompetitionController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;

    private Session session;

    public CompetitionController() {
        session = ConnectionController.getSession();
    }


    @FXML
    public void addCompetition(ActionEvent event) {
        if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            System.out.println("Ошибка: Все поля должны быть заполнены");
            return;
        }
        String name = nameField.getText();
        String description = descriptionField.getText();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Competition competition = new Competition();
            competition.setName(name);
            competition.setDescription(description);

            session.save(competition);

            transaction.commit();
            System.out.println("Компетенция добавлена");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Ошибка при добавлении компетенции");
        }
    }
}
