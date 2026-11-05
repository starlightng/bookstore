package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * In-memory catalogue. Replace with a repository (e.g. Spring Data JPA)
 * when you are ready for a real database.
 */
@Service
public class BookService {

    private final List<Book> books = List.of(
            new Book(1, "Things Fall Apart", "Chinua Achebe", "Fiction", 1958, new BigDecimal("12.99"),
                    "A landmark novel about Okonkwo, a proud Igbo leader, and the arrival of colonial rule."),
            new Book(2, "Half of a Yellow Sun", "Chimamanda Ngozi Adichie", "Fiction", 2006, new BigDecimal("15.50"),
                    "Three lives intertwine during the Nigerian Civil War."),
            new Book(3, "Clean Code", "Robert C. Martin", "Technology", 2008, new BigDecimal("39.99"),
                    "A handbook of agile software craftsmanship."),
            new Book(4, "Effective Java", "Joshua Bloch", "Technology", 2018, new BigDecimal("44.00"),
                    "Best practices for the Java platform, updated for modern Java."),
            new Book(5, "Sapiens", "Yuval Noah Harari", "History", 2011, new BigDecimal("18.75"),
                    "A brief history of humankind, from the Stone Age to the present."),
            new Book(6, "Atomic Habits", "James Clear", "Self-Help", 2018, new BigDecimal("16.20"),
                    "Practical strategies for building good habits and breaking bad ones.")
    );

    public List<Book> findAll() {
        return books;
    }

    public List<Book> featured() {
        return books.stream().limit(3).toList();
    }

    public Optional<Book> findById(long id) {
        return books.stream().filter(b -> b.id() == id).findFirst();
    }

    public List<Book> search(String query) {
        if (query == null || query.isBlank()) {
            return books;
        }
        String q = query.toLowerCase(Locale.ROOT).trim();
        return books.stream()
                .filter(b -> b.title().toLowerCase(Locale.ROOT).contains(q)
                        || b.author().toLowerCase(Locale.ROOT).contains(q)
                        || b.category().toLowerCase(Locale.ROOT).contains(q))
                .toList();
    }
}
