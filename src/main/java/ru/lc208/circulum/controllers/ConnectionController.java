package ru.lc208.circulum.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.lc208.circulum.gui.MainScene;

import static ru.lc208.circulum.util.WindowTools.showAlert;


public class ConnectionController {
//    @FXML private TextField hostField;
//    @FXML private TextField portField;
//    @FXML private TextField dbNameField;
    @FXML private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    @FXML
    public void onConnect() {
//        String host = hostField.getText();
//        String port = portField.getText();
//        String dbName = dbNameField.getText();
        String host = "127.0.0.1";
        String port = "5432";
        String dbName = "postgres";
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (host.isEmpty() || port.isEmpty() || dbName.isEmpty() || username.isEmpty()) {
            showAlert("Ошибка", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
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

            showAlert("Успех", "Подключение установлено", Alert.AlertType.INFORMATION);
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            MainScene main = new MainScene();
            main.show(currentStage);
        } catch (Exception e) {
            showAlert("Что-то пошло не так", "Ошибка: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    public void closeResources() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}