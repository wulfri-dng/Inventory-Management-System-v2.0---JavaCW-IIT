package com.example.priyalalstorejavacw;
import com.example.priyalalstorejavacw.Category;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.*;
import org.bson.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CategoryTabController {
    public TableView<Category> categoryTable;
    public TableColumn<Category, Integer> categoryTableId;
    public TableColumn<Category, String> categoryTableName;
    public TableColumn<Category, String> categoryTableDescription;

    public void initialize() {
        ObservableList<Category> list = FXCollections.observableArrayList();

        MongoCollection<Document> categoryCollection = LoginController.database.getCollection(("categories"));
        FindIterable<Document> iterDoc = categoryCollection.find();
        for (Document doc: iterDoc) {
            int currentCategoryId = (Integer) doc.get("id");
            String currentCategoryName = (String) doc.get("name");
            String currentCategoryDescription = (String) doc.get("note");

            Category currentCategory = new Category(currentCategoryId, currentCategoryName, currentCategoryDescription);
            list.add(currentCategory);
        }

        categoryTableId.setCellValueFactory(new PropertyValueFactory<Category, Integer>("categoryId"));
        categoryTableName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        categoryTableDescription.setCellValueFactory(new PropertyValueFactory<Category, String>("note"));

        categoryTable.setItems(list);
    }
}
