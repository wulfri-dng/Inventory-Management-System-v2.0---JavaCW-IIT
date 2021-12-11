package com.example.priyalalstorejavacw;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.util.ArrayList;

public class ProductTabController {
    public TableView<Product> productTable;
    public TableColumn<Product, Integer> productTableId;
    public TableColumn<Product, String> productTableName;
    public TableColumn<Product, String> productTableCategory;

    public TextField editProductSearchTxtBox;
    public Button productDeleteBtn;
    public Label productNotificationLabel;
    public Label showProductName;
    public Label showProductId;
    public Label showProductCategory;
    public TextField editProductNewProductName;
    public TextField addProductName;
    public ChoiceBox<String> editProductChoiceBox;
    public ChoiceBox<String> addProductChoiceBox;

    FindIterable<Document> productIterDoc = LoginController.productCollection.find();
    FindIterable<Document> categoryIterDoc = LoginController.categoryCollection.find();

    int searchedProductID;

    public void initialize() {
        updateProductTable(true);
    }

    public void updateProductTable(boolean updateChoiceBox) {
        ObservableList<Product> productList = FXCollections.observableArrayList();

        for (Document doc: productIterDoc) {
            int currentProductId = (Integer) doc.get("id");
            String currentProductName = (String) doc.get("name");
            String currentProductCategory = (String) doc.get("categoryName");
            Product currentProduct = new Product(currentProductId, currentProductName, currentProductCategory);
            productList.add(currentProduct);
        }

        productTableId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productTableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productTableCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));

        // UPDATE CHOICE BOX
        if (updateChoiceBox) {
            for (Document doc: categoryIterDoc) {
                String currentCategory = (String) doc.get("name");
                addProductChoiceBox.getItems().add(currentCategory);
                editProductChoiceBox.getItems().add(currentCategory);
            }
        }
        productTable.setItems(productList);
    }

    public int generateProductID() {
        ArrayList<Integer> productIdList = new ArrayList<Integer>();
        for (Document doc: productIterDoc) {
            int currentCategoryId = (Integer) doc.get("id");
            productIdList.add(currentCategoryId);
        }
        if (productIdList.size() == 0){
            return 10000;
        } else {
            return productIdList.get(productIdList.size()-1) + 1;
        }
    }

    public void addProduct(ActionEvent actionEvent) {
        Document productDocument = new Document();

        if (!(addProductName.getText().equals("")) && !addProductChoiceBox.getValue().equals("")) {
            productDocument.put("name", addProductName.getText());
            productDocument.put("categoryName", addProductChoiceBox.getValue());
            productDocument.put("id", generateProductID());
            LoginController.productCollection.insertOne(productDocument);
        }
        addProductName.setText("");
        updateProductTable(false);
    }

    public void searchFromProduct() {
        boolean isFound = false;
        for (Document doc: productIterDoc) {
            int currentProductId = (Integer) doc.get("id");
            String currentProductName = (String) doc.get("name");
            String currentProductCategory = (String) doc.get("categoryName");
            if (editProductSearchTxtBox.getText().equals("" + currentProductId) || editProductSearchTxtBox.getText().equals(currentProductName)) {
                isFound = true;
                searchedProductID = currentProductId;

                productNotificationLabel.setText("Product found");
                showProductId.setText("" + currentProductId);
                showProductName.setText(currentProductName);
                showProductCategory.setText(currentProductCategory);

                editProductNewProductName.setText(currentProductName);
//                editProductChoiceBox.getItems().add(currentProductCategory);

                productDeleteBtn.setDisable(false);
            }
        }
        if (!isFound) {
            productNotificationLabel.setText("Category not found. Invalid ID or Name ");
            showProductId.setText("");
            showProductName.setText("");
            showProductCategory.setText("");

            editProductNewProductName.setText("");
//            editProductChoiceBox.setText("");

            productDeleteBtn.setDisable(true);
        }
    }

    public void searchProduct() {
        searchFromProduct();
    }

    public void updateProduct(ActionEvent actionEvent) {
        if (!editProductNewProductName.getText().equals("")) {
            BasicDBObject query = new BasicDBObject();
            query.put("id", searchedProductID);

            // UPDATE PRODUCT NAME
            BasicDBObject newUpdateName = new BasicDBObject();
            newUpdateName.put("name", editProductNewProductName.getText());

            BasicDBObject updateDocumentName = new BasicDBObject();
            updateDocumentName.put("$set", newUpdateName);

            LoginController.productCollection.updateOne(query, updateDocumentName);

            // UPDATE PRODUCT CATEGORY
            BasicDBObject newUpdateNote = new BasicDBObject();
            newUpdateNote.put("categoryName", editProductChoiceBox.getValue());

            BasicDBObject updateDocumentNote = new BasicDBObject();
            updateDocumentNote.put("$set", newUpdateNote);

            LoginController.productCollection.updateOne(query, updateDocumentNote);

            updateProductTable(false);
            searchFromProduct();
            editProductNewProductName.setText("");
//            editProductChoiceBox.setText("");
            productNotificationLabel.setText("Product updated successfully");
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        LoginController.productCollection.deleteOne(Filters.eq("id", searchedProductID));
        if (deleteCheck()) {
            productNotificationLabel.setText("Category deleted successfully");
        }
        updateProductTable(false);
        showProductName.setText("");
        showProductId.setText("");
        showProductCategory.setText("");
        editProductNewProductName.setText("");

        productDeleteBtn.setDisable(true);
    }

    public boolean deleteCheck() {
        boolean isDeleted = true;
        for (Document doc: productIterDoc) {
            int currentProductId = (Integer) doc.get("id");
            if (currentProductId == searchedProductID) {
                isDeleted = false;
                break;
            }
        }
        return isDeleted;
    }
}