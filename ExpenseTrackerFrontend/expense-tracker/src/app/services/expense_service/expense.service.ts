import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Expense } from 'src/app/model/expense';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {

  baseURL = "http://localhost:4040/expenses";

  constructor(private http: HttpClient) { }

  getExpensesByCategory(categoryId: number, month: number, year: number): Observable<any>{
    month = Number.parseInt(month.toString()) + 1;
    year = Number.parseInt(year.toString());
    const yearMonth = `${year.toString()}${month.toString().padStart(2, '0')}`;
    return this.http.get(`${this.baseURL}/${categoryId}/date/${yearMonth}`);
  }

  getMonthlyExpensesPerCategory(): Observable<any>{
    return this.http.get(`${this.baseURL}/monthlyExpPerCategory`);
  }

  deleteExpense(expense: Expense,categoryId: number){
    return this.http.delete(`${this.baseURL}/${categoryId}/${expense.expenseId}`);
  }

  addExpnese(expense: Expense,categoryId: number){
    return this.http.post(`${this.baseURL}/${categoryId}`,{note: expense.note, amount: expense.amount, transactionDate: expense.transactionDate});
  }

  getAllExpenses(){
    return this.http.get<Expense[]>(`${this.baseURL}`);
  }

  getAverageByMonthPerCategory(categoryId: number){
    return this.http.get(`${this.baseURL}/${categoryId}/monthlyAverage`);
  }

  getAverageByMonth(){
    return this.http.get(`${this.baseURL}/monthlyAverage`);
  }

  getPredictions(futureDays: number){
    return this.http.get(`${this.baseURL}/predictions/${futureDays}`);
  }

  getMonthlyPredictions(){
    return this.http.get<number[]>(`${this.baseURL}/monthlyExpensePredictions`);
  }

  getTrendsForClient(): Observable<any> {
    return this.http.get(`${this.baseURL}/risingTrend`);
  }
}
