import { Component } from '@angular/core';
import { AuthService } from './services/auth_service/auth.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'expense-tracker';

  constructor(public authService: AuthService) {
  }
  
  handleLogOutClick(){
    this.authService.logout();
  }
}
