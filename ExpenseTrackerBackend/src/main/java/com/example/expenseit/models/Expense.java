package com.example.expenseit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_expense")
@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int expenseId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "note")
    private String note;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

}
