import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserRegistrationComponent } from './components/user-registration/user-registration.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { DashboardComponent } from './components/user-dashboard/dashboard/dashboard.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './Interceptors/TokenInterceptor';
import { AuthInterceptor } from './Interceptors/AuthInterceptor';
import { ReactiveFormsModule } from '@angular/forms';
import { DashcardComponent } from './components/user-dashboard/dashcard/dashcard.component';
import { AddCategoryDialogComponent } from './components/user-dashboard/add-category-dialog/add-category-dialog.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CategoryInfoComponent } from './components/category-info/category-info.component';
import { NgChartsModule } from 'ng2-charts';
import { ChatComponent } from './components/chat/chat.component';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { PhotoInstructionDialogComponent } from './components/user-dashboard/photo-instruction-dialog/photo-instruction-dialog.component';
import { AnalysisComponent } from './components/user-dashboard/analysis/analysis.component';
import { CurrencyConvertorComponent } from './components/currency-convertor/currency-convertor.component';

@NgModule({
  declarations: [
    AppComponent,
    UserRegistrationComponent,
    UserLoginComponent,
    DashboardComponent,
    DashcardComponent,
    AddCategoryDialogComponent,
    CategoryInfoComponent,
    ChatComponent,
    PhotoInstructionDialogComponent,
    AnalysisComponent,
    CurrencyConvertorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatSnackBarModule,
    NgChartsModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    CommonModule,
    MatIconModule,
    MatDividerModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
