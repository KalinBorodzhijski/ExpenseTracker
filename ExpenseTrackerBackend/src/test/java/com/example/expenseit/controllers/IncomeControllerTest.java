package com.example.expenseit.controllers;
import com.example.expenseit.models.Income;
import com.example.expenseit.services.IncomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class IncomeControllerTest {

    @InjectMocks
    private IncomeController incomeController;

    @Mock
    private IncomeService incomeService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToIncome() throws Exception {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Map<String, String> body = new HashMap<>();
        body.put("transactionDate", "2023-01-01");
        body.put("amount", "100.0");
        body.put("note", "Test Note");

        when(incomeService.updateBalance(clientId, Double.parseDouble(body.get("amount")),
                body.get("note"), new SimpleDateFormat("yyyy-MM-dd").parse(body.get("transactionDate")))).thenReturn(100.0);

        ResponseEntity<Map<String, String>> response = incomeController.addToIncome(body, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("100.0", Objects.requireNonNull(response.getBody()).get("new_balance"));
    }

    @Test
    void getBalance() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        when(incomeService.getBalance(clientId)).thenReturn(100.0);

        ResponseEntity<Double> response = incomeController.getBalance(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody());
    }

    @Test
    void getIncomeHistory() {
        int clientId = 1;
        when(request.getAttribute("clientId")).thenReturn(clientId);

        Income income = new Income();
        List<Income> incomes = Collections.singletonList(income);

        when(incomeService.getIncomeHistory(clientId)).thenReturn(incomes);

        ResponseEntity<List<Income>> response = incomeController.getIncomeHistory(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(incomes, response.getBody());
    }
}
