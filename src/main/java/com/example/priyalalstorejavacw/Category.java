package com.example.priyalalstorejavacw;

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

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}