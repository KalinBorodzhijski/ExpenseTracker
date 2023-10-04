import cv2
import pytesseract
import re
import numpy as np


class ReceiptScanner:

    def preprocess_image(self, image_stream):
        image_np = np.frombuffer(image_stream.read(), np.uint8)
        image = cv2.imdecode(image_np, cv2.IMREAD_COLOR)
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        smoothed = cv2.GaussianBlur(gray, (5, 5), 0)
        return smoothed

    def extract_text_from_image(self, image):
        config = r'--oem 3 --psm 6 -l bul+eng'
        return pytesseract.image_to_string(image, config=config)

    def extract_data(self, text):
        date_pattern = r"(0[1-9]|[12][0-9]|3[01])[-.\s](0[1-9]|1[0-2])[-.\s](19|20)\d{2}"
        date = re.search(date_pattern, text)
        date = date.group(0) if date else "Date not found"

        lowercase_text = text.lower()
        total_pattern = r"об[шщ]а\s*сума\s*:?\s*(\d+\.\s*\d{2})"
        total_pattern_2 = r"в\s*брой\s*лв\s*(\d+\.\s*\d{2})"
        total = re.search(total_pattern, lowercase_text)
        total_2 = re.search(total_pattern_2, lowercase_text)
        total_result = "Total amount not found"
        if(total_2 is not None): total_result = total_2.group(1).replace(" ","")
        if(total is not None): total_result = total.group(1).replace(" ","")

        product_pattern = r"([^\d\n<|]+)\s+(\d+\.\d{2})\sБ"
        products_matches = re.findall(product_pattern, text)
        products = ', '.join([f"{match[0].strip()}" for match in products_matches]) if products_matches else "Products not found"

        return {"date": date, "total": total_result, "products": products}

    def process(self, image_path):
        processed_image = self.preprocess_image(image_path)
        extracted_text = self.extract_text_from_image(processed_image)
        print(extracted_text)
        data = self.extract_data(extracted_text)
        return data