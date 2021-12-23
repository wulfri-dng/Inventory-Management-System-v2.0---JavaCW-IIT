package com.example.priyalalstorejavacw;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.bson.Document;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Locale;

public class ProductTabController {
    public TableView<Product> productTable;
    public TableColumn<Product, Integer> productTableId;
    public TableColumn<Product, String> productTableName;
    public TableColumn<Product, String> productTableCategory;
    public TableColumn<Product, Integer> productTableStockCount;
    public TableColumn<Product, String> productTableUpdatedDate;

    public TextField editProductSearchTxtBox;
    public Button productDeleteBtn;
    public Label productNotificationLabel;
    public Label showProductName;
    public Label showProductId;
    public Label showProductCategory;
    public TextField editProductNewProductName;
    public TextField addProductName;
    public ChoiceBox<String> addProductChoiceBox;
    public TextField addStockCount;
    public ComboBox<String> editProductComboBox;
    public ComboBox<String> addProductComboBox;
    public ComboBox<String> addStockComboBox;
    public Button productUpdateBtn;
    public Pane editProductControlPane;

    FindIterable<Document> productIterDoc = LoginController.productCollection.find();
    FindIterable<Document> categoryIterDoc = LoginController.categoryCollection.find();

    int searchedProductID;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    LocalDateTime now = LocalDateTime.now();

    public void initialize() {
        updateProductTab();
    }

    public void updateProductTab() {
        ObservableList<Product> productList = FXCollections.observableArrayList();

        for (Document doc: productIterDoc) {
            int currentProductId = (Integer) doc.get("id");
            String currentProductName = (String) doc.get("name");
            int currentProductCategoryId = (Integer) doc.get("categoryId");
            String currentProductCategoryName = getNameFromCategoryId(currentProductCategoryId);
            int currentProductStockCount = (Integer) doc.get("stockCount");
            String currentProductLastUpdatedDateAndTime = (String) doc.get("lastUpdatedDateTime");
            Product currentProduct = new Product(currentProductId, currentProductName, currentProductCategoryName, currentProductStockCount, currentProductLastUpdatedDateAndTime);
            productList.add(currentProduct);
        }
        productTableId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productTableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productTableCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
        productTableStockCount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stockCount"));
        productTableUpdatedDate.setCellValueFactory(new PropertyValueFactory<Product, String>("lastUpdatedDateTime"));
        productTable.setItems(productList);

        // UPDATE COMBO BOXES
        ObservableList<String> categoryNameList = FXCollections.observableArrayList();
        for (Document doc: categoryIterDoc) {
            categoryNameList.add((String)doc.get("name"));
        }
        addProductComboBox.setItems(categoryNameList);
        editProductComboBox.setItems(categoryNameList);

        ObservableList<String> productNameList = FXCollections.observableArrayList();
        for (Document doc: productIterDoc) {
            productNameList.add((String)doc.get("name"));
        }
        addStockComboBox.setItems(productNameList);
    }

    public String getNameFromCategoryId(int categoryId) {
        String categoryName = null;
        for (Document doc : categoryIterDoc) {
            int currentCategoryId = (Integer) doc.get("id");
            if (currentCategoryId == categoryId) {
                categoryName = (String) doc.get("name");
                break;
            }
        }
        return categoryName;
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

    public int getIdFromCategoryName(String categoryName) {
        int categoryId = 0;
        for (Document doc : categoryIterDoc) {
            String currentCategoryName = (String) doc.get("name");
            if (currentCategoryName.equals(categoryName)) {
                categoryId = (Integer) doc.get("id");
                break;
            }
        }
        return categoryId;
    }

    public boolean checkProductAvailability(String productName) {
        for (Document doc: productIterDoc) {
            String currentProduct = (String) doc.get("name");
            if (currentProduct.toLowerCase(Locale.ROOT).equals(productName.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public void addProduct() {
        if (checkProductAvailability(addProductName.getText())) {
            Document productDocument = new Document();
            boolean canRun = false;
            try {
                if (!(addProductName.getText().equals("")) && !addProductComboBox.getValue().equals("")) {
                    canRun = true;
                }
            } catch (RuntimeException e) {
                System.out.println(e);
            }
            if (canRun) {
                productDocument.put("id", generateProductID());
                productDocument.put("name", addProductName.getText());
                productDocument.put("categoryId", getIdFromCategoryName(addProductComboBox.getValue()));
                productDocument.put("stockCount", 0);
                productDocument.put("lastUpdatedDateTime", dtf.format(now));
                LoginController.productCollection.insertOne(productDocument);
            }
            addProductName.setText("");
            updateProductTab();
        }
    }

    public BasicDBObject updateDateTime() {
        // UPDATE LAST UPDATED TIME
        BasicDBObject currentDateTime = new BasicDBObject();
        currentDateTime.put("lastUpdatedDateTime", dtf.format(now));

        BasicDBObject updatedDocumentTime = new BasicDBObject();
        updatedDocumentTime.put("$set", currentDateTime);
        return updatedDocumentTime;
    }

    public void updateProduct() {
        if (checkProductAvailability(editProductNewProductName.getText())){
            boolean canRun = false;
            try {
                if (!editProductNewProductName.getText().equals("") && !(editProductComboBox.getValue().equals(""))) {
                    canRun = true;
                }
            } catch (RuntimeException e) {
                System.out.println(e);
            }
            if (canRun) {
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
                newUpdateNote.put("categoryId", getIdFromCategoryName(editProductComboBox.getValue()));

                BasicDBObject updateDocumentNote = new BasicDBObject();
                updateDocumentNote.put("$set", newUpdateNote);

                LoginController.productCollection.updateOne(query, updateDocumentNote);

                // UPDATE PRODUCT LAST UPDATED TIME
                BasicDBObject currentDateTime = new BasicDBObject();
                currentDateTime.put("lastUpdatedDateTime", dtf.format(now));

                BasicDBObject updatedDocumentTime = new BasicDBObject();
                updatedDocumentTime.put("$set", currentDateTime);

                LoginController.productCollection.updateOne(query, updatedDocumentTime);

                LoginController.productCollection.updateOne(query, updateDateTime());

                updateProductTab();
                searchProduct();
                editProductControlPane.setDisable(true);
                editProductNewProductName.setText("");
//            editProductChoiceBox.setText("");
                productNotificationLabel.setText("Product updated successfully.");
            } else {
                productNotificationLabel.setText("Error. Enter a new product name and category.");
            }
        }
    }

    public void addStock() {
        boolean canRun = false;
        int stockCount = 0;
        try {
            stockCount = Integer.parseInt(addStockCount.getText());
            if (!addStockCount.getText().equals("") && !addStockComboBox.getValue().equals("")) {
                canRun = true;
            }
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        if (canRun) {
            BasicDBObject query = new BasicDBObject();
            query.put("name", addStockComboBox.getValue());

            // UPDATE PRODUCT STOCK
            BasicDBObject productStockCount = new BasicDBObject();
            productStockCount.put("stockCount", stockCount);

            BasicDBObject updateDocumentStockCount = new BasicDBObject();
            updateDocumentStockCount.put("$set", productStockCount);

            LoginController.productCollection.updateOne(query, updateDocumentStockCount);

            LoginController.productCollection.updateOne(query, updateDateTime());

            updateProductTab();
            addStockCount.setText("");
        }
    }

    public void searchProduct() {
        boolean isFound = false;
        for (Document doc: productIterDoc) {
            int currentProductId = (Integer) doc.get("id");
            String currentProductName = (String) doc.get("name");
            String currentProductCategory = (String) doc.get("categoryName");
            if (editProductSearchTxtBox.getText().equals("" + currentProductId) || editProductSearchTxtBox.getText().toLowerCase().equals(currentProductName.toLowerCase())) {
                isFound = true;
                searchedProductID = currentProductId;

                productNotificationLabel.setText("Product found");
                showProductId.setText("" + currentProductId);
                showProductName.setText(currentProductName);
                showProductCategory.setText(currentProductCategory);
                editProductNewProductName.setText(currentProductName);
                productDeleteBtn.setDisable(false);
                productUpdateBtn.setDisable(false);
                editProductControlPane.setDisable(false);
            }
        }
        if (!isFound) {
            productNotificationLabel.setText("Category not found. Invalid ID or Name ");
            showProductId.setText("");
            showProductName.setText("");
            showProductCategory.setText("");
            editProductNewProductName.setText("");
            productUpdateBtn.setDisable(true);
            productDeleteBtn.setDisable(true);
            editProductControlPane.setDisable(true);
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        LoginController.productCollection.deleteOne(Filters.eq("id", searchedProductID));
        if (deleteCheck()) {
            productNotificationLabel.setText("Category deleted successfully");
            productUpdateBtn.setDisable(true);
        }
        updateProductTab();
        showProductName.setText("");
        showProductId.setText("");
        showProductCategory.setText("");
        editProductNewProductName.setText("");
        productDeleteBtn.setDisable(true);
        editProductControlPane.setDisable(true);
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