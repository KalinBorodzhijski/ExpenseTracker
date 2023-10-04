package com.example.expenseit.services;

import com.example.expenseit.models.*;
import com.example.expenseit.models.ENUMS.Intent;
import com.example.expenseit.models.dto.InfoChatbotResponse;
import com.example.expenseit.models.dto.IntentChatbotResponse;
import com.example.expenseit.models.dto.UserChatbotRequest;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import com.example.expenseit.util.IntentHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.expenseit.Constants.*;
import static com.example.expenseit.models.ENUMS.Intent.*;

@Service
public class ChatbotService {

    private static final String CHATBOT_URL = PYTHON_SERVICE_URL + "/chatbot";

    private final Map<Intent, IntentHandler> intentHandlers = new HashMap<>();
    private ConcurrentHashMap<Integer, IntentChatbotResponse> verificationChatbot = new ConcurrentHashMap<>();


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final CategoryService categoryService;

    public ChatbotService(RestTemplate restTemplate, CategoryRepository categoryRepository,
                          ClientRepository clientRepository, ExpenseService expenseService,
                          IncomeService incomeService, CategoryService categoryService) {
        this.restTemplate = restTemplate;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.categoryService = categoryService;
    }

    @PostConstruct
    public void init() {
        intentHandlers.put(CREATE_CATEGORY, this::handleCreateCategory);
        intentHandlers.put(DELETE_CATEGORY, this::handleDeleteCategory);
        intentHandlers.put(LIST_CATEGORIES, this::handleListCategories);
        intentHandlers.put(ADD_EXPENSE, this::handleAddExpense);
        intentHandlers.put(DELETE_EXPENSE, this::handleDeleteExpense);
        intentHandlers.put(LIST_EXPENSES, this::handleListExpenses);
        intentHandlers.put(ADD_INCOME, this::handleAddIncome);
        intentHandlers.put(LIST_INCOMES, this::handleListIncomes);
        intentHandlers.put(MONTHLY_SUMMARY, this::handleMonthlySummary);
        intentHandlers.put(BIGGEST_EXPENSE, this::handleBiggestExpense);
        intentHandlers.put(SUGGEST_SAVING, this::handleSuggestSaving);
        intentHandlers.put(COMPARE_MONTHS, this::handleCompareMonths);
        intentHandlers.put(GREETING, this::handleGreet);
        intentHandlers.put(ASK_FUNCTIONS, this::handleAskFunctions);
    }

    public Map<String,String> handleChatBotCommunication(String message, int clientId){
        HashMap<String, String > result = new HashMap<>();
        if(verificationChatbot.containsKey(clientId)){
            JSONObject jsonObject = new JSONObject(message);
            String parsedMessage = jsonObject.get("message").toString();
            if(parsedMessage.equalsIgnoreCase("yes")){
                String value = handleIncomingIntents(clientId, verificationChatbot.get(clientId));
                result.put("response", value);
            }
            else{
                result.put("response", "Operation canceled.");
            }
            verificationChatbot.remove(clientId);
            return result;
        }else{
            return getResponse(message,clientId);
        }
    }

    private Map<String,String> getResponse(String message, int clientId) {
        Map<String, String> returnValue = new HashMap<>();
        Object response = getChatbotResponse(message);
        if(response instanceof IntentChatbotResponse){
            String result = "Are you sure that you want to execute "
                    + ((IntentChatbotResponse) response).getIntent().toLowerCase().replace("_"," ")
                    + " ?\nType \"yes\" to continue and \"no\" to abort the request.";
            verificationChatbot.put(clientId,(IntentChatbotResponse)response);
            returnValue.put("response",result);
        }
        else if(response instanceof InfoChatbotResponse){
            returnValue.put("response",((InfoChatbotResponse) response).getInformation());
        }

        return returnValue;
    }

    Object getChatbotResponse(String message) {
        UserChatbotRequest request = new UserChatbotRequest(message);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(CHATBOT_URL, HttpMethod.POST, new HttpEntity<>(request), String.class);
            String responseBody = responseEntity.getBody();
            if(responseBody != null){
                if (responseBody.contains("\"information\"")) {
                    return objectMapper.readValue(responseBody, InfoChatbotResponse.class);
                }
                return objectMapper.readValue(responseBody, IntentChatbotResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String handleIncomingIntents(int clientId, IntentChatbotResponse chatbotResponse) {
        String result;
        Intent intent = Intent.valueOf(chatbotResponse.getIntent());
        IntentHandler handler = intentHandlers.get(intent);

        if (handler != null) {
            result = handler.handle(chatbotResponse.getEntities(), clientId);
        } else {
            throw new IllegalArgumentException("Invalid intent type: " + chatbotResponse.getIntent());
        }
        return result;
    }

    String handleCompareMonths(Map<String, String> entities, int clientId) {
        Client client = clientRepository.findByUserId(clientId);
        String monthOneName = entities.get(MONTH_ONE_NAME);
        String monthTwoName = entities.get(MONTH_TWO_NAME);
        Month monthOne = Month.valueOf(entities.get(MONTH_ONE_NAME).toUpperCase());
        Month monthTwo = Month.valueOf(entities.get(MONTH_TWO_NAME).toUpperCase());

        double expenseMonthOne = expenseService.getAllExpensesByUsername(clientId)
                .stream()
                .filter(expense -> expense.getTransactionDate().getMonth() == monthOne)
                .mapToDouble(Expense::getAmount)
                .sum();
        double expenseMonthTwo = expenseService.getAllExpensesByUsername(clientId)
                .stream()
                .filter(expense -> expense.getTransactionDate().getMonth() == monthTwo)
                .mapToDouble(Expense::getAmount)
                .sum();

        double incomeMonthOne = client.getIncomeHistory()
                .stream()
                .filter(income -> income.getTransactionDate().getMonth() == monthOne)
                .mapToDouble(Income::getAmount)
                .sum();
        double incomeMonthTwo = client.getIncomeHistory()
                .stream()
                .filter(income -> income.getTransactionDate().getMonth() == monthTwo)
                .mapToDouble(Income::getAmount)
                .sum();

        StringBuilder response = new StringBuilder();
        response.append("Comparing months ").append(monthOneName).append(" and ").append(monthTwoName).append(". ");

        if (expenseMonthOne > expenseMonthTwo) {
            response.append("You spent more in ").append(monthOneName).append("(").append(expenseMonthOne).append(")").append(". ");
        } else if (expenseMonthOne < expenseMonthTwo) {
            response.append("You spent more in ").append(monthTwoName).append("(").append(expenseMonthTwo).append(")").append(". ");
        } else {
            response.append("Your spending was about the same in both months").append("(").append(expenseMonthTwo).append(").");
        }

        if (incomeMonthOne > incomeMonthTwo) {
            response.append("You earned more in ").append(monthOneName).append("(").append(incomeMonthOne).append(")").append(". ");
        } else if (incomeMonthOne < incomeMonthTwo) {
            response.append("You earned more in ").append(monthTwoName).append("(").append(incomeMonthTwo).append(")").append(". ");
        } else {
            response.append("Your income was about the same in both months").append("(").append(incomeMonthOne).append(").");
        }

        return response.toString();
    }

    private String handleSuggestSaving(Map<String, String> entities, int clientId) {
        Client client = clientRepository.findByUserId(clientId);
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);
        LocalDate now = LocalDate.now();
        List<Income> incomes = client.getIncomeHistory().stream()
                .filter(income -> {
                    LocalDate transactionDate = income.getTransactionDate();
                    return transactionDate.getMonthValue() == now.getMonthValue()
                            && transactionDate.getYear() == now.getYear();
                })
                .collect(Collectors.toList());

        List<Expense> expenses = expenseService.getAllExpensesByUsername(clientId).stream()
                .filter(expense -> {
                    LocalDate transactionDate = expense.getTransactionDate();
                    return transactionDate.getMonthValue() == now.getMonthValue()
                            && transactionDate.getYear() == now.getYear();
                })
                .collect(Collectors.toList());

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double savedAmount = totalIncome - totalExpenses;

        Map<String, Double> expensesByCategory = new HashMap<>();
        for (Category category : categories) {
            double totalExpenseForCategory = category.getExpenses().stream().filter(expense -> {
                LocalDate transactionDate = expense.getTransactionDate();
                return transactionDate.getMonthValue() == now.getMonthValue()
                        && transactionDate.getYear() == now.getYear();
            }).mapToDouble(Expense::getAmount).sum();
            expensesByCategory.put(category.getTitle(), totalExpenseForCategory);
        }

        // Find the category with the highest expense
        Map.Entry<String, Double> maxCategory = null;
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            if (maxCategory == null || entry.getValue().compareTo(maxCategory.getValue()) > 0) {
                maxCategory = entry;
            }
        }

        StringBuilder response = new StringBuilder();

        if (savedAmount < 0) {
            response.append("You are spending more than your income. Consider reviewing your expenses.");
        } else if (savedAmount < 0.2 * totalIncome) {
            response.append("You're saving less than 20% of your income. It's advised to save at least 20%.");
        } else {
            response.append("Great job! You're saving a good portion of your income.");
        }

        if (maxCategory != null) {
            response.append(" Your highest spending is on ").append(maxCategory.getKey())
                    .append(" with ").append(maxCategory.getValue()).append(" amount. Consider reviewing this category.");
        }

        // Expense vs. Income Ratio
        double ratio = totalExpenses / totalIncome;
        if (ratio > 0.8) {
            response.append("You're spending over 80% of your income. It might be worthwhile to evaluate where cuts can be made. ");
        }

        return response.toString();
    }

    private String handleBiggestExpense(Map<String, String> entities, int clientId) {
        List<Category> categories = categoryRepository.findAllByUserId_UserId(clientId);
        Month month = Month.valueOf(entities.get(MONTH_NAME).toUpperCase());

        Optional<Expense> biggestExpense = categories.stream()
                .flatMap(category -> category.getExpenses().stream())
                .filter(expense -> expense.getTransactionDate().getMonth() == month)
                .max(Comparator.comparingDouble(Expense::getAmount));

        if(biggestExpense.isPresent()){
            return String.format("Biggest expense for month %s is %s with value %f.",
                    entities.get(MONTH_NAME), biggestExpense.get().getNote(),biggestExpense.get().getAmount());
        }
        else
            return String.format("No expense for month %s found.", entities.get(MONTH_NAME));
    }

    private String handleMonthlySummary(Map<String, String> entities, int clientId) {
        Client client = clientRepository.findByUserId(clientId);
        Month month = Month.valueOf(entities.get(MONTH_NAME).toUpperCase());
        double totalIncome = client.getIncomeHistory()
                .stream()
                .filter(income -> income.getTransactionDate().getMonth() == month)
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = expenseService.getAllExpensesByUsername(clientId)
                .stream()
                .filter(expense -> expense.getTransactionDate().getMonth() == month)
                .mapToDouble(Expense::getAmount)
                .sum();

        return String.format("For month %s you spend %s and your income was %s.", entities.get(MONTH_NAME), totalExpense, totalIncome);
    }

    private String handleListIncomes(Map<String, String> entities, int clientId) {
        List<Income> incomes = incomeService.getIncomeHistory(clientId);
        StringBuilder result = new StringBuilder("You have following incomes:\n");
        for(Income income: incomes){
            result.append(" - ").append(income.getNote()).append(", amount - ").append(income.getAmount()).append("\n");
        }
        return result.toString();
    }

    private String handleAddIncome(Map<String, String> entities, int clientId) {
        incomeService.updateBalance(clientId,
                Double.parseDouble(entities.get(AMOUNT)),
                entities.get(INCOME_NAME),Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return "Income created: " + entities.get(INCOME_NAME) + " " + entities.get(AMOUNT);
    }

    private String handleListExpenses(Map<String, String> entities, int clientId) {
        Category category = categoryRepository.findByTitleAndUserId_UserId(entities.get(CATEGORY_NAME), clientId);
        StringBuilder result = new StringBuilder("You have following expenses in " + entities.get(CATEGORY_NAME) + ":\n");
        if(category == null) return "Invalid category !";
        for (Expense expense: category.getExpenses()){
            result.append(" - ").append(expense.getNote()).append(", amount - ").append(expense.getAmount());
        }
        return result.toString();
    }

    private String handleDeleteExpense(Map<String, String> entities, int clientId) {
        int categoryId = 0;
        Expense expenseToDelete = null;
        Category category = categoryRepository.findByTitleAndUserId_UserId(entities.get(CATEGORY_NAME), clientId);

        for (Expense expense: category.getExpenses()){
            if((expense.getAmount() == Double.parseDouble(entities.get(AMOUNT))) &&
                expense.getNote().equals(entities.get(EXPENSE_NAME))){
                categoryId = category.getCategoryId();
                expenseToDelete = expense;
            }
        }

        if(expenseToDelete != null){
            expenseService.deleteExpense(categoryId,clientId,expenseToDelete.getExpenseId());
        }
        else{
            return "No such category or expense !";
        }
        return "Expense deleted with values: " + entities.get(CATEGORY_NAME) + " "
                + entities.get(EXPENSE_NAME) + " "
                + entities.get(AMOUNT);
    }

    private String handleAddExpense(Map<String, String> entities, int clientId) {
        Category category = categoryRepository.findByTitleAndUserId_UserId(entities.get(CATEGORY_NAME), clientId);
        if(category != null){
            expenseService.addNewExpense(category.getCategoryId(),
                    clientId,Double.parseDouble(entities.get(AMOUNT)),
                    entities.get(EXPENSE_NAME),
                    Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            return "Expense created with values: " + entities.get(CATEGORY_NAME) + " "
                    + entities.get(EXPENSE_NAME) + " "
                    + entities.get(AMOUNT);
        }
        else return "Category " + entities.get(CATEGORY_NAME) + " not found !";

    }

    private String handleListCategories(Map<String, String> entities, int clientId) {
        List<Category> categoryList = categoryRepository.findAllByUserId_UserId(clientId);
        StringBuilder response = new StringBuilder("You have following categories:\n");
        for (Category category:categoryList) {
            response.append(" - ").append(category.getTitle()).append("\n");
        }
        return response.toString();
    }

    private String handleDeleteCategory(Map<String, String> entities, int clientId) {
        String title = entities.get(CATEGORY_NAME);
        Category category = categoryRepository.findByTitleAndUserId_UserId(title,clientId);
        if(category == null) return "Category " + title + " does not exist";
        categoryService.deleteCategoryWithAllTransactions(clientId, category.getCategoryId());
        return "Category " + entities.get(CATEGORY_NAME) + " deleted";
    }

    private String handleCreateCategory(Map<String, String> entities, int clientId) {
        String title = entities.get(CATEGORY_NAME);
        Category category = categoryRepository.findByTitleAndUserId_UserId(title,clientId);
        if(category == null){
            Client client = clientRepository.findByUserId(clientId);
            List<Budget> budgets = new ArrayList<>();
            budgets.add(Budget.builder().budgetDate(YearMonth.now()).amount(0.0).build());
            category = Category.builder().title(title).description(title + " expenses")
                    .budgets(budgets)
                    .userId(client).build();
            categoryRepository.save(category);
            return "Category " + title + " created";
        }

        return "Category with name " + title + " exists";
    }

    private String handleGreet(Map<String, String> entities, int clientId) {
        return entities.get("greet");
    }

    private String handleAskFunctions(Map<String, String> entities, int clientId) {
        return CHATBOT_MENU;
    }

}
