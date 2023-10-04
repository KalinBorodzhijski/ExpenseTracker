import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class NetworthService {

  private baseURL = 'http://localhost:4040/networth';

  constructor(private http: HttpClient) { }

  getNetWorth(): Observable<any> {
    return this.http.get(this.baseURL);
  }
}
