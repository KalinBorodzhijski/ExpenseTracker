import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth_service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard{
  constructor(private router: Router, private authService: AuthService) {}

  canActivate(): boolean {

    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
      return false;
    }
    return true;
  }

}