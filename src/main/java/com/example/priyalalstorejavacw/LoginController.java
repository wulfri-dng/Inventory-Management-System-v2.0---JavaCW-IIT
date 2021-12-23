package com.example.priyalalstorejavacw;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.text.Font;

import java.io.IOException;

public class LoginController {
    private static final MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static final MongoDatabase database = mongoClient.getDatabase("Priyalal_Store");
    private static final MongoCollection<Document> passwordCollection = database.getCollection("password");
    public static MongoCollection<Document> categoryCollection = database.getCollection("categories");
    public static MongoCollection<Document> productCollection = database.getCollection("products");

    @FXML
    Label topLabel;
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    @FXML

    public void logInPressed(ActionEvent event) throws IOException {
        String username = null;
        String password = null;
        FindIterable<Document> iterDoc = passwordCollection.find();
        for (Document doc : iterDoc) {
            username = (String) doc.get("username");
            password = (String) doc.get("password");
        }

        if (this.username.getText().equals(username) && this.password.getText().equals(password)) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/menuWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setScene(scene);
        } else {
            topLabel.setText("Invalid username or password");
        }
    }
}