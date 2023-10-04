from flask import Flask, request, jsonify
import json
import pmdarima as pm
from chat_bot_model import ChatBotModel
from receipt_scanner import ReceiptScanner


app = Flask(__name__)
chat_bot = ChatBotModel(True)
scanner = ReceiptScanner()

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

        model = pm.auto_arima(data, seasonal=False, trace=True,
                      error_action='ignore', suppress_warnings=True, 
                      stepwise=True)

        forecast = model.predict(n_periods=3)
        
        return jsonify(predictions=list(forecast))
    except Exception as e:
        return jsonify(error=str(e)), 400

if __name__ == "__main__":
    app.run(port=5000)