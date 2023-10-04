package com.example.expenseit.controllers;

import com.example.expenseit.models.Income;
import com.example.expenseit.services.ClientService;
import com.example.expenseit.services.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("")
    public ResponseEntity<Map<String,String>> addToIncome(@RequestBody Map<String,String> body, HttpServletRequest request){
        Date transactionDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> map = new HashMap<>();
        int clientId = (int)request.getAttribute("clientId");
        try {
            transactionDate = dateFormat.parse(body.get("transactionDate"));
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        double newBalance = incomeService.updateBalance(clientId,Double.parseDouble(body.get("amount")),body.get("note"),transactionDate);
        map.put("new_balance",String.valueOf(newBalance));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Double> getBalance(HttpServletRequest request) {
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(incomeService.getBalance(clientId), HttpStatus.OK);
    }

    @GetMapping("income-history")
    public ResponseEntity<List<Income>> getIncomeHistory(HttpServletRequest request) {
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(incomeService.getIncomeHistory(clientId), HttpStatus.OK);
    }
}
