import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  private subject = new Subject<any>();
  sendMessageEvent() {
    this.subject.next(null);
  }

  getMessageEvent(): Observable<any>{ 
    return this.subject.asObservable();
  }

  constructor() { }
}
