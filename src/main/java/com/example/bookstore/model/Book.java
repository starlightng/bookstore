package com.example.bookstore.model;

import java.math.BigDecimal;

public record Book(
        long id,
        String title,
        String author,
        String category,
        int year,
        BigDecimal price,
        String description) {

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
