package com.example.priyalalstorejavacw;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;

public class MenuController {
    public AnchorPane menuAnchorPane;
    public Button menuCloseBtn;
    public BorderPane menuBorderPane;

    public void home(MouseEvent mouseEvent) {
        menuBorderPane.setCenter(menuAnchorPane);
    }

    public void products(MouseEvent mouseEvent) {
        loadPage("productTab");
    }

    public void category(MouseEvent mouseEvent) {
        loadPage("categoryTab");
    }

    public void closeMenu(ActionEvent event) {
        Stage currentStage = (Stage) menuCloseBtn.getScene().getWindow();
        currentStage.close();
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/loginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setScene(scene);
    }

    private void loadPage(String page){
        Parent root = null;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/" + page + ".fxml")));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        menuBorderPane.setCenter(root);
    }
}