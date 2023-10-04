import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { DashboardComponent } from './components/user-dashboard/dashboard/dashboard.component';
import { UserRegistrationComponent } from './components/user-registration/user-registration.component';
import { AuthGuard } from './guards/AuthGuard';
import { LoginGuard } from './guards/LoginGuard';
import { CategoryInfoComponent } from './components/category-info/category-info.component';

const routes: Routes = [
  {path: "register", component: UserRegistrationComponent, canActivate: [LoginGuard]},
  {path: "login", component: UserLoginComponent, canActivate: [LoginGuard]},
  {path: "dashboard", component: DashboardComponent, canActivate: [AuthGuard]},
  {path: "category-info/:id", component: CategoryInfoComponent, canActivate: [AuthGuard]},
  {path: '', redirectTo: 'dashboard', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
