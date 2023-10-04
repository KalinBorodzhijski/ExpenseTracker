package com.example.expenseit.controllers;

import com.example.expenseit.models.Expense;
import com.example.expenseit.models.dto.ExpensePredictionDTO;
import com.example.expenseit.models.dto.MonthlyTrendDTO;
import com.example.expenseit.services.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private ExpenseService expenseService;


    @GetMapping("")
    public ResponseEntity<List<Expense>> getAllExpenses(HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        List<Expense> expenses = expenseService.getAllExpensesByUsername(clientId);
        return new ResponseEntity<>(expenses,HttpStatus.OK);
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<Void> addNewExpense(@PathVariable int categoryId,
                                                 @RequestBody Map<String,String> map,
                                                 HttpServletRequest request){
        Date transactionDate;
        int clientId = (int)request.getAttribute("clientId");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            transactionDate = dateFormat.parse(map.get("transactionDate"));
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        expenseService.addNewExpense(categoryId,clientId,Double.parseDouble(map.get("amount")),map.get("note"), transactionDate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<Expense>> getAllExpensesByCategory(@PathVariable int categoryId,
                                                        HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        List<Expense> expenses = expenseService.getAllExpenses(categoryId,clientId);

        System.out.println(expenses);
        return new ResponseEntity<>(expenses,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/date/{date}")
    public ResponseEntity<ExpensePredictionDTO> getAllExpensesByCategoryAndMonth(@PathVariable("date") String date,
                                                                                 @PathVariable int categoryId,
                                                                                 HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, formatter);
        return new ResponseEntity<>(expenseService.getAllExpensesForMonth(categoryId,clientId,yearMonth),HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/{expenseId}")
    public ResponseEntity<Expense> getExpense(@PathVariable int categoryId,
                                              @PathVariable int expenseId,
                                              HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Expense expense = expenseService.getExpense(categoryId,clientId,expenseId);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable int categoryId,
                                                 @PathVariable int expenseId,
                                                 HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        expenseService.deleteExpense(categoryId,clientId,expenseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/{expenseId}")
    public ResponseEntity<Void> updateExpense(@PathVariable int categoryId,
                                                 @PathVariable int expenseId,
                                                 HttpServletRequest request,
                                                 @RequestBody Map<String,String> map) {
        int clientId = (int)request.getAttribute("clientId");
        expenseService.updateExpense(categoryId,clientId,expenseId,
                Double.parseDouble(map.get("amount")),map.get("note"));
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/{categoryId}/monthlyAverage")
    public ResponseEntity<Map<YearMonth, Double>> getMonthlyAverageExpensesPerCategory(@PathVariable int categoryId, HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Map<YearMonth, Double> monthlyAverage = expenseService.getMonthlyAverageByCategory(clientId,categoryId);
        return new ResponseEntity<>(monthlyAverage,HttpStatus.OK);
    }

    @GetMapping("/monthlyAverage")
    public ResponseEntity<Map<String,Map<YearMonth, Double>>> getMonthlyAverageExpenses( HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Map<String,Map<YearMonth, Double>> monthlyAverage = expenseService.getMonthlyAverage(clientId);
        return new ResponseEntity<>(monthlyAverage,HttpStatus.OK);
    }


    @GetMapping("/monthlyExpensePredictions")
    public ResponseEntity<List<Double>> getMonthlyPredictions(HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(expenseService.getMonthlyExpenseDataAndPredictions(clientId), HttpStatus.OK);
    }


    @GetMapping("/risingTrend")
    public ResponseEntity<MonthlyTrendDTO> getRisingTrend(HttpServletRequest request) throws IOException, InterruptedException {
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(expenseService.getTrendAnalysisData(clientId), HttpStatus.OK);
    }

    @GetMapping("/monthlyExpPerCategory")
    public ResponseEntity<Map<String, Double>> getMonthlyExpensesPerCategory(HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(expenseService.getMonthlyExpensesPerCategory(clientId), HttpStatus.OK);
    }
}
