package com.example.expenseit.services;

import com.example.expenseit.errors.InvalidDataException;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.Income;
import com.example.expenseit.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncomeServiceTest {

    @InjectMocks
    private IncomeService incomeService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateBalance_Valid() {
        int clientId = 1;
        double value = 100.0;
        String note = "note";
        Date transactionDate = new Date();

        Client client = new Client();
        client.setBalance(0.0);
        client.setIncomeHistory(new ArrayList<>());
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        double result = incomeService.updateBalance(clientId, value, note, transactionDate);

        assertEquals(value, result);
    }

    @Test
    void updateBalance_Invalid() {
        int clientId = 1;
        double value = -100.0;
        String note = "note";
        Date transactionDate = new Date();

        assertThrows(InvalidDataException.class, () -> incomeService.updateBalance(clientId, value, note, transactionDate));
    }

    @Test
    void getIncomeHistory() {
        int clientId = 1;
        Client client = new Client();
        List<Income> incomes = Collections.singletonList(new Income());
        client.setIncomeHistory(incomes);
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        List<Income> result = incomeService.getIncomeHistory(clientId);

        assertEquals(incomes, result);
    }


    @Test
    void getBalance() {
        int clientId = 1;
        Client client = new Client();
        client.setBalance(100.0);
        when(clientRepository.findByUserId(clientId)).thenReturn(client);

        Double result = incomeService.getBalance(clientId);

        assertEquals(100.0, result);
    }
}
