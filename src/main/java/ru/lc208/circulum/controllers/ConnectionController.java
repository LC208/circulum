package ru.lc208.circulum.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static ru.lc208.circulum.util.WindowTools.showAlert;


public class ConnectionController {
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField dbNameField;
    @FXML private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private SessionFactory sessionFactory;
    private static Session session;

    public static Session getSession()
    {
        return session;
    }

    @FXML
    public void onConnect() {
        String host = hostField.getText();
        String port = portField.getText();
        String dbName = dbNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (host.isEmpty() || port.isEmpty() || dbName.isEmpty() || username.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Создание Hibernate SessionFactory
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml") // Загружает конфигурацию из hibernate.cfg.xml
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://" + host + ":" + port + "/" + dbName)
                    .setProperty("hibernate.connection.username", username)
                    .setProperty("hibernate.connection.password", password)
                    .buildSessionFactory();

            session = sessionFactory.openSession();
            showAlert("Success", "Connection successful!", Alert.AlertType.INFORMATION);
            Stage currentStage = (Stage) hostField.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/lc208/circulum/competition.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loader.load()));
            newStage.setTitle("New Window");
            newStage.show();

        } catch (Exception e) {
            showAlert("Something Went Wrong", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void closeResources() {
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}