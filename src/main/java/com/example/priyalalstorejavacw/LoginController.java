package com.example.priyalalstorejavacw;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    String defaultUsername = "Danusha";
    String defaultPassword = "12345";

    @FXML
    Label topLabel;

    @FXML
    TextField username;
    @FXML
    TextField password;

    @FXML
    public void logInPressed(ActionEvent event) throws IOException {
        if (username.getText().equals(defaultUsername) && password.getText().equals(defaultPassword)) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/menuWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setScene(scene);
        } else {
            topLabel.setText("Invalid username or password");
        }
    }

}