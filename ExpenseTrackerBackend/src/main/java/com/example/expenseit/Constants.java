package com.example.expenseit;


public class Constants {
    public static final String SECRET_KEY = "ExpenseItApp";
    public static final Long JWT_TOKEN_VALID_TIME = (long) (2 * 60 * 60 * 1000);

    public static final String PYTHON_SERVICE_URL = "http://127.0.0.1:5000";

    //Entity names
    public static final String CATEGORY_NAME = "category_name";
    public static final String EXPENSE_NAME = "expense_name";
    public static final String AMOUNT = "amount";
    public static final String INCOME_NAME = "income_name";
    public static final String MONTH_NAME = "month_name";
    public static final String MONTH_ONE_NAME = "month_one_name";
    public static final String MONTH_TWO_NAME = "month_two_name";


    //Chatbot menu
    public static final String CHATBOT_MENU = "\uD83E\uDD16 ChatBot Financial Assistant Menu:\n" +
            "\n" +
            "- Create New Category: Specify the category name\nExample: \"Can you create me new category Games ?\"\n" +
            "\n" +
            "- Delete Existing Category: Specify the category name\nExample: \"Can you delete category Vehicle ?\"\n"+
            "\n" +
            "- List All Categories\nExample: \"List all my categories.\"\n" +
            "\n" +
            "- Create Expense: \n" +
            "Format: \"Add expense {expense-name} for ${amount} in {category-name}\"\n" +
            "\n" +
            "- Delete Expense:\n" +
            "Format: \"Delete expense {expense-name} from {category-name} with amount ${amount}\"\n" +
            "\n" +
            "- List All Expenses in a Category: Specify the category name\nExample: \"Show me my expenses in category Food.\"\n"+
            "\n" +
            "- Add Income:\n" +
            "Format: \"Add income {income-name} ${amount}\"\n" +
            "\n" +
            "- List All Incomes:\nExample: \"Can you show me my incomes ?\"\n" +
            "\n" +
            "- Monthly Summary: Specify the month name (e.g. June)\nExample: \"Show me my monthly summary for August.\"\n" +
            "\n" +
            "- Biggest Expense: Specify the month name (e.g. June)\nExample: \"Show me my biggest expense for March.\"\n" +
            "\n" +
            "- Suggest Savings\nExample: \"Can you suggest me how to save ?\"\n" +
            "\n" +
            "- Compare Months: Specify the month names (e.g. June)\nExample: \"Can you compare September and August ?\"\n" +
            "\n" +
            "Please follow the specified formats for the best assistance. How can I assist you today?\n";

}
