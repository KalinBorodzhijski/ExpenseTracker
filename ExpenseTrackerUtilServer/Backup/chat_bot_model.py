import spacy
from spacy.util import minibatch, compounding
import random
from spacy.training import Example
from spacy.matcher import Matcher
from training_data import TRAIN_DATA, GREETING_RESPONSES
from config import *

class ChatBotModel:

    def __init__(self, load_from_disk):

        if load_from_disk:
            self.load_model_from_disk()
        else: 
            self.init_model()
            self.train_model(80)
            self.save_model_to_disk()

        self.matcher = Matcher(self.nlp.vocab)

        self.matcher.add("DELETE_EXPENSE", [DELETE_EXPENSE_PATTERN])
        self.matcher.add("ADD_EXPENSE", [ADD_EXPENSE_PATTERN])
        self.matcher.add("ADD_INCOME",[ADD_INCOME_PATTERN])

    def init_model(self):
        self.nlp = spacy.blank("en")
        self.nlp.add_pipe("ner")
        self.nlp.add_pipe("textcat", config=config, last=True)

        self.ner = self.nlp.get_pipe("ner")
        for _, annotations in TRAIN_DATA:
            for ent in annotations.get("entities"):
                self.ner.add_label(ent[2])

        self.textcat = self.nlp.get_pipe("textcat")
        for _, annotations in TRAIN_DATA:
            for label, value in annotations.get("cats", {}).items():
                self.textcat.add_label(label)


    def train_model(self, iterations):
        previous_loss = None
        convergence_threshold = 0.001
        optimizer = self.nlp.begin_training()
        optimizer.learn_rate = 0.01
        train_examples = [Example.from_dict(self.nlp(text), annotations) for text, annotations in TRAIN_DATA]
        for i in range(iterations):
            random.shuffle(train_examples)
            losses = {}

            batches = minibatch(train_examples, size=compounding(2.0, 64.0, 1.005))
            for batch in batches:
                self.nlp.update(batch, drop=0.3, losses=losses)
            
            print(f"Losses at iteration {i} - {losses}")

            total_loss = sum(losses.values())
            
            # Check for convergence
            if previous_loss is not None:
                loss_difference = abs(total_loss - previous_loss)
                
                if loss_difference < convergence_threshold:
                    print(f"Model is converging at iteration {i}. Stopping training.")
                    break

            previous_loss = total_loss

    def get_intent_and_entity(self, input_text):

        """Predict the intent and entity for the given text."""
        self.doc = self.nlp(input_text)
        self.handle_patterns()
        
        entities = {ent.label_: ent.text for ent in self.doc.ents}
        intent = max(self.doc.cats, key=self.doc.cats.get)
        print(entities,intent)
        return self.doc, intent, entities
    
    def handle_missing_entities(self,intent, entities):
        """Handle missing entities based on intent."""

        required_entities_per_intent = {
            "CREATE_CATEGORY": ["category_name"],
            "DELETE_CATEGORY": ["category_name"],
            "LIST_CATEGORIES": [],
            "ADD_EXPENSE": ["category_name", "expense_name", "amount"],
            "DELETE_EXPENSE": ["category_name", "expense_name", "amount"],
            "LIST_EXPENSES": ["category_name"],
            "ADD_INCOME": ["income_name","amount"],
            "LIST_INCOMES": [],
            "MONTHLY_SUMMARY": ["month_name"],
            "BIGGEST_EXPENSE": ["month_name"],
            "SUGGEST_SAVING": [],
            "COMPARE_MONTHS": ["month_one_name","month_two_name"]

        }

        required_entities = required_entities_per_intent.get(intent, [])

        missing_entities = [ent for ent in required_entities if ent not in entities]

        if missing_entities:
            return (f"Invalid fields, please try again: {', '.join(ent.replace('_', ' ') for ent in missing_entities)}")

        return ""


    def process_message(self, input_text):
        doc, intent, entities = self.get_intent_and_entity(input_text)
        confidence_threshold = 0.95
        info = self.handle_missing_entities(intent, entities)

        if intent == "GREETING":
            entities["greet"] = random.choice(GREETING_RESPONSES)

        if doc.cats[intent] < confidence_threshold:
            info = "I'm not quite sure about that. Could you please rephrase or provide more details?"
        print(doc.cats[intent])
        print(intent)
        if info != "" and info is not None:
            intent = None 
            entities = None

        return info, intent, entities

    def handle_patterns(self):
        
        matches = self.matcher(self.doc)
        
        for match_id, start, end in matches:
            match_label = self.nlp.vocab.strings[match_id]
            print(match_label)
            if match_label == "ADD_EXPENSE":
                self.doc.cats = {key: 0.0 for key in self.doc.cats}
                self.doc.cats["ADD_EXPENSE"] = 1.0
                for_pos = [token.i for token in self.doc if token.lower_ == 'for'][0]
                dollar_pos = [token.i for token in self.doc if token.text == '$'][0]
                in_pos = [token.i for token in self.doc if token.lower_ == 'in'][0]
                
                expense_name = spacy.tokens.Span(self.doc, start + 2, for_pos, label="expense_name")
                amount = spacy.tokens.Span(self.doc, dollar_pos + 1, in_pos, label="amount")
                category_name = spacy.tokens.Span(self.doc, in_pos + 1, end, label="category_name")
                
                self.doc.ents = [expense_name, amount, category_name]
                self.doc.ents = tuple(self.doc.ents)

            elif match_label == "DELETE_EXPENSE":
                self.doc.cats = {key: 0.0 for key in self.doc.cats}
                self.doc.cats["DELETE_EXPENSE"] = 1.0
                
                from_pos = [token.i for token in self.doc if token.text.lower() == 'from'][0]
                with_pos = [token.i for token in self.doc if token.text.lower() == 'with'][0]
                dollar_pos = [token.i for token in self.doc if token.text == '$'][0]
                
                expense_name = spacy.tokens.Span(self.doc, start + 2, from_pos, label="expense_name")
                category_name = spacy.tokens.Span(self.doc, from_pos + 1, with_pos, label="category_name")
                amount = spacy.tokens.Span(self.doc, dollar_pos + 1, end, label="amount")
                
                self.doc.ents = [expense_name, category_name, amount]
                self.doc.ents = tuple(self.doc.ents)
                
            elif match_label == "ADD_INCOME":
                self.doc.cats = {key: 0.0 for key in self.doc.cats}
                self.doc.cats["ADD_INCOME"] = 1.0

                income_name_start = start + 2
                income_name_end = self.doc[end-3].i if self.doc[end-2].text == '$' else self.doc[end-2].i
                income_name = spacy.tokens.Span(self.doc, income_name_start, income_name_end + 1, label="income_name")
                amount_start = income_name_end + 2 if self.doc[end-2].text == '$' else income_name_end + 1
                amount = spacy.tokens.Span(self.doc, amount_start, end, label="amount")
                
                self.doc.ents = [income_name, amount]
                self.doc.ents = tuple(self.doc.ents)

    def save_model_to_disk(self):
        self.nlp.to_disk("./model")

    def load_model_from_disk(self):
        self.nlp = spacy.load("./model")
