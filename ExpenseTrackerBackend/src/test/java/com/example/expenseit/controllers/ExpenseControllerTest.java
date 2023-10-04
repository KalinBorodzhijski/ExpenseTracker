package com.example.expenseit.controllers;

import com.example.expenseit.models.Expense;
import com.example.expenseit.models.dto.ExpensePredictionDTO;
import com.example.expenseit.models.dto.MonthlyTrendDTO;
import com.example.expenseit.services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllExpenses() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        List<Expense> expenses = Collections.singletonList(new Expense());
        when(expenseService.getAllExpensesByUsername(clientId)).thenReturn(expenses);

        ResponseEntity<List<Expense>> response = expenseController.getAllExpenses(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenses, response.getBody());
    }

    @Test
    void addNewExpense() throws ParseException {
        int clientId = 1;
        int categoryId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<String, String> map = new HashMap<>();
        map.put("amount", "100.0");
        map.put("note", "Test Note");
        map.put("transactionDate", "2023-01-01");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date transactionDate = dateFormat.parse(map.get("transactionDate"));

        doNothing().when(expenseService).addNewExpense(categoryId, clientId, Double.parseDouble(map.get("amount")), map.get("note"), transactionDate);

        ResponseEntity<Void> response = expenseController.addNewExpense(categoryId, map, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getAllExpensesByCategory() {
        int clientId = 1;
        int categoryId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        List<Expense> expenses = Collections.singletonList(new Expense());
        when(expenseService.getAllExpenses(categoryId, clientId)).thenReturn(expenses);

        ResponseEntity<List<Expense>> response = expenseController.getAllExpensesByCategory(categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenses, response.getBody());
    }

    @Test
    void getAllExpensesByCategoryAndMonth() {
        int clientId = 1;
        int categoryId = 1;
        String date = "202301";
        when(request.getAttribute("clientId")).thenReturn(clientId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, formatter);

        ExpensePredictionDTO expensePredictionDTO = new ExpensePredictionDTO();
        when(expenseService.getAllExpensesForMonth(categoryId, clientId, yearMonth)).thenReturn(expensePredictionDTO);

        ResponseEntity<ExpensePredictionDTO> response = expenseController.getAllExpensesByCategoryAndMonth(date, categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expensePredictionDTO, response.getBody());
    }

    @Test
    void getExpense() {
        int clientId = 1;
        int categoryId = 1;
        int expenseId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Expense expense = new Expense();
        when(expenseService.getExpense(categoryId, clientId, expenseId)).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.getExpense(categoryId, expenseId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    void deleteExpense() {
        int clientId = 1;
        int categoryId = 1;
        int expenseId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        doNothing().when(expenseService).deleteExpense(categoryId, clientId, expenseId);

        ResponseEntity<Void> response = expenseController.deleteExpense(categoryId, expenseId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateExpense() {
        int clientId = 1;
        int categoryId = 1;
        int expenseId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<String, String> map = new HashMap<>();
        map.put("amount", "100.0");
        map.put("note", "Updated Note");

        doNothing().when(expenseService).updateExpense(categoryId, clientId, expenseId, Double.parseDouble(map.get("amount")), map.get("note"));

        ResponseEntity<Void> response = expenseController.updateExpense(categoryId, expenseId, request, map);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMonthlyAverageExpensesPerCategory() {
        int clientId = 1;
        int categoryId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<YearMonth, Double> monthlyAverage = new HashMap<>();
        when(expenseService.getMonthlyAverageByCategory(clientId, categoryId)).thenReturn(monthlyAverage);

        ResponseEntity<Map<YearMonth, Double>> response = expenseController.getMonthlyAverageExpensesPerCategory(categoryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlyAverage, response.getBody());
    }

    @Test
    void getMonthlyAverageExpenses() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<String, Map<YearMonth, Double>> monthlyAverage = new HashMap<>();
        when(expenseService.getMonthlyAverage(clientId)).thenReturn(monthlyAverage);

        ResponseEntity<Map<String, Map<YearMonth, Double>>> response = expenseController.getMonthlyAverageExpenses(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlyAverage, response.getBody());
    }

    @Test
    void getMonthlyPredictions() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        List<Double> predictions = Collections.singletonList(100.0);
        when(expenseService.getMonthlyExpenseDataAndPredictions(clientId)).thenReturn(predictions);

        ResponseEntity<List<Double>> response = expenseController.getMonthlyPredictions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(predictions, response.getBody());
    }

    @Test
    void getRisingTrend() throws IOException, InterruptedException {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        MonthlyTrendDTO monthlyTrendDTO = new MonthlyTrendDTO();
        when(expenseService.getTrendAnalysisData(clientId)).thenReturn(monthlyTrendDTO);

        ResponseEntity<MonthlyTrendDTO> response = expenseController.getRisingTrend(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlyTrendDTO, response.getBody());
    }

    @Test
    void getMonthlyExpensesPerCategory() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<String, Double> monthlyExpensesPerCategory = new HashMap<>();
        when(expenseService.getMonthlyExpensesPerCategory(clientId)).thenReturn(monthlyExpensesPerCategory);

        ResponseEntity<Map<String, Double>> response = expenseController.getMonthlyExpensesPerCategory(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlyExpensesPerCategory, response.getBody());
    }
}
