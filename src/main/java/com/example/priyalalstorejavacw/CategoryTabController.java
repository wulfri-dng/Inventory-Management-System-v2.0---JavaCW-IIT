package com.example.priyalalstorejavacw;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.bson.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class CategoryTabController {
    public TableView<Category> categoryTable;
    public TableColumn<Category, Integer> categoryTableId;
    public TableColumn<Category, String> categoryTableName;
    public TableColumn<Category, String> categoryTableDescription;

    public TextField addCategoryName;
    public TextArea addCategoryNote;
    public TextField editCategorySearchTxtBox;
    public Label showCategoryName;
    public Label showCategoryNote;
    public Label showCategoryId;
    public TextField editCategoryNewCategoryName;
    public TextArea editCategoryNewCategoryNote;
    public Button categoryDeleteBtn;
    public Label categoryNotificationLabel;

    FindIterable<Document> categoryIterDoc = LoginController.categoryCollection.find();

    int searchedCategoryID;
//    String searchedCategoryIdOrName = "";

    public void initialize() {
        updateCategoryTable();
    }

    public void updateCategoryTable() {
        ObservableList<Category> list = FXCollections.observableArrayList();

        for (Document doc: categoryIterDoc) {
            int currentCategoryId = (int) doc.get("id");
            String currentCategoryName = (String) doc.get("name");
            String currentCategoryNote = (String) doc.get("note");  
            Category currentCategory = new Category(currentCategoryId, currentCategoryName, currentCategoryNote);
            list.add(currentCategory);
        }

        categoryTableId.setCellValueFactory(new PropertyValueFactory<Category, Integer>("categoryId"));
        categoryTableName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        categoryTableDescription.setCellValueFactory(new PropertyValueFactory<Category, String>("note"));
        categoryTable.setItems(list);
    }

    public int generateCategoryID() {
        ArrayList<Integer> categoryIdList = new ArrayList<Integer>();
        for (Document doc: categoryIterDoc) {
            int currentCategoryId = (Integer) doc.get("id");
            categoryIdList.add(currentCategoryId);
        }
        if (categoryIdList.size() == 0){
            return 1000;
        } else {
            return categoryIdList.get(categoryIdList.size()-1) + 1;
        }
    }

    public void addCategory(ActionEvent actionEvent) {
        Document document = new Document();
        String categoryName = addCategoryName.getText();
        String categoryNote = addCategoryNote.getText();

        if (!(addCategoryName.getText().equals(""))) {
            document.put("name", categoryName);
            if (!(addCategoryNote.getText().equals(""))) {
                document.put("note", categoryNote);
            }
            document.put("id", generateCategoryID());
            LoginController.categoryCollection.insertOne(document);
        }
        addCategoryName.setText("");
        addCategoryNote.setText("");
        updateCategoryTable();
    }

    public void searchFromCategory() {
        boolean isFound = false;
        for (Document doc: categoryIterDoc) {
            int currentCategoryId = (Integer) doc.get("id");
            String currentCategoryName = (String) doc.get("name");
            String currentCategoryNote = (String) doc.get("note");
            if (editCategorySearchTxtBox.getText().equals("" + currentCategoryId) || editCategorySearchTxtBox.getText().equals(currentCategoryName)) {
                isFound = true;
                searchedCategoryID = currentCategoryId;
//                searchedCategoryIdOrName = ("" + currentCategoryId);

                categoryNotificationLabel.setText("Category found");
                showCategoryNote.setText(currentCategoryNote);
                showCategoryName.setText(currentCategoryName);
                showCategoryId.setText("" + currentCategoryId);
                editCategoryNewCategoryName.setText(currentCategoryName);
                editCategoryNewCategoryNote.setText(currentCategoryNote);
                categoryDeleteBtn.setDisable(false);
            }
        }
        if (!isFound) {
            categoryNotificationLabel.setText("Category not found. Invalid ID or Name ");
            showCategoryName.setText("");
            showCategoryId.setText("");
            showCategoryNote.setText("");
            editCategoryNewCategoryNote.setText("");
            editCategoryNewCategoryName.setText("");
            categoryDeleteBtn.setDisable(true);
        }
    }

    public void searchCategory() {
//        searchedCategoryIdOrName = editCategorySearchTxtBox.getText();
        searchFromCategory();
    }

    public void updateCategory(ActionEvent actionEvent) {
        if (!editCategoryNewCategoryName.getText().equals("")) {
            BasicDBObject query = new BasicDBObject();
            query.put("id", searchedCategoryID);

            // UPDATE CATEGORY NAME
            BasicDBObject newUpdateName = new BasicDBObject();
            newUpdateName.put("name", editCategoryNewCategoryName.getText());

            BasicDBObject updateDocumentName = new BasicDBObject();
            updateDocumentName.put("$set", newUpdateName);

            LoginController.categoryCollection.updateOne(query, updateDocumentName);

            // UPDATE CATEGORY NOTE
            BasicDBObject newUpdateNote = new BasicDBObject();
            newUpdateNote.put("note", editCategoryNewCategoryNote.getText());

            BasicDBObject updateDocumentNote = new BasicDBObject();
            updateDocumentNote.put("$set", newUpdateNote);

            LoginController.categoryCollection.updateOne(query, updateDocumentNote);

            updateCategoryTable();
            searchFromCategory();
            editCategoryNewCategoryName.setText("");
            editCategoryNewCategoryNote.setText("");
            categoryNotificationLabel.setText("Category updated successfully");
        }
    }

    public void deleteCategory(ActionEvent actionEvent) {
        LoginController.categoryCollection.deleteOne(Filters.eq("id", searchedCategoryID));
        System.out.println(deleteCheck());
        if (deleteCheck()) {
            categoryNotificationLabel.setText("Category deleted successfully");
        }
        updateCategoryTable();
        showCategoryName.setText("");
        showCategoryId.setText("");
        showCategoryNote.setText("");
        editCategoryNewCategoryName.setText("");
        editCategoryNewCategoryNote.setText("");

        categoryDeleteBtn.setDisable(true);
    }

    public boolean deleteCheck() {
        boolean isDeleted = true;
        for (Document doc: categoryIterDoc) {
            int currentCategoryId = (Integer) doc.get("id");
            System.out.println("In DB - " + currentCategoryId);
            System.out.println(searchedCategoryID);

            if (currentCategoryId == searchedCategoryID) {
                System.out.println("deleted");
                isDeleted = false;
                break;
            }
        }
        return isDeleted;
    }
}