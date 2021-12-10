package com.example.priyalalstorejavacw;

import javafx.scene.control.Button;

public class Category {
    int categoryId;
    String name;
    String note;

    public Category(int categoryId, String name, String note) {
        this.name = name;
        this.categoryId = categoryId;
        this.note = note;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
