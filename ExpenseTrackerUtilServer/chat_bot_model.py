import spacy
from spacy.util import minibatch, compounding
import random
import time
from spacy.training import Example
from spacy.matcher import Matcher
from sklearn.model_selection import train_test_split
from training_data import TRAIN_DATA, GREETING_RESPONSES
from config import *
import matplotlib.pyplot as plt



class ChatBotModel:

    def __init__(self, load_from_disk):

        if load_from_disk:
            self.load_model_from_disk()
        else: 
            intents = [max(annotations.get("cats", {}).items(), key=lambda x: x[1])[0] for _, annotations in TRAIN_DATA]
            self.train_data, self.test_data = train_test_split(TRAIN_DATA, test_size=0.2, stratify=intents, random_state=42)
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
        best_combined_score = 0.0
        patience = 5
        no_improvement = 0
        optimizer = self.nlp.begin_training()
        optimizer.learn_rate = 0.01
        train_examples = [Example.from_dict(self.nlp(text), annotations) for text, annotations in TRAIN_DATA]

        training_losses = []
        for i in range(iterations):
            start_time = time.time()
            random.shuffle(train_examples)
            losses = {}

            batches = minibatch(train_examples, size=compounding(4.0, 32.0, 1.002))
            for batch in batches:
                self.nlp.update(batch, drop=0.3, losses=losses)
            
            end_time = time.time()
            epoch_duration = end_time - start_time

            print(f"Losses at iteration {i} - {losses}")
            print(f"Duration of epoch {i} is {epoch_duration:.2f} seconds")
            textcat_accuracy = self.evaluate_accuracy(self.test_data)
            ner_accuracy = self.evaluate_ner_accuracy(self.test_data)
            textcat_weight = 0.65
            ner_weight = 0.35
            combined_score = (textcat_accuracy * textcat_weight) + (ner_accuracy * ner_weight)

            print(f"Accuracy on test data at iteration {i} - {combined_score}")
            training_losses.append(1 - combined_score)
            if combined_score > best_combined_score:
                best_combined_score = combined_score
                no_improvement = 0
            else:
                no_improvement += 1

            if no_improvement >= patience:
                print(f"No improvement in accuracy for {patience} consecutive iterations. Stopping training.")
                break

        plt.figure(figsize=(10, 6))
        plt.plot(training_losses, label='Training Loss')
        plt.title("Training Losses")
        plt.xlabel("Iterations")
        plt.ylabel("Loss")
        plt.legend()
        plt.show()

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

    def evaluate_accuracy(self, test_data):
        correct_predictions = 0
        total_predictions = 0
        for text, annotations in test_data:
            doc = self.nlp(text)
            intent = max(doc.cats, key=doc.cats.get)
            correct_intent = max(annotations.get("cats", {}).items(), key=lambda x: x[1])[0]
            if intent == correct_intent:
                correct_predictions += 1
            total_predictions += 1

        return correct_predictions / total_predictions if total_predictions > 0 else 0
    
    def evaluate_ner_accuracy(self, test_data):
        correct_ner_predictions = 0
        total_ner_predictions = 0
        for text, annotations in test_data:
            doc = self.nlp(text)
            predicted_entities = {ent.label_: ent.text for ent in doc.ents}
            true_entities = {ent[2]: text[ent[0]:ent[1]] for ent in annotations.get("entities", [])}
            correct_ner_predictions += len(predicted_entities.keys() & true_entities.keys())
            total_ner_predictions += len(true_entities.keys())

        ner_accuracy = correct_ner_predictions / total_ner_predictions if total_ner_predictions > 0 else 0
        return ner_accuracy


    def save_model_to_disk(self):
        self.nlp.to_disk("./model")

    def load_model_from_disk(self):
        self.nlp = spacy.load("./model")

