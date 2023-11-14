package com.example.expenseit.services;

import com.example.expenseit.models.Client;
import com.example.expenseit.models.Expense;
import com.example.expenseit.models.Income;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NetWorthService {

    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;

    public NetWorthService(CategoryRepository categoryRepository, ClientRepository clientRepository) {
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
    }

    public Map<YearMonth, Double> getMonthlyNetWorth(int clientId) {
        LocalDate fiveMonthsAgo = LocalDate.now().minusMonths(5);
        Client client = clientRepository.findByUserId(clientId);
        if(client == null) return Collections.emptyMap();
        Double balance = client.getBalance();

        Map<YearMonth, Double> balanceTrend = new HashMap<>();

        Map<YearMonth, Double> monthlyExpenses  = categoryRepository.findAllByUserId_UserId(clientId).stream()
                 .flatMap(category -> category.getExpenses().stream())
                 .filter(expense -> !expense.getTransactionDate().isBefore(fiveMonthsAgo))
                 .collect(Collectors.groupingBy(
                         expense -> YearMonth.from(expense.getTransactionDate()),
                         Collectors.summingDouble(Expense::getAmount)
                 ));

        Map<YearMonth, Double> monthlyIncomes = client.getIncomeHistory().stream()
                .filter(income -> !income.getTransactionDate().isBefore(fiveMonthsAgo))
                .collect(Collectors.groupingBy(
                        income -> YearMonth.from(income.getTransactionDate()),
                        Collectors.summingDouble(Income::getAmount)
                ));
        YearMonth currentMonth = YearMonth.now();

        balanceTrend.put(YearMonth.from(LocalDate.now()), balance);
        double currentBalance = balance;
        for (int i = 0; i < 5; i++) {

            double incomesThisMonth = monthlyIncomes.getOrDefault(currentMonth, 0.0);
            double expensesThisMonth = monthlyExpenses.getOrDefault(currentMonth, 0.0);
            currentBalance -= (incomesThisMonth - expensesThisMonth);
            currentMonth = YearMonth.from(currentMonth.minusMonths(1));
            balanceTrend.put(currentMonth, currentBalance);

        }
        TreeMap<YearMonth, Double> reversedMap = new TreeMap<>(balanceTrend);
        return reversedMap;
    }

    public Map<YearMonth, Double> getPredictedNetWorth(int clientId) {
        Map<YearMonth, Double> historicalData = getMonthlyNetWorth(clientId);
        int n = historicalData.size();
        if(n == 0) return Collections.emptyMap();
        double sumX = 0, sumY = 0, sumX2 = 0, sumXY = 0;

        int baseYear = YearMonth.now().minusMonths(n).getYear();
        for (YearMonth month : historicalData.keySet()) {
            int x = (month.getYear() - baseYear) * 12 + month.getMonthValue();
            double y = historicalData.get(month);

            sumX += x;
            sumY += y;
            sumX2 += x * x;
            sumXY += x * y;
        }
        double m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double b = (sumY - m * sumX) / n;

        Map<YearMonth, Double> predictions = new TreeMap<>();
        YearMonth startMonth = YearMonth.from(historicalData.keySet().stream().max(YearMonth::compareTo).get());
        for (int i = 1; i <= 3; i++) {
            YearMonth nextMonth = startMonth.plusMonths(i);
            int x = (nextMonth.getYear() - baseYear) * 12 + nextMonth.getMonthValue();
            double prediction = m * x + b;
            predictions.put(nextMonth, prediction);
        }

        return predictions;
    }
}
