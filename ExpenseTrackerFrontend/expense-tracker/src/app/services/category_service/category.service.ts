import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from 'src/app/model/category';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CategoryService{

  baseURL = "http://localhost:4040/categories";

  constructor(private http: HttpClient) { }

  getCategories(): Observable<Category[]>{
    return this.http.get<Category[]>(`${this.baseURL}`);
  }

  addCategory(name: string, description: string){
    return this.http.post(`${this.baseURL}`,{title: name, description: description});
  }

  deleteCateogry(categoryId: string){
    return this.http.delete(`${this.baseURL}/${categoryId}`);
  }

  updateCategory(categoryId: string, header: string, description: string){
    return this.http.put(`${this.baseURL}/${parseInt(categoryId)}`,{title: header, description: description});
  }

  getCategory(categoryId: number){
    return this.http.get<any>(`${this.baseURL}/${categoryId}`).pipe( map(response => response.title) );
  }

  getBudget(categoryId: number, month: number, year: number){
    month = Number.parseInt(month.toString()) + 1;
    const yearMonth = `${year.toString()}${month.toString().padStart(2, '0')}`;
    return this.http.get<number>(`${this.baseURL}/${categoryId}/budget/${yearMonth}`);
  }

  setBudget(categoryId: number, budget: number, month: number, year: number){
    month = Number.parseInt(month.toString()) + 1;
    const yearMonth = `${year.toString()}${month.toString().padStart(2, '0')}`;
    return this.http.post<number>(`${this.baseURL}/${categoryId}/budget/${yearMonth}`,{amount: budget});
  }

  getUnusedCategories(): Observable<Category[]>{
    return this.http.get<Category[]>(`${this.baseURL}/unused`);
  }
}
