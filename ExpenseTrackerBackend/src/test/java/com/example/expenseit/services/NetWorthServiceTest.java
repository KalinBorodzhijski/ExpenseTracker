package com.example.expenseit.services;

import com.example.expenseit.models.Client;
import com.example.expenseit.models.Expense;
import com.example.expenseit.models.Income;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NetWorthServiceTest {

    @InjectMocks
    private NetWorthService netWorthService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMonthlyNetWorth() {
        int clientId = 1;
        Client client = new Client();
        client.setBalance(100.0);
        client.setIncomeHistory(new ArrayList<>());
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        Map<YearMonth, Double> result = netWorthService.getMonthlyNetWorth(clientId);

        assertFalse(result.isEmpty());
    }

    @Test
    void getPredictedNetWorth() {
        int clientId = 1;
        Client client = new Client();
        client.setBalance(100.0);
        client.setIncomeHistory(new ArrayList<>());
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        Map<YearMonth, Double> result = netWorthService.getPredictedNetWorth(clientId);

        assertFalse(result.isEmpty());
    }
}
