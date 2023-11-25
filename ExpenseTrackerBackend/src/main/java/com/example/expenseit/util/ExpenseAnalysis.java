package com.example.expenseit.util;

import com.example.expenseit.models.Expense;
import lombok.experimental.UtilityClass;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

@UtilityClass
public class ExpenseAnalysis {

    public static Map<YearMonth, Double> calculateMonthlyTotalByCategory(List<Expense> expenses){
        Map<YearMonth, Double> totalAmountPerMonth = new HashMap<>();
        YearMonth currentMonth = YearMonth.now();

        for (Expense expense : expenses) {
            YearMonth yearMonth = YearMonth.from(expense.getTransactionDate());

            // Check if the expense is within the last 8 months
            long monthsDifference = ChronoUnit.MONTHS.between(yearMonth, currentMonth);
            if (monthsDifference >= 0 && monthsDifference < 8) {
                double amount = expense.getAmount();
                totalAmountPerMonth.compute(yearMonth, (k, v) -> v == null ? amount : v + amount);
            }
        }

        List<Map.Entry<YearMonth, Double>> entryList = new ArrayList<>(totalAmountPerMonth.entrySet());
        entryList.sort(Map.Entry.comparingByKey());
        Map<YearMonth, Double> sortedTotalAmountPerMonth = new LinkedHashMap<>();
        for (Map.Entry<YearMonth, Double> entry : entryList) {
            sortedTotalAmountPerMonth.put(entry.getKey(), entry.getValue());
        }

        return sortedTotalAmountPerMonth;
    }

}
