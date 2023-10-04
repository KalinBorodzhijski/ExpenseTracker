package com.example.expenseit.services;

import com.example.expenseit.errors.InvalidDataException;
import com.example.expenseit.models.*;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories() {
        int clientId = 1;
        List<Category> categories = Collections.singletonList(new Category());
        when(categoryRepository.findAllByUserId_UserId(clientId)).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories(clientId);

        assertEquals(categories, result);
    }

    @Test
    void getCategory_Valid() {
        int clientId = 1;
        int categoryId = 1;
        Category category = new Category();
        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(category);

        Category result = categoryService.getCategory(clientId, categoryId);

        assertEquals(category, result);
    }

    @Test
    void getCategory_Invalid() {
        int clientId = 1;
        int categoryId = 1;
        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(null);

        assertThrows(InvalidDataException.class, () -> categoryService.getCategory(clientId, categoryId));
    }

    @Test
    void addCategory() {
        int clientId = 1;
        Client client = new Client();
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        Category category = new Category();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.addCategory(clientId, "title", "description");

        assertEquals(category, result);
    }


    @Test
    void deleteCategoryWithAllTransactions() {
        int clientId = 1;
        int categoryId = 1;
        Client client = new Client();
        client.setBalance(100.0);
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        Category category = new Category();
        Expense expense = new Expense();
        expense.setAmount(50.0);
        category.setExpenses(Collections.singletonList(expense));
        when(categoryRepository.deleteByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(Collections.singletonList(category));

        Category result = categoryService.deleteCategoryWithAllTransactions(clientId, categoryId);

        assertEquals(category, result);
        verify(clientRepository).save(client);
    }

    @Test
    void updateCategory() {
        int clientId = 1;
        int categoryId = 1;
        Category category = new Category();
        category.setTitle("Title");
        category.setDescription("Description");
        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(category);

        Category updatedCategory = new Category();
        updatedCategory.setTitle("New Title");
        updatedCategory.setDescription("New Description");
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(clientId, categoryId, "New Title", "New Description");

        assertNotNull(result, "The updated category should not be null");
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(categoryRepository).save(category);
    }

    @Test
    void getBudget() {
        int clientId = 1;
        int categoryId = 1;
        Category category = new Category();
        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(category);

        Budget budget = new Budget();
        budget.setAmount(100.0);
        budget.setBudgetDate(YearMonth.now());
        category.setBudgets(Collections.singletonList(budget));

        Double result = categoryService.getBudget(clientId, categoryId, YearMonth.now());

        assertEquals(100.0, result);
    }

    @Test
    void setBudgetPerCategory() {
        int clientId = 1;
        int categoryId = 1;
        Category category = new Category();
        category.setBudgets(new ArrayList<>());
        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(category);

        categoryService.setBudgetPerCategory(clientId, categoryId, 200.0, YearMonth.now());

        verify(categoryRepository).save(category);
    }

    @Test
    void getUnusedCategories() {
        int clientId = 1;
        Category category = new Category();
        Expense expense = new Expense();
        expense.setTransactionDate(LocalDate.now().minusMonths(4));
        category.setExpenses(Collections.singletonList(expense));
        when(categoryRepository.findAllByUserId_UserId(clientId)).thenReturn(Collections.singletonList(category));

        List<Category> result = categoryService.getUnusedCategories(clientId);

        assertEquals(1, result.size());
    }
}
