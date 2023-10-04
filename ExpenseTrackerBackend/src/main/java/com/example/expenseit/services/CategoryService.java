package com.example.expenseit.services;

import com.example.expenseit.errors.InvalidDataException;
import com.example.expenseit.models.Budget;
import com.example.expenseit.models.Category;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.Expense;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    CategoryRepository categoryRepository;
    ClientRepository clientRepository;

    public List<Category> getAllCategories(int clientId){
        return categoryRepository.findAllByUserId_UserId(clientId);
    }

    public Category getCategory(int clientId, int categoryId){
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,clientId);
        if(category == null)throw new InvalidDataException("Category not found!");
        return category;
    }

    public Category addCategory(int clientId, String title, String description){
        Client client = clientRepository.findByUserId(clientId);
        List<Budget> budgets = new ArrayList<>();
        budgets.add(Budget.builder().amount(0.0).budgetDate(YearMonth.now()).build());
        Category category = Category.builder()
                .title(title)
                .description(description)
                .budgets(budgets)
                .userId(client)
                .build();
        return categoryRepository.save(category);
    }

    public Category deleteCategoryWithAllTransactions(int clientId, int categoryId){
        Client client = clientRepository.findByUserId(clientId);
        List<Category> categories = categoryRepository.deleteByCategoryIdAndUserId_UserId(categoryId,clientId);
        for (Category category:categories) {
            for (Expense expense:category.getExpenses()) {
                client.setBalance(client.getBalance() + expense.getAmount());
            }
        }
        if(categories.isEmpty()) throw new InvalidDataException("There is no such category!");
        clientRepository.save(client);
        return categories.get(0);
    }

    public Category updateCategory(int clientId, int categoryId, String title, String description) {
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,clientId);
        if(category == null)throw new InvalidDataException("Category not found!");
        category.setDescription(description);
        category.setTitle(title);
        return categoryRepository.save(category);
    }

    public Double getBudget(int clientId, Integer categoryId, YearMonth month) {
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,clientId);
        if(category == null) throw new InvalidDataException("This category does not exist !");
        Optional<Budget> budget = category.getBudgets()
                .stream()
                .filter(b -> b.getBudgetDate().equals(month))
                .findFirst();
        if(budget.isPresent()){
            return budget.get().getAmount();
        }
        else return 0.0;
    }

    public void setBudgetPerCategory(int clientId, Integer categoryId, Double budget, YearMonth yearMonth){
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,clientId);
        if(budget > 0){
            Optional<Budget> budgetOpt = category.getBudgets()
                    .stream()
                    .filter(b -> Objects.equals(b.getBudgetDate(), yearMonth))
                    .findFirst();
            Budget result;
            if(budgetOpt.isPresent()){
                result = budgetOpt.get();
                result.setAmount(budget);
            } else {
                result = Budget.builder().budgetDate(yearMonth).amount(budget).build();
                category.getBudgets().add(result);
            }
            categoryRepository.save(category);
        }
    }

    public List<Category> getUnusedCategories(int clientId) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);

        return categories.stream()
                .filter(category -> category.getExpenses().stream()
                        .allMatch(expense -> expense.getTransactionDate().isBefore(threeMonthsAgo)))
                .collect(Collectors.toList());
    }
}
