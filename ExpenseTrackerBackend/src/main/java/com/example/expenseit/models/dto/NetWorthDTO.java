package com.example.expenseit.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NetWorthDTO {
    private Map<YearMonth, Double> actualNetWorth;
    private Map<YearMonth, Double> predictedNetWorth;
}
