import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-currency-convertor',
  templateUrl: './currency-convertor.component.html',
  styleUrls: ['./currency-convertor.component.css']
})
export class CurrencyConvertorComponent {

  public form: FormGroup;
  public isCurrencyConverterOpen: boolean = false;

  currencies: any[] = [
    { code: 'USD', name: 'United States Dollar' },
    { code: 'EUR', name: 'Euro' },
    { code: 'GBP', name: 'British Pound' },
    { code: 'BGN', name: 'Bulgarian Lev' },
    { code: 'JPY', name: 'Japanese Yen' },
    { code: 'CAD', name: 'Canadian Dollar' },
    { code: 'AUD', name: 'Australian Dollar' },
    { code: 'CHF', name: 'Swiss Franc' },
    { code: 'CNY', name: 'Chinese Yuan' },
    { code: 'INR', name: 'Indian Rupee' },
    { code: 'BRL', name: 'Brazilian Real' },
    { code: 'ZAR', name: 'South African Rand' },
    { code: 'RUB', name: 'Russian Ruble' },
    { code: 'MXN', name: 'Mexican Peso' },
    { code: 'SGD', name: 'Singapore Dollar' },
    { code: 'HKD', name: 'Hong Kong Dollar' },
    { code: 'BTC', name: 'Bitcoin' }
  ];

    apiKey = 't2nhNd7mzrB5cT9HpbNecQ==6CR1LuSIfJpud6lR';

    amount: number = 0;
    fromCurrency: string = '';
    toCurrency: string = '';

    response: any;
    result: number = 0;

  constructor(private http: HttpClient,private formBuilder: FormBuilder, private snackBar: MatSnackBar,) { 
    this.form = this.formBuilder.group({
      amount: ['', [Validators.required, Validators.min(1)]],
      fromCurrency: ['', Validators.required],
      toCurrency: ['', Validators.required],
    });

    this.form.valueChanges.subscribe(() => {
      this.result = 0;
    });
  }

  toggleCurrencyConverter() {
    this.isCurrencyConverterOpen = !this.isCurrencyConverterOpen;
  }
  
  //Get information about API usage on https://api-ninjas.com/profile
  convert() {
    const url = `https://api.api-ninjas.com/v1/convertcurrency?want=${this.toCurrency}&have=${this.fromCurrency}&amount=${this.amount}`;

    if (this.form.valid) {
      
      this.http.get(url, { headers: { 'X-Api-Key': this.apiKey } }).subscribe({
        error: e => {this.snackBar.open(e.error.error, 'Close', { duration: 3000 });},
        next: data => {
          this.response = data;
          this.result = this.response.new_amount;
        }});
    }

  }
}
