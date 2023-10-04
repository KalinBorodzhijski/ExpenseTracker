package com.example.expenseit.controllers;

import com.example.expenseit.controllers.CategoryController;
import com.example.expenseit.models.Category;
import com.example.expenseit.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Category category = Category.builder().title("Food").description("Food expenses").build();
        List<Category> categories = Collections.singletonList(category);

        when(categoryService.getAllCategories(clientId)).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }

    @Test
    void addCategory() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<String, String> map = new HashMap<>();
        map.put("title", "Test Title");
        map.put("description", "Test Description");

        Category category = Category.builder().title("Food").description("Food expenses").build();

        when(categoryService.addCategory(clientId, map.get("title"), map.get("description"))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.addCategory(map, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void getCategory() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        int categoryId = 1;
        Category category = Category.builder().title("Food").description("Food expenses").build();

        when(categoryService.getCategory(clientId, categoryId)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.getCategory(categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void updateCategory() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        int categoryId = 1;
        Map<String, String> map = new HashMap<>();
        map.put("title", "Updated Title");
        map.put("description", "Updated Description");

        Category category = Category.builder().title("Food").description("Food expenses").build();

        when(categoryService.updateCategory(clientId, categoryId, map.get("title"), map.get("description"))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, map, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void deleteCategory() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        int categoryId = 1;
        Category category = Category.builder().title("Food").description("Food expenses").build();

        when(categoryService.deleteCategoryWithAllTransactions(clientId, categoryId)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.deleteCategory(categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void getBudget() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Integer categoryId = 1;
        String date = "202301";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, formatter);

        when(categoryService.getBudget(clientId, categoryId, yearMonth)).thenReturn(100.0);

        ResponseEntity<Double> response = categoryController.getBudget(date, categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody());
    }

    @Test
    void setBudget() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Integer categoryId = 1;
        String date = "202301";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, formatter);

        Map<String, String> map = new HashMap<>();
        map.put("amount", "100.0");

        doNothing().when(categoryService).setBudgetPerCategory(clientId, categoryId, Double.valueOf(map.get("amount")), yearMonth);

        ResponseEntity<Void> response = categoryController.setBudget(date, map, categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUnusedCategories() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Category category = Category.builder().title("Food").description("Food expenses").build();
        List<Category> categories = Collections.singletonList(category);

        when(categoryService.getUnusedCategories(clientId)).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getUnusedCategories(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }
}
