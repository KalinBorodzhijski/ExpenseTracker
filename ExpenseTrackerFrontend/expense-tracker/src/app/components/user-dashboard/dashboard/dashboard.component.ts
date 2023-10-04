import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CategoryService } from 'src/app/services/category_service/category.service';
import { AddCategoryDialogComponent } from '../add-category-dialog/add-category-dialog.component';
import { Router } from '@angular/router';
import { ClientService } from 'src/app/services/client_service/client.service';
import { Expense } from 'src/app/model/expense';
import { ExpenseService } from 'src/app/services/expense_service/expense.service';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Subscription } from 'rxjs';
import { SharedService } from 'src/app/services/shared-service/shared.service';
import { AnalysisComponent } from '../analysis/analysis.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  @ViewChild(AnalysisComponent) private analysisComponent: AnalysisComponent;

  cards: Array<{ categoryId: string, title: string, content: string }> = [];
  unusedCategories: Array<{ categoryId: string, title: string, content: string }> = [];
  addCard = { title: 'Add New Category', content: 'Click to add a new category' };
  username: string = "";
  newIncome: any = {};
  incomeHistory: any[] = [];
  balance: number = 0;
  formSubmitted = false;
  currentDate = new Date();
  expenses: Expense[] = [];

  toggleCategories: boolean = true;
  showUnusedCategoriesCard = true;
  chatEventsubscription: Subscription;


  ngOnInit(): void {
    this.username = localStorage.getItem("name") ?? "";
    this.getCategories();
    this.getIncomeHistory();
    this.getBalance();
    this.currentDate = new Date();
    this.getAllExpnese();
    this.getUnusedCategories();
  }

  constructor(
    private categoryService: CategoryService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private router: Router,
    private clientService: ClientService,
    private expenseService: ExpenseService,
    private sharedService: SharedService
  ) {
    this.chatEventsubscription = this.sharedService.getMessageEvent().subscribe(() => {
      this.chatBotMessageNotif();
    })

  }

  reloadAnalysis() {
    if (this.analysisComponent) {
      this.analysisComponent.reload();
    }
  }

  chatBotMessageNotif() {
    this.getCategories();
    this.getIncomeHistory();
    this.getBalance();
    this.getAllExpnese();
    this.reloadAnalysis();
    this.getUnusedCategories();
  }

  hideUnusedCategoriesCard() {
    this.showUnusedCategoriesCard = false;
  }

  getAllExpnese() {
    this.expenseService.getAllExpenses().subscribe({
      error: e => { this.snackBar.open('Error loading expenses !', 'Close', { duration: 3000 }) },
      next: data => {
        this.expenses = data;
      }
    })
  }

  addIncome() {
    this.formSubmitted = true;

    if (!this.isFormValid(this.newIncome)) {
      return;
    }

    this.clientService.addIncome(this.newIncome).subscribe({
      error: e => { this.snackBar.open('Error adding income !', 'Close', { duration: 3000 }) },
      next: data => {
        this.snackBar.open(`Income added successfully!`, 'Close', { duration: 3000 });
        this.getIncomeHistory();
        this.getBalance();
        this.reloadAnalysis();
      }
    })
    this.formSubmitted = false;
    this.newIncome = { amount: null, note: '', transactionDate: '' };

  }

  getIncomeHistory() {
    this.clientService.getIncomeHistory().subscribe({
      error: e => { this.snackBar.open('Error loading income history !', 'Close', { duration: 3000 }) },
      next: data => {
        this.incomeHistory = data.map(val => {
          return { note: val.note, amount: val.amount, transactionDate: val.transactionDate }
        })
      }
    })
  }


  getBalance() {
    this.clientService.getBalance().subscribe({
      error: e => { this.snackBar.open('Error loading income balance !', 'Close', { duration: 3000 }) },
      next: data => {
        this.balance = data;
      }
    })
  }

  isFormValid(form: any) {
    return form.amount &&
      form.note &&
      form.transactionDate &&
      form.amount.valueOf() > 0 &&
      !this.isFutureDate(form.transactionDate);
  }

  isFutureDate(date: string): boolean {
    const enteredDate = new Date(date);
    return enteredDate > this.currentDate;
  }

  getCategories() {

    this.categoryService.getCategories().subscribe(data => {
      this.cards = data.map(val => {
        return { categoryId: val.categoryId, title: val.title, content: val.description }
      })

    })
  }

  deleteCategory(categoryId: string): void {
    this.categoryService.deleteCateogry(categoryId).subscribe({
      error: e => { this.snackBar.open('Error deleting category !', 'Close', { duration: 3000 }) },
      next: data => {
        this.snackBar.open(`Category deleted successfully!`, 'Close', { duration: 3000 });
        this.getCategories();
        this.getBalance();
        this.getAllExpnese();
        this.reloadAnalysis();
        this.getUnusedCategories();
      }
    })
  }


  editCategory(category: any) {

    const dialogRef = this.dialog.open(AddCategoryDialogComponent, {
      width: '400px',
      disableClose: true,
      data: { categoryName: category.title, categoryDescription: category.content }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        this.categoryService.updateCategory(category.categoryId, result.name, result.description).subscribe({
          error: e => this.snackBar.open('Error editing category !', 'Close', { duration: 3000 }),
          next: data => {
            this.snackBar.open(`Category edited successfully!`, 'Close', { duration: 3000 });
            this.getCategories();
          }
        })
      }
    });

  }


  viewCategory(categoryId: string) {
    this.router.navigate(['category-info', categoryId]);
  }


  openAddCategoryDialog(): void {
    const dialogRef = this.dialog.open(AddCategoryDialogComponent, {
      width: '400px',
      disableClose: true,
      data: { categoryName: "", categoryDescription: "" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const categoryName = result.name;
        const categoryDescription = result.description;

        this.categoryService.addCategory(categoryName, categoryDescription).subscribe({
          error: e => this.snackBar.open('Error creating category !', 'Close', { duration: 3000 }),
          next: data => {
            this.snackBar.open(`Category saved successfully!`, 'Close', { duration: 3000 });
            this.getCategories();
            this.reloadAnalysis();
          }
        })
      }
    });
  }

  dateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value) {
        const parts = value.split(".");
        const formattedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;
        if (isNaN(Date.parse(formattedDate))) {
          return { invalidDate: true };
        }
        else {
          return null;
        }

      }
      return null;
    };
  }

  numberValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value && isNaN(parseFloat(value))) {
        return { invalidNumber: true };
      }
      return null;
    };
  }

  getUnusedCategories() {
    this.unusedCategories = []
    this.categoryService.getUnusedCategories().subscribe(data => {
      this.unusedCategories = data.map(val => {
        return { categoryId: val.categoryId, title: val.title, content: val.description }
      })
    })

  }

}
