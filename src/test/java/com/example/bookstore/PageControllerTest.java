package com.example.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void homePageLoads() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk())
           .andExpect(content().string(containsString("Featured books")));
    }

    @Test
    void catalogueSearchFilters() throws Exception {
        mvc.perform(get("/books").param("q", "achebe")).andExpect(status().isOk())
           .andExpect(content().string(containsString("Things Fall Apart")));
    }

    @Test
    void bookDetailLoads() throws Exception {
        mvc.perform(get("/books/1")).andExpect(status().isOk());
    }

    @Test
    void unknownBookReturns404() throws Exception {
        mvc.perform(get("/books/999")).andExpect(status().isNotFound());
    }

    @Test
    void aboutPageLoads() throws Exception {
        mvc.perform(get("/about")).andExpect(status().isOk());
    }
}
