package ru.lc208.circulum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CirculumApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("connection.fxml"));
        Parent p = loader.load();
        stage.setScene(new Scene(p));
        stage.setTitle("Вход");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}