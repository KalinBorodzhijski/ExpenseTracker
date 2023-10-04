package com.example.expenseit.models.dto;

import com.example.expenseit.models.Expense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpensePredictionDTO {
    List<Expense> monthlyExpenses;
    List<Double> currentMonthPrediction;
}
