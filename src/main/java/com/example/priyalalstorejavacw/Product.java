package com.example.priyalalstorejavacw;

public class Product {
    int id;
    String name;
    Category category;
    String categoryName;
    int stockCount;

    public Product(int id, String name, String categoryName, int stockCount) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.stockCount = stockCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }
}