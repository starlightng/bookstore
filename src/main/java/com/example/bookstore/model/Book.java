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
}
