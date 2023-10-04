# Create category intent
TRAIN_DATA = [
    ("Create new category called food", 
     {"entities": [(27, 31, 'category_name')], "cats": {"CREATE_CATEGORY": True}}),

    ("Create category called games", 
     {"entities": [(23, 28, 'category_name')], "cats": {"CREATE_CATEGORY": True}}),

    ("Create category utilities", 
     {"entities": [(16, 25, 'category_name')], "cats": {"CREATE_CATEGORY": True}}),

    ("I want new category sports", 
     {"entities": [(20, 26, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("shoes should be created", 
     {"entities": [(0, 5, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Create category for additional expenses",
     {"entities": [(20, 39, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Introduce music as a category", 
     {"entities": [(10, 15, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Why not add toys as a category?", 
     {"entities": [(12, 16, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Add a new category named books", 
     {"entities": [(25, 30, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Let's have a category for electronics", 
     {"entities": [(26, 37, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Set up a category for movies", 
     {"entities": [(22, 28, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Can we have a category named stationery?", 
     {"entities": [(29, 39, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("How about a category for pets?", 
     {"entities": [(25, 29, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("games should be a new category", 
     {"entities": [(0, 5, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Please set up a category called furniture", 
     {"entities": [(32, 41, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Think of creating a category for vehicle", 
     {"entities": [(33, 40, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("I'm suggesting tools as a category", 
     {"entities": [(15, 20, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("How about brainstorming a new category: gadgets?", 
     {"entities": [(40, 47, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Let's consider arts as a category", 
     {"entities": [(15, 19, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("outdoor gear would be a great category", 
     {"entities": [(0, 12, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Why not a category named beauty?", 
     {"entities": [(25, 31, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Let's have a new category for health", 
     {"entities": [(30, 36, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("travel could be our next category", 
     {"entities": [(0, 6, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("accessories as a category sounds good", 
     {"entities": [(0, 11, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("We need a category for clothing", 
     {"entities": [(23, 31, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("groceries is a must-have category", 
     {"entities": [(0, 9, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("I'd like to see beverages as a category", 
     {"entities": [(16, 25, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Can we introduce household as a category?", 
     {"entities": [(17, 26, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("I propose tech gadgets as our new category", 
     {"entities": [(10, 22, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("Think of cosmetics as a new category", 
     {"entities": [(9, 18, "category_name")], "cats": {"CREATE_CATEGORY": True}}),

    ("I suggest having a category for hobbies", 
     {"entities": [(32, 39, "category_name")], "cats": {"CREATE_CATEGORY": True}})
]

#Delete category intent
TRAIN_DATA += [
    ("Delete category games", 
     {"entities": [(16, 21, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Can you delete category additional ?", 
     {"entities": [(24, 34, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Can we delete the food category?", 
     {"entities": [(18, 22, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("I don't want the sports category anymore.", 
     {"entities": [(17, 23, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Remove the category named shoes.", 
     {"entities": [(26, 31, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("No need for the music category now.", 
     {"entities": [(16, 21, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Delete the toys category please.", 
     {"entities": [(11, 15, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("I'm thinking of getting rid of the books category.", 
     {"entities": [(35, 40, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Electronics category is not needed.", 
     {"entities": [(0, 11, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Do away with the movies category.", 
     {"entities": [(17, 23, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Stationery doesn't need to be a category anymore.", 
     {"entities": [(0, 10, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Pets category can be removed.", 
     {"entities": [(0, 4, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("I don't see a point in having the games category.", 
     {"entities": [(34, 39, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("It's time to remove the furniture category.", 
     {"entities": [(24, 33, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("No more need for the vehicles category.", 
     {"entities": [(21, 29, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("The tools category seems redundant.", 
     {"entities": [(4, 9, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Let's stop considering gadgets as a category.", 
     {"entities": [(23, 30, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Arts category is obsolete.", 
     {"entities": [(0, 4, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Outdoor gear category isn't relevant.", 
     {"entities": [(0, 12, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Beauty category has no more use.", 
     {"entities": [(0, 6, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Health category can be removed.", 
     {"entities": [(0, 6, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("We no longer need the travel category.", 
     {"entities": [(22, 28, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("I've decided to get rid of the accessories category.", 
     {"entities": [(31, 42, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("The additional expenses category is now redundant.", 
     {"entities": [(4, 23, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Clothing category can be discarded.", 
     {"entities": [(0, 8, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("No more groceries category please.", 
     {"entities": [(8, 17, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Remove the lifestyle category.", 
     {"entities": [(11, 20, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("There's no point in having the drinks category now.", 
     {"entities": [(31, 37, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("I've changed my mind about the hobbies category.", 
     {"entities": [(31, 38, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Delete the events category from the list.", 
     {"entities": [(11, 17, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("Remove the education category.", 
     {"entities": [(11, 20, "category_name")], "cats": {"DELETE_CATEGORY": True}}),

    ("We can do away with the services category.", 
     {"entities": [(24, 32, "category_name")], "cats": {"DELETE_CATEGORY": True}})
]

#List categories intent
TRAIN_DATA += [
    ("Can you list all the categories?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Show me all the categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("What categories do we have?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("I'd like to see all the categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Display all the category names.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Provide a list of all categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Which categories have been created?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Let's see the list of categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Could you show the category list?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("I'm interested in the list of all categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Present all the created categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Reveal all the categories.", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("How many categories do we have?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("Can I see a list of categories?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}}),

    ("What are the available categories?", 
     {"entities": [], "cats": {"LIST_CATEGORIES": True}})
]

#List categories intent
TRAIN_DATA += [
    ("Show me the expenses for food", 
     {"entities": [(25, 29, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Show me the expenses in vehicle", 
     {"entities": [(24, 31, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("List all expenses under sports", 
     {"entities": [(24, 30, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Can I see the expenses related to shoes?", 
     {"entities": [(34, 39, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Display all expenses for music", 
     {"entities": [(25, 30, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("What did I spend on toys?", 
     {"entities": [(20, 24, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Break down the expenses for books", 
     {"entities": [(28, 33, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Give me a summary of expenses in electronics", 
     {"entities": [(33, 44, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("I want to see my movies expenses", 
     {"entities": [(17, 23, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("How much did I spend in the stationery category?", 
     {"entities": [(28, 38, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Show expenses under pets category", 
     {"entities": [(20, 24, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("List down expenses for games", 
     {"entities": [(23, 28, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Provide a list of expenses in furniture", 
     {"entities": [(30, 39, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("I'd like to review expenses for vehicles", 
     {"entities": [(32, 40, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("What are my expenses on tools?", 
     {"entities": [(24, 29, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Can you show the breakdown for gadgets expenses?", 
     {"entities": [(31, 38, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("How much did I spend in arts?", 
     {"entities": [(24, 28, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Display the expenses for outdoor gear", 
     {"entities": [(25, 37, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("I need to see beauty related expenses", 
     {"entities": [(14, 20, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Get the expenses for health", 
     {"entities": [(21, 27, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Can you list travel expenses for me?", 
     {"entities": [(13, 19, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Show the list of accessories expenses", 
     {"entities": [(17, 28, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Show expenses in additional category", 
     {"entities": [(17, 27, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("How much did I spend on additional expenses?", 
     {"entities": [(24, 43, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Show me the expenses for clothing", 
     {"entities": [(25, 33, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("List all expenses in the groceries category", 
     {"entities": [(25, 34, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("I need a breakdown of all expenses in the dining out category", 
     {"entities": [(42, 52, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Show all my expenses for transportation", 
     {"entities": [(25, 39, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("List expenses in the utilities category", 
     {"entities": [(21, 30, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("Display all my expenses for entertainment", 
     {"entities": [(28, 41, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("How much did I spend in the education category?", 
     {"entities": [(28, 37, 'category_name')], "cats": {"LIST_EXPENSES": True}}),

    ("I want to see all expenses related to gifts", 
     {"entities": [(38, 43, 'category_name')], "cats": {"LIST_EXPENSES": True}})
]

#List incomes intent
TRAIN_DATA += [
    ("Show me my incomes", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("List all my incomes", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Can you display my earnings?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("I'd like to see my revenues", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("What are my incomes?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Show my income list", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("How much did I earn last month?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Let me see my incomes", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Display all incomes", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Provide a list of my earnings", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("I want to check my incomes", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("How much money did I make?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("I'd like to review my earnings", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Can I get a list of all my incomes?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Reveal my income details", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("I'm interested in my income stats", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Bring up my income data", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Please show all my revenue entries", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("What have I registered as income?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("I want a breakdown of my incomes", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Please provide my income summary", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Can you fetch my earnings?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("What does my income report say?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("I'm keen to see my earnings", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Break down my income sources", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Let's review my income data", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Give me details of my income", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Can you list my earnings for the past year?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("Display my revenue details", 
     {"entities": [], "cats": {"LIST_INCOMES": True}}),

    ("How much income did I register?", 
     {"entities": [], "cats": {"LIST_INCOMES": True}})
]


#Monthly summary intent
TRAIN_DATA += [
    ("Show me the monthly summary for January", 
     {"entities": [(32, 39, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("I'd like a report for February", 
     {"entities": [(22, 30, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Can you provide a summary for March?", 
     {"entities": [(30, 35, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("What were the expenses in April?", 
     {"entities": [(26, 31, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("How much did I spend in May?", 
     {"entities": [(24, 27, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("June monthly report please", 
     {"entities": [(0, 4, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("I need to see the summary for July", 
     {"entities": [(30, 34, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("August's monthly expenses", 
     {"entities": [(0, 6, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Give me a breakdown for September", 
     {"entities": [(24, 33, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("What did I spend in October?", 
     {"entities": [(20, 27, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("I'd like to see November's summary", 
     {"entities": [(16, 24, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Can I see the December report?", 
     {"entities": [(14, 22, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Show the report for January", 
     {"entities": [(20, 27, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Fetch the summary for February", 
     {"entities": [(22, 30, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("How did March look?", 
     {"entities": [(8, 13, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Any updates on April's expenses?", 
     {"entities": [(15, 20, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("May expenses breakdown", 
     {"entities": [(0, 3, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),

    ("Monthly report for June, please", 
     {"entities": [(19, 23, 'month_name')], "cats": {"MONTHLY_SUMMARY": True}}),
]


#Biggest expense intent
TRAIN_DATA += [
    ("What was my biggest expense in January?", 
     {"entities": [(31, 38, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Tell me the largest expenditure in February", 
     {"entities": [(35, 43, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("How much did I spend the most in March?", 
     {"entities": [(33, 38, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Which purchase cost me the most in April?", 
     {"entities": [(35, 40, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("What cost the most in May?", 
     {"entities": [(22, 25, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Reveal the highest expenditure in June", 
     {"entities": [(34, 38, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("I want to know my priciest buy in July", 
     {"entities": [(34, 38, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Which expense was the largest in August?", 
     {"entities": [(33, 39, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Show the top expense for September", 
     {"entities": [(25, 34, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Can you display the max expense in October?", 
     {"entities": [(35, 42, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("I'd like to view the peak expenditure in November", 
     {"entities": [(41, 49, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Find out my top expense for December", 
     {"entities": [(28, 36, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("I'm curious about my heftiest spend in June", 
     {"entities": [(39, 43, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("Determine the record expense in May for me", 
     {"entities": [(32, 35, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),

    ("How much was my steepest purchase in July?", 
     {"entities": [(37, 41, 'month_name')], "cats": {"BIGGEST_EXPENSE": True}}),
]


#Suggest savings intent
TRAIN_DATA += [
    ("Can you suggest ways to save?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),
    
    ("What are some saving tips?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("How can I reduce my expenses?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Give me some suggestions to save more.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("I'd like to save money, any tips?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Can you help me cut down on my spending?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Tell me ways to save up.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("I need to save for a trip. Any advice?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("How can I budget to save more?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Show me some methods to save.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("I want to save up for a car, help?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Are there any good saving strategies?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Share some money-saving tactics.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Suggest how I can keep more of my money.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Can you offer any financial tips?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Guide me on saving more.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("I'm looking to save. Any ideas?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Help me strategize to save money.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Any pointers on increasing my savings?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Provide me some saving insights.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("I want to reduce costs. Tips?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("How do I go about saving more?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Give me guidance on budgeting and saving.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("I'd like to put away more money. Suggestions?", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}}),

    ("Advise me on better saving habits.", 
     {"entities": [], "cats": {"SUGGEST_SAVING": True}})
]

#Compare months intent
TRAIN_DATA += [
    ("How did January compare to December?", 
     {"entities": [(8, 15, 'month_one_name'), (27, 35, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("Can you show a comparison between February and November?", 
     {"entities": [(34, 42, 'month_one_name'), (47, 55, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("Is March doing better than October?", 
     {"entities": [(3, 8, 'month_one_name'), (27, 34, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("April vs September spending?", 
     {"entities": [(0, 5, 'month_one_name'), (9, 18, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("I'd like to see May versus August", 
     {"entities": [(16, 19, 'month_one_name'), (27, 33, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("Are there differences in expenses from June to July?", 
     {"entities": [(39, 43, 'month_one_name'), (47, 51, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("I'm curious about the differences between July and June", 
     {"entities": [(42, 46, 'month_one_name'), (51, 55, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("August compared to May?", 
     {"entities": [(0, 6, 'month_one_name'), (19, 22, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("Let's contrast September and April", 
     {"entities": [(15, 24, 'month_one_name'), (29, 34, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("Can we look at October against March?", 
     {"entities": [(15, 22, 'month_one_name'), (31, 36, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("Comparison of November with February?", 
     {"entities": [(14, 22, 'month_one_name'), (28, 36, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}}),

    ("I want to compare the expenses of December to those of January", 
     {"entities": [(34, 42, 'month_one_name'), (55, 62, 'month_two_name')], "cats": {"COMPARE_MONTHS": True}})
]


# Greeting intents
TRAIN_DATA += [
    ("Hello", {"entities": [], "cats": {"GREETING": True}}),
    ("Hi there", {"entities": [], "cats": {"GREETING": True}}),
    ("Hey", {"entities": [], "cats": {"GREETING": True}}),
    ("Good morning", {"entities": [], "cats": {"GREETING": True}}),
    ("Good afternoon", {"entities": [], "cats": {"GREETING": True}}),
    ("Good evening", {"entities": [], "cats": {"GREETING": True}}),
    ("Heya", {"entities": [], "cats": {"GREETING": True}}),
    ("Hello there", {"entities": [], "cats": {"GREETING": True}}),
    ("Hi", {"entities": [], "cats": {"GREETING": True}}),
    ("Greetings", {"entities": [], "cats": {"GREETING": True}}),
    ("Salutations", {"entities": [], "cats": {"GREETING": True}}),
    ("Yo", {"entities": [], "cats": {"GREETING": True}}),
    ("What's up?", {"entities": [], "cats": {"GREETING": True}}),
    ("Howdy", {"entities": [], "cats": {"GREETING": True}}),
    ("Hey there", {"entities": [], "cats": {"GREETING": True}}),
    ("Hiya", {"entities": [], "cats": {"GREETING": True}}),
    ("Heyo", {"entities": [], "cats": {"GREETING": True}}),
    ("Good day", {"entities": [], "cats": {"GREETING": True}}),
    ("Hola", {"entities": [], "cats": {"GREETING": True}}),
    ("Sup?", {"entities": [], "cats": {"GREETING": True}}),
    ("How's it going?", {"entities": [], "cats": {"GREETING": True}}),
    ("What's new?", {"entities": [], "cats": {"GREETING": True}}),
    ("How are you doing?", {"entities": [], "cats": {"GREETING": True}}),
    ("Hey, how are you?", {"entities": [], "cats": {"GREETING": True}}),
    ("Hi, how's everything?", {"entities": [], "cats": {"GREETING": True}})
]

#Ask functions intent
TRAIN_DATA += [
    ("What can you do?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Tell me your features.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("List all your capabilities.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Show me what you can do.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Can you explain your functionalities?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("How can you assist me?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("What services do you offer?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("How do you work?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Describe your features to me.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("What tasks can you perform?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Break down your functionalities for me.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Give me a summary of your capabilities.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Explain your main features.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Help me understand your services.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("What's in your feature list?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("I want to know your abilities.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Guide me through what you can do.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Show me a list of your tasks.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("What all features do you have?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Can you list down your main tasks?", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("I'm curious about your features.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Tell me more about your abilities.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Demonstrate your functionalities.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Highlight your main features.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}}),

    ("Walk me through your services.", 
     {"entities": [], "cats": {"ASK_FUNCTIONS": True}})
]



# Greeting responses
GREETING_RESPONSES = [
    "Hello!",
    "Hey there!",
    "Good to see you.",
    "Hi, how can I assist you today?",
    "Hello! How can I help you?",
    "Hey! How's it going?",
    "Greetings! What can I do for you?",
    "Hey! Need any help?",
    "Hello! Let me know if you need anything."
]
