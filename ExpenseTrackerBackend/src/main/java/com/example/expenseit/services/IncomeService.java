package com.example.expenseit.services;

import com.example.expenseit.errors.InvalidDataException;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.Income;
import com.example.expenseit.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class IncomeService {

    private ClientRepository clientRepository;

    public double updateBalance(int clientId, double value, String note, Date transactionDate) {
        Client client = clientRepository.findByUserId(clientId);
        if(value <= 0) throw new InvalidDataException("Value should be greater than 0 !");
        client.setBalance(client.getBalance() + value);
        client.getIncomeHistory()
                .add(Income.builder()
                        .amount(value)
                        .note(note)
                        .transactionDate(LocalDateTime.ofInstant(transactionDate.toInstant(), ZoneId.systemDefault()).toLocalDate())
                        .build());
        clientRepository.save(client);
        return client.getBalance();
    }

    public List<Income> getIncomeHistory(int clientId) {
        Client client = clientRepository.findByUserId(clientId);
        if(client == null) return Collections.emptyList();
        return client.getIncomeHistory();
    }

    public Double getBalance(int clientId) {
        Client client = clientRepository.findByUserId(clientId);
        if(client == null){
            return 0.0;
        }
        return client.getBalance();
    }

}
