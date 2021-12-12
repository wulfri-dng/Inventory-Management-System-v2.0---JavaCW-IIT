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
        String defaultUsername = null;
        String defaultPassword = null;
        FindIterable<Document> iterDoc = passwordCollection.find();
        for (Document doc: iterDoc) {
            defaultUsername = (String) doc.get("username");
            defaultPassword = (String) doc.get("password");
        }

        if (username.getText().equals(defaultUsername) && password.getText().equals(defaultPassword)) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/menuWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setScene(scene);
        } else {
            topLabel.setText("Invalid username or password");
        }
    }

//    VBox vbox = new VBox(new Text("Invalid Username or Password"), new Button("Ok"));
//            vbox.setAlignment(Pos.CENTER);
//            vbox.setPadding(new Insets(30));
////            vbox.setSpacing(new Insets(10));
//
//            dialogStage.setScene(new Scene(vbox));
//            dialogStage.show();
}