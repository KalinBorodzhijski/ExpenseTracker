package com.example.expenseit.controllers;

import com.example.expenseit.models.Category;
import com.example.expenseit.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(HttpServletRequest request){
        List<Category> categories = categoryService.getAllCategories((int)request.getAttribute("clientId"));
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Map<String, String> map,HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Category category = categoryService.addCategory(clientId,map.get("title"),map.get("description"));
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Integer categoryId,HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Category category = categoryService.getCategory(clientId,categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category>  updateCategory(@PathVariable("id") Integer categoryId, @RequestBody Map<String,String> map,HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Category category = categoryService.updateCategory(clientId,categoryId,map.get("title"),map.get("description"));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") Integer categoryId,HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        Category category = categoryService.deleteCategoryWithAllTransactions(clientId,categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/{id}/budget/{date}")
    public ResponseEntity<Double> getBudget(@PathVariable("date") String date,
                                            @PathVariable("id") Integer categoryId,
                                            HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, formatter);

        return new ResponseEntity<>(categoryService.getBudget(clientId, categoryId,yearMonth),HttpStatus.OK);
    }

    @PostMapping("/{id}/budget/{date}")
    public ResponseEntity<Void> setBudget(@PathVariable("date") String date,@RequestBody Map<String, String> map,@PathVariable("id") Integer categoryId,HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, formatter);

        categoryService.setBudgetPerCategory(clientId, categoryId, Double.valueOf(map.get("amount")),yearMonth);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<Category>> getUnusedCategories(HttpServletRequest request){
        int clientId = (int)request.getAttribute("clientId");
        return new ResponseEntity<>(categoryService.getUnusedCategories(clientId),HttpStatus.OK);
    }

}
