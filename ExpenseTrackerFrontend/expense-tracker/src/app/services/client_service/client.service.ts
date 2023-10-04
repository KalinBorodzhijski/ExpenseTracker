import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseURL = "http://localhost:4040/income";
  constructor(private http: HttpClient) { }

  addIncome(incomeData: any){
    return this.http.post<any>(`${this.baseURL}`,{amount: incomeData.amount, note: incomeData.note, transactionDate: incomeData.transactionDate})
    .pipe( map(response => response.new_balance));
  }

  getIncomeHistory(){
    return this.http.get<any[]>(`${this.baseURL}/income-history`);
  }

  getBalance(){
    return this.http.get<number>(`${this.baseURL}`);
  }
}
