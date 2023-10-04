package com.example.expenseit.services;

import com.example.expenseit.errors.InvalidDataException;
import com.example.expenseit.models.Category;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.Expense;
import com.example.expenseit.models.dto.ExpensePredictionDTO;
import com.example.expenseit.models.dto.MonthlyTrendDTO;
import com.example.expenseit.models.dto.PredictionsResponseDTO;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import com.example.expenseit.repositories.ExpenseRepository;
import com.example.expenseit.util.ExpenseAnalysis;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.hadoop.shaded.com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.expenseit.Constants.PYTHON_SERVICE_URL;


@Service
@Transactional
@AllArgsConstructor
public class ExpenseService {

    private CategoryRepository categoryRepository;
    private ExpenseRepository expenseRepository;
    private ClientRepository clientRepository;
    private IncomeService incomeService;

    public void addNewExpense(int categoryId, int userId, double amount, String note, Date transactionDate){
        Client client = clientRepository.findByUserId(userId);
        if(client.getBalance() < amount) throw new InvalidDataException("Not enough money in the account !");
        client.setBalance(client.getBalance() - amount);
        Expense expense = Expense.builder()
                .amount(amount)
                .note(note)
                .transactionDate(LocalDateTime.ofInstant(transactionDate.toInstant(), ZoneId.systemDefault()).toLocalDate())
                .build();
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,userId);
        category.getExpenses().add(expense);
        clientRepository.save(client);
        categoryRepository.save(category);
    }

    public List<Expense> getAllExpenses(int categoryId, int userId){
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,userId);
        return category.getExpenses();
    }

    public List<Expense> getAllExpensesByUsername(int clientId) {
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);
        return categories.stream()
                .flatMap(category -> category.getExpenses().stream())
                .collect(Collectors.toList());
    }

    public Map<YearMonth, Double> getMonthlyAverageByCategory(int clientId, int categoryId){
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,clientId);
        return ExpenseAnalysis.calculateMonthlyAverageByCategory(category.getExpenses());
    }

    public Map<String, Map<YearMonth, Double>> getMonthlyAverage(int clientId) {
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);
        Map<String, Map<YearMonth, Double>> averageAmountPerCategory = new HashMap<>();

        for (Category category : categories) {
            List<Expense> expenses = category.getExpenses();
            Map<YearMonth, Double> averageAmountPerMonth = ExpenseAnalysis.calculateMonthlyAverageByCategory(expenses);
            averageAmountPerCategory.put(category.getTitle(), averageAmountPerMonth);
        }
        return averageAmountPerCategory;
    }

    public Expense getExpense(int categoryId, int userId, int expenseId){
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,userId);
        return findExpenseById(category,expenseId);
    }

    public void updateExpense(int categoryId, int userId, int expenseId, double amount, String note){
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,userId);
        Expense expense = findExpenseById(category,expenseId);
        expense.setAmount(amount);
        expense.setNote(note);
        categoryRepository.save(category);
    }

    public void deleteExpense(int categoryId, int userId, int expenseId){
        Client client = clientRepository.findByUserId(userId);
        Category category = categoryRepository.findByCategoryIdAndUserId_UserId(categoryId,userId);
        List<Expense> expenses = category.getExpenses();
        Expense expenseToDelete = expenses.stream()
                .filter(expense -> expense.getExpenseId() == expenseId)
                .findFirst()
                .orElse(null);
        if (expenseToDelete != null) {
            client.setBalance(client.getBalance() + expenseToDelete.getAmount());
        }
        expenses.remove(expenseToDelete);
        expenseRepository.deleteByExpenseId(expenseId);
        categoryRepository.save(category);
    }

    public ExpensePredictionDTO getAllExpensesForMonth(int categoryId, int clientId, YearMonth yearMonth) {
        List<Expense> expenses = getAllExpenses(categoryId, clientId);
        List<Expense> expensesPerMonth =  expenses.stream()
                .filter(expense -> YearMonth.from(expense.getTransactionDate()).equals(yearMonth))
                .collect(Collectors.toList());

        List<Double> predictions = new ArrayList<>();

        if(YearMonth.now().equals(yearMonth)){
            double expensesSoFar = expensesPerMonth.stream().mapToDouble(Expense::getAmount).sum();
            int numberOfDays = YearMonth.now().lengthOfMonth();
            int currentDay = LocalDate.now().getDayOfMonth();
            int remainingDays = numberOfDays - currentDay;
            double averageDailyExpense = (expensesSoFar / currentDay);
            for (int i = 0; i < remainingDays; i++) {
                expensesSoFar += averageDailyExpense;
                predictions.add(expensesSoFar);
            }
        }
        else predictions = null;
        return ExpensePredictionDTO.builder().monthlyExpenses(expensesPerMonth).currentMonthPrediction(predictions).build();
    }

    public MonthlyTrendDTO getTrendAnalysisData(int clientId) throws InterruptedException, IOException {
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);
        YearMonth sixMonthsAgo = YearMonth.now().minusMonths(6);

        List<Expense> expenses = categories.stream()
                .flatMap(category -> category.getExpenses().stream())
                .filter(expense -> !expense.getTransactionDate().isBefore(sixMonthsAgo.atDay(1))
                        && !expense.getTransactionDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
        Map<YearMonth, Double> monthlyExpensesSum = getMonthlyExpensesSum(expenses);
       monthlyExpensesSum = getMonthlyExpensesSumWithMissingMonths(monthlyExpensesSum);
        Map<YearMonth, Double> result = predictNextMonths(monthlyExpensesSum);
        boolean rising = isRisingTrend(result);
        MonthlyTrendDTO trendAnalysisDto = new MonthlyTrendDTO();
        trendAnalysisDto.setMonthlyExpensesSum(monthlyExpensesSum);
        trendAnalysisDto.setRisingTrend(rising);
        trendAnalysisDto.setPredictedExpenses(result);
        return trendAnalysisDto;

    }

    public Map<String, Double> getMonthlyExpensesPerCategory(int clientId) {
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);
        Map<String, Double> monthlyExpensesPerCategory = new HashMap<>();

        for (Category category: categories){
            Double sumPerMonth = getAllExpenses(category.getCategoryId(), clientId).stream()
                    .filter(expense -> YearMonth.from(expense.getTransactionDate()).equals(YearMonth.now()))
                    .mapToDouble(Expense::getAmount).sum();

            monthlyExpensesPerCategory.put(category.getTitle(),sumPerMonth);
        }
        return monthlyExpensesPerCategory;
    }

    public List<Double> getMonthlyExpenseDataAndPredictions(int clientId) {
        List<Expense> expenses = getAllExpensesByUsername(clientId);
        int numberOfDays = YearMonth.now().lengthOfMonth();
        int currentDay = LocalDate.now().getDayOfMonth();

        List<Double> result = new ArrayList<>(numberOfDays);

        List<Expense> transactionsForMonth = expenses.stream()
                .filter(transaction -> transaction.getTransactionDate().getMonthValue() == LocalDateTime.now().getMonthValue())
                .collect(Collectors.toList());

        Map<Integer, Double> sumOfAmountsByDayOfMonth = transactionsForMonth.stream()
                .collect(Collectors.groupingBy(expense -> expense.getTransactionDate().getDayOfMonth(),
                        Collectors.summingDouble(Expense::getAmount)));
        Double totalSum = 0.0;
        for (int i = 0; i < currentDay; i++){
            if(sumOfAmountsByDayOfMonth.containsKey(i+1)){
                totalSum += sumOfAmountsByDayOfMonth.get(i+1);
            }
            result.add(totalSum);
        }

        double[] prediction = predictRemainingMonth(result.stream().mapToDouble(Double::doubleValue).toArray());
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
        for (double value : prediction) {
            String formattedValue = decimalFormat.format(value);
            result.add(Double.parseDouble(formattedValue));
        }

        return result;

    }

    public double[] predictRemainingMonth(double[] expenses) {
        int daysPassed = expenses.length;
        int daysInMonth = YearMonth.now().lengthOfMonth();

        SimpleRegression regression = new SimpleRegression();

        for (int i = 0; i < daysPassed; i++) {
            regression.addData(i + 1, expenses[i]);
        }

        double[] predictions = new double[daysInMonth - daysPassed];

        for (int i = daysPassed; i < daysInMonth; i++) {
            double predictedValue = regression.predict(i + 1);
            predictions[i - daysPassed] = Math.max(predictedValue, expenses[expenses.length - 1]);
        }

        return predictions;
    }

    Map<YearMonth, Double> getMonthlyExpensesSumWithMissingMonths(Map<YearMonth, Double> monthlyExpensesSum) {

        YearMonth currentMonth = YearMonth.now();
        YearMonth sixMonthsAgo = currentMonth.minusMonths(5);
        Map<YearMonth, Double> result = new HashMap<>();
        YearMonth month = sixMonthsAgo;
        while (!month.isAfter(currentMonth)) {
            result.putIfAbsent(month, 0.0);
            month = month.plusMonths(1);
        }
        result.putAll(monthlyExpensesSum);

        return result;
    }

    Map<YearMonth, Double> getMonthlyExpensesSum(List<Expense> expenses){
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> YearMonth.from(expense.getTransactionDate()),
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    boolean isRisingTrend(Map<YearMonth, Double> predictions) {
        YearMonth firstMonth = YearMonth.now().plusMonths(1);
        YearMonth secondMonth = firstMonth.plusMonths(1);
        YearMonth thirdMonth = secondMonth.plusMonths(1);

        double firstPrediction = predictions.getOrDefault(firstMonth, 0.0);
        double secondPrediction = predictions.getOrDefault(secondMonth, 0.0);
        double thirdPrediction = predictions.getOrDefault(thirdMonth, 0.0);

        return firstPrediction < secondPrediction && secondPrediction < thirdPrediction;
    }

    Map<YearMonth, Double> predictNextMonths(Map<YearMonth, Double> expenses) throws InterruptedException, IOException {
        List<Double> monthlyExpenses = new ArrayList<>(expenses.values());
        JSONObject json = new JSONObject();
        json.put("data", monthlyExpenses);
        String jsonString = json.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PYTHON_SERVICE_URL + "/predict"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        PredictionsResponseDTO predictionResponse = gson.fromJson(response.body(), PredictionsResponseDTO.class);
        double[] predictionsArray = predictionResponse.getPredictions();
        Map<YearMonth, Double> predictions = new HashMap<>();
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        for (int i = 0; i < 3; i++) {
            predictions.put(nextMonth.plusMonths(i), predictionsArray[i]);
        }
        return predictions;
    }

    Expense findExpenseById(Category category, int expenseId){
        List<Expense> expenses = category.getExpenses();
        Optional<Expense> matchingObject = expenses.stream().filter(expense -> expense.getExpenseId() == expenseId).findFirst();
        return matchingObject.orElse(null);
    }


}


