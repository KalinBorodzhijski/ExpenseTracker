import { Component,OnInit  } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth_service/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  registrationForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router,private snackBar: MatSnackBar) { }
  
  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      secondName: ['', Validators.required],
      thirdName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      balance: ['', [Validators.required, Validators.min(0), Validators.pattern(/^\d+$/)]]
    });
  }

  onSubmit() {
    if (this.registrationForm.invalid) {
      return;
    }

    const registrationData = this.registrationForm.value;
    this.authService.register(
      registrationData.firstName, 
      registrationData.secondName, 
      registrationData.thirdName, 
      registrationData.email,
      registrationData.password,
      registrationData.balance).subscribe({
        error: e => {this.snackBar.open(e.error, 'Close', { duration: 3000 })},
        next: data => {
          this.authService.logout();
          this.router.navigate(['/login'])
        }
      });
  }

}
