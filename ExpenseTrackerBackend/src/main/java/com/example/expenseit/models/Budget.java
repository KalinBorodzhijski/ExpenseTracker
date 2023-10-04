package com.example.expenseit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_budget")
@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int budgetId;

    @Column(name = "budget_date", nullable = false)
    private YearMonth budgetDate;

    @Column(name = "amount", nullable = false)
    private Double amount;
}
