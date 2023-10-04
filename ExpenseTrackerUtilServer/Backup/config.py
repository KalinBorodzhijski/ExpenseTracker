
# Add the TextCategorizer to the pipeline
config = {
    "model": {
        "@architectures": "spacy.TextCatEnsemble.v2",
        "linear_model": {
            "@architectures": "spacy.TextCatBOW.v2",
            "exclusive_classes": True,
            "ngram_size": 1,
            "no_output_layer": False
        },
        "tok2vec": {
            "@architectures": "spacy.Tok2Vec.v2",
            "embed": {
                "@architectures": "spacy.MultiHashEmbed.v2",
                "width": 64,
                "rows": [2000, 2000, 500, 1000, 500],
                "attrs": ["NORM", "LOWER", "PREFIX", "SUFFIX", "SHAPE"],
                "include_static_vectors": False
            },
            "encode": {
                "@architectures": "spacy.MaxoutWindowEncoder.v2",
                "width": 64,
                "window_size": 1,
                "maxout_pieces": 3,
                "depth": 2
            }
        }
    },
    "scorer": {
        "@scorers": "spacy.textcat_scorer.v2"
    },
    "threshold": 0.0
}



#"Add expense {expense-name} for ${amount} in {category-name}"
ADD_EXPENSE_PATTERN = [{"LOWER": "add"}, {"LOWER": "expense"}, {"IS_ALPHA": True, "OP": "+"},
                       {"LOWER": "for"}, {"ORTH": "$", "OP": "?"}, {"IS_DIGIT": True, "OP": "+"},
                       {"LOWER": "in"}, {"IS_ALPHA": True, "OP": "+"}]


#"Delete expense {expense-name} from {category-name} with amount ${amount}"
DELETE_EXPENSE_PATTERN = [
    {"LOWER": "delete"}, {"LOWER": "expense"}, {"IS_ALPHA": True, "OP": "+"}, {"LOWER": "from"},
    {"IS_ALPHA": True, "OP": "+"}, {"LOWER": "with"}, {"LOWER": "amount"}, {"ORTH": "$", "OP": "?"}, {"IS_DIGIT": True, "OP": "+"}]

#"Add income {income-name} ${amount}"
ADD_INCOME_PATTERN = [{"LOWER": "add"},{"LOWER": "income"},{"IS_ALPHA": True, "OP": "+"}, {"ORTH": "$", "OP": "?"}, {"IS_DIGIT": True, "OP": "+"}]
