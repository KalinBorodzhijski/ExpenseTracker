package com.example.expenseit.services;

import com.example.expenseit.errors.InvalidDataException;
import com.example.expenseit.models.Category;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.Expense;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import com.example.expenseit.repositories.ExpenseRepository;
import com.example.expenseit.services.IncomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private IncomeService incomeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllExpensesByUsername() {
        int clientId = 1;
        Category category = new Category();
        Expense expense = new Expense();
        category.setExpenses(Collections.singletonList(expense));

        when(categoryRepository.findAllByUserId_UserId(clientId)).thenReturn(Collections.singletonList(category));

        List<Expense> result = expenseService.getAllExpensesByUsername(clientId);

        assertEquals(1, result.size());
        assertEquals(expense, result.get(0));
    }

    @Test
    void getMonthlyAverageByCategory() {
        int clientId = 1;
        int categoryId = 1;
        Category category = new Category();
        Expense expense = new Expense();
        expense.setTransactionDate(LocalDate.now());
        category.setExpenses(Collections.singletonList(expense));

        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(category);

        Map<YearMonth, Double> result = expenseService.getMonthlyAverageByCategory(clientId, categoryId);

        assertFalse(result.isEmpty());
    }

    @Test
    void getMonthlyAverage() {
        int clientId = 1;
        Category category = new Category();
        category.setTitle("Test Category");
        Expense expense = new Expense();
        expense.setTransactionDate(LocalDate.now());
        category.setExpenses(Collections.singletonList(expense));

        when(categoryRepository.findAllByUserId_UserId(clientId)).thenReturn(Collections.singletonList(category));

        Map<String, Map<YearMonth, Double>> result = expenseService.getMonthlyAverage(clientId);

        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("Test Category"));
    }

    @Test
    void getExpense() {
        int categoryId = 1;
        int userId = 1;
        int expenseId = 1;
        Category category = new Category();
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        category.setExpenses(Collections.singletonList(expense));

        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, userId)).thenReturn(category);

        Expense result = expenseService.getExpense(categoryId, userId, expenseId);

        assertEquals(expense, result);
    }


    @Test
    void updateExpense() {
        int categoryId = 1;
        int userId = 1;
        int expenseId = 1;
        double amount = 100.0;
        String note = "Updated Note";
        Category category = new Category();
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        category.setExpenses(Collections.singletonList(expense));

        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, userId)).thenReturn(category);

        expenseService.updateExpense(categoryId, userId, expenseId, amount, note);

        verify(categoryRepository, times(1)).save(category);
        assertEquals(amount, expense.getAmount());
        assertEquals(note, expense.getNote());
    }

    @Test
    void deleteExpense() {
        int categoryId = 1;
        int userId = 1;
        int expenseId = 1;
        Client client = new Client();
        client.setBalance(100.0);
        Category category = new Category();
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        expense.setAmount(50.0);
        category.setExpenses(new ArrayList<>());
        category.getExpenses().add(expense);

        when(clientRepository.findByUserId(userId)).thenReturn(client);
        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, userId)).thenReturn(category);

        expenseService.deleteExpense(categoryId, userId, expenseId);

        verify(expenseRepository, times(1)).deleteByExpenseId(expenseId);
        verify(categoryRepository, times(1)).save(category);
        assertEquals(150.0, client.getBalance());
    }

    @Test
    void getAllExpensesForMonth() {
        int categoryId = 1;
        int clientId = 1;
        YearMonth yearMonth = YearMonth.now();
        Expense expense = new Expense();
        expense.setTransactionDate(LocalDate.now());
        Category category = new Category();
        category.setExpenses(Collections.singletonList(expense));

        when(categoryRepository.findByCategoryIdAndUserId_UserId(categoryId, clientId)).thenReturn(category);
        assertNotNull(expenseService.getAllExpensesForMonth(categoryId, clientId, yearMonth));
    }



    @Test
    void getTrendAnalysisData() throws InterruptedException, IOException {
        int clientId = 1;

        assertNotNull(expenseService.getTrendAnalysisData(clientId));
    }

    @Test
    void getMonthlyExpenseDataAndPredictions() {
        int clientId = 1;

        assertNotNull(expenseService.getMonthlyExpenseDataAndPredictions(clientId));
    }

    @Test
    void predictRemainingMonth() {
        double[] expenses = {100.0, 200.0, 300.0};

        double[] result = expenseService.predictRemainingMonth(expenses);

        assertNotNull(result);
    }

    @Test
    void getMonthlyExpensesSumWithMissingMonths() {
        Map<YearMonth, Double> monthlyExpensesSum = new HashMap<>();
        monthlyExpensesSum.put(YearMonth.now(), 100.0);

        Map<YearMonth, Double> result = expenseService.getMonthlyExpensesSumWithMissingMonths(monthlyExpensesSum);

        assertFalse(result.isEmpty());
    }

    @Test
    void getMonthlyExpensesSum() {
        Expense expense = new Expense();
        expense.setTransactionDate(LocalDate.now());

        Map<YearMonth, Double> result = expenseService.getMonthlyExpensesSum(Collections.singletonList(expense));

        assertFalse(result.isEmpty());
    }

    @Test
    void isRisingTrend() {
        Map<YearMonth, Double> predictions = new HashMap<>();
        predictions.put(YearMonth.now().plusMonths(1), 100.0);
        predictions.put(YearMonth.now().plusMonths(2), 200.0);
        predictions.put(YearMonth.now().plusMonths(3), 300.0);

        assertTrue(expenseService.isRisingTrend(predictions));
    }

    @Test
    void predictNextMonths() throws InterruptedException, IOException {
        Map<YearMonth, Double> expenses = new HashMap<>();
        expenses.put(YearMonth.now(), 100.0);

        assertNotNull(expenseService.predictNextMonths(expenses));
    }

    @Test
    void findExpenseById() {
        int expenseId = 1;
        Category category = new Category();
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        category.setExpenses(Collections.singletonList(expense));

        assertEquals(expense, expenseService.findExpenseById(category, expenseId));
    }
}
