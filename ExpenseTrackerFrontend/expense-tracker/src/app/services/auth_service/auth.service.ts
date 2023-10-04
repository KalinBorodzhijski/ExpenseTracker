import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { share, shareReplay, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseURL = "http://localhost:4040/clients";

  constructor(private http: HttpClient) { }

  login(email:string, password:string ){
    return this.http.post(`${this.baseURL}/login`, {email, password}).pipe(tap(res => this.setSession(res), shareReplay()));
  }
  
  register(firstName: string, secondName: string, thirdName: string, email: string, password: string, balance: number){
    return this.http.post(`${this.baseURL}/register`, {firstName, secondName, thirdName ,password,email, balance}).pipe( shareReplay());
  }

  private setSession(authResult: any) {
    console.log(authResult);
    localStorage.setItem('id_token', authResult.token);
    localStorage.setItem('name', authResult.name);
  }
  
  logout() {
    localStorage.removeItem("id_token");
    localStorage.removeItem("name");
  }

  public isLoggedIn() {
    return !this.isTokenExpired();
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }

  private isTokenExpired(): boolean {
    const token = localStorage.getItem('id_token');
    
    if (token) {
      try {
        const tokenParts = token.split('.');
        const tokenPayload = JSON.parse(atob(tokenParts[1]));
        const expirationTime = tokenPayload.exp;
        const expirationDate = expirationTime * 1000;
        const currentDate = new Date().getTime();
        
        return expirationDate < currentDate;
      } catch (error) {
        console.log('Invalid token:', error);
        return true;
      }
    }
    else return true;
    
  }
}
