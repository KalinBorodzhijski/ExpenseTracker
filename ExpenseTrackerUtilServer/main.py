from flask import Flask, request, jsonify
import json
from chat_bot_model import ChatBotModel
from receipt_scanner import ReceiptScanner


app = Flask(__name__)
chat_bot = ChatBotModel(True)
scanner = ReceiptScanner()
custom_weights = [1, 2, 3, 4, 5, 5, 5]


def weighted_moving_average(expenses, num_predictions, custom_weights):
    predictions = []

    for _ in range(num_predictions):
        if len(expenses) > len(custom_weights):
            recent_expenses = expenses[-len(custom_weights):]
        else:
            recent_expenses = expenses
        aligned_weights = custom_weights[-len(recent_expenses):]
        weighted_sum = sum(expense * weight for expense, weight in zip(recent_expenses, aligned_weights))
        total_weights = sum(aligned_weights)
        wma = weighted_sum / total_weights
        expenses.append(wma)
        if len(expenses) > len(custom_weights):
            expenses.pop(0)

        predictions.append(wma)

    return predictions

@app.route('/chatbot', methods=['POST'])
def chatbot_response():
    try:
        data = request.get_json()
        inner_json = json.loads(data['message'])
        input_text = inner_json['message']
        
        info, intent, entities = chat_bot.process_message(input_text)

        if intent is not None and entities is not None:
            return jsonify({"intent": intent, "entities": entities})
        else:
            return jsonify({"information": info})
        
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/scan_receipt', methods=['POST'])
def scan_receipt():
    file = request.files['file']
    if not file:
        return jsonify({"error": "no file"}), 400
    
    scanner = ReceiptScanner()
    data = scanner.process(file.stream)
    print(data)
    return jsonify(data)


@app.route('/predict', methods=['POST'])
def predict():
    try:
        if not request.is_json:
            return jsonify(error="Request does not contain valid JSON"), 400

        data = request.json.get('data')
        
        if not data:
            return jsonify(error="Missing 'data' key in request or data is empty"), 400
        
        forecast = weighted_moving_average(data, 3, custom_weights)
        print(forecast)
        return jsonify(predictions=forecast)
    except Exception as e:
        return jsonify(error=str(e)), 400


if __name__ == "__main__":
    app.run(port=5000)

