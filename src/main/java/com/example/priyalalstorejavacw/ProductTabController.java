package com.example.priyalalstorejavacw;

import com.mongodb.client.FindIterable;
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

    public void initialize() {
        updateProductTable(true);

    }

    public void updateProductTable(boolean updateChoiceBox) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        ArrayList<String> categoryList = new ArrayList<>();

        for (Document doc: productIterDoc) {
            int currentProductId = (Integer) doc.get("id");
            String currentProductName = (String) doc.get("name");
            String currentProductCategory = (String) doc.get("categoryName");
            Product currentProduct = new Product(currentProductId, currentProductName, currentProductCategory);
            productList.add(currentProduct);

            if (updateChoiceBox) {
                boolean isAvailable = false;
                for (String currentCategory: categoryList){
                    if (currentCategory.equals(currentProductCategory)) {
                        isAvailable = true;
                        break;
                    }
                }
                categoryList.add(currentProductCategory);
                if (!isAvailable) {
                    addProductChoiceBox.getItems().add(currentProductCategory);
                    editProductChoiceBox.getItems().add(currentProductCategory);
                }
            }
        }

        productTableId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productTableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productTableCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
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

    public void searchProduct(ActionEvent actionEvent) {
    }

    public void deleteCategory(ActionEvent actionEvent) {
    }

    public void updateProduct(ActionEvent actionEvent) {
    }

}
