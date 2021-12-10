package com.example.priyalalstorejavacw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/loginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1090, 668);
        stage.setTitle("Priyalal Store");
        stage.setScene(scene);
        stage.show();
    }
}
