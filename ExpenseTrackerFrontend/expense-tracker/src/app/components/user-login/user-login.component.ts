import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth_service/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit{

  user: User = new User();

  constructor(private authService: AuthService, private router: Router,private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    
  }

  onSubmit(){
    this.authService.login(this.user.email, this.user.password).subscribe({
      error: e => this.snackBar.open("Invalid credentials", 'Close', { duration: 3000 }),
      next: data => {
        this.router.navigate(['/dashboard'])
      }
    });
  }

}
