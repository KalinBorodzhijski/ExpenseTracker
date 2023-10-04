package com.example.expenseit.util;

import com.example.expenseit.models.Expense;
import lombok.experimental.UtilityClass;

import java.time.YearMonth;
import java.util.*;

@UtilityClass
public class ExpenseAnalysis {

    public static Map<YearMonth, Double> calculateMonthlyAverageByCategory(List<Expense> expenses){

        Map<YearMonth, Double> averageAmountPerMonth = new HashMap<>();
        Map<YearMonth, Map.Entry<Double, Integer>> sumAndCountPerMonth = new HashMap<>();
        for (Expense expense : expenses) {
            YearMonth yearMonth = YearMonth.from(expense.getTransactionDate());
            double amount = expense.getAmount();

            sumAndCountPerMonth.compute(yearMonth, (k, v) -> {
                if (v == null) {
                    return Map.entry(amount, 1);
                } else {
                    double sum = v.getKey() + amount;
                    int count = v.getValue() + 1;
                    return Map.entry(sum, count);
                }
            });
        }

        for (Map.Entry<YearMonth, Map.Entry<Double, Integer>> entry : sumAndCountPerMonth.entrySet()) {
            YearMonth yearMonth = entry.getKey();
            double sum = entry.getValue().getKey();
            int count = entry.getValue().getValue();
            double average = sum / count;
            averageAmountPerMonth.put(yearMonth, average);
        }

        List<Map.Entry<YearMonth, Double>> entryList = new ArrayList<>(averageAmountPerMonth.entrySet());
        entryList.sort(Map.Entry.comparingByKey());
        Map<YearMonth, Double> sortedAverageAmountPerMonth = new LinkedHashMap<>();
        for (Map.Entry<YearMonth, Double> entry : entryList) {
            sortedAverageAmountPerMonth.put(entry.getKey(), entry.getValue());
        }

        return sortedAverageAmountPerMonth;
    }

}
