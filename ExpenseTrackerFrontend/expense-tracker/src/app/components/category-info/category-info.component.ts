import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { Expense } from 'src/app/model/expense';
import { ChartDataset, ChartOptions } from 'chart.js';
import { CategoryService } from 'src/app/services/category_service/category.service';
import { ExpenseService } from 'src/app/services/expense_service/expense.service';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { SharedService } from 'src/app/services/shared-service/shared.service';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { ChatbotService } from 'src/app/services/chatbot_service/chatbot.service';
import { PhotoInstructionDialogComponent } from '../user-dashboard/photo-instruction-dialog/photo-instruction-dialog.component';

declare var $: any;
@Component({
  selector: 'app-category-info',
  templateUrl: './category-info.component.html',
  styleUrls: ['./category-info.component.css']
})
export class CategoryInfoComponent implements OnInit {

  chartData: ChartDataset[] = [];
  chartLabels: string[] = [];
  chartOptions: ChartOptions = {
    responsive: true,
    scales: {
      x: { display: true },
      y: { display: true }
    }
  };

  public lineChartLabels: string[] = [];
  public lineChartData: ChartDataset[] = [];

  uploadForm: FormGroup;
  receiptData: any;

  currentDailyAverage: number = 0;
  forecastedDailyAverage: number = 0;
  monthPredictions: number[] = [];

  categoryId: number = 0;
  categoryName: string = "";
  formSubmitted = false;


  pageSize: number = 4;
  pageNumber: number = 1;
  totalItems: number = 0;
  paginatedExpenses: any[] = [];

  chatEventsubscription: Subscription;

  // Budget variables
  showBudgetCard: boolean = false;
  budgetAmount: number = 0;
  budgetFormSubmitted: boolean = false;
  tempBudgetAmount: number = 0;
  totalExpenses: number = 0;


  newExpense: Expense = {
    expenseId: 0,
    note: '',
    amount: 0,
    transactionDate: new Date()
  };

  expenses: Expense[] = [];

  months = [
    { name: 'January', value: 0 },
    { name: 'February', value: 1 },
    { name: 'March', value: 2 },
    { name: 'April', value: 3 },
    { name: 'May', value: 4 },
    { name: 'June', value: 5 },
    { name: 'July', value: 6 },
    { name: 'August', value: 7 },
    { name: 'September', value: 8 },
    { name: 'October', value: 9 },
    { name: 'November', value: 10 },
    { name: 'December', value: 11 }
  ];


  selectedMonth: number = new Date().getMonth();
  selectedYear: number = new Date().getFullYear();

  receipFile: File | null = null;

  showReceiptUpload: boolean = false;

  years: number[] = [];
  currentYear: number = new Date().getFullYear();


  constructor(private activatedRoute: ActivatedRoute,
    private expenseService: ExpenseService,
    private snackBar: MatSnackBar,
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private sharedService: SharedService,
    private chatbotService: ChatbotService,
    private dialog: MatDialog) {
    this.chatEventsubscription = this.sharedService.getMessageEvent().subscribe(() => {
      this.chatBotMessageNotif();
    })

    this.uploadForm = this.formBuilder.group({
      file: [null],
      date: ['', [Validators.required, this.dateValidator()]],
      total: ['', [Validators.required, this.numberValidator()]],
      products: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    for (let year = 2020; year <= this.currentYear; year++) {
      this.years.push(year);
    }
    this.selectedYear = this.currentYear;

    this.categoryId = this.activatedRoute.snapshot.params['id'];
    this.getExpenses(this.categoryId);
    this.categoryService.getCategory(this.categoryId).subscribe(data => {
      this.categoryName = data;
    })
    this.updateChart();

  }

  chatBotMessageNotif() {
    this.getExpenses(this.categoryId);
    this.updateChart();
  }

  toggleReceiptUpload(): void {
    this.showReceiptUpload = !this.showReceiptUpload;
  }

  onFileChange(event: any) {
    this.receipFile = event.target.files.item(0);
  }


  onUpload(): void {
    if (this.receipFile) {
      this.chatbotService.uploadFile(this.receipFile).subscribe({
        error: e => { },
        next: data => {

          if (data.date == "Date not found" &&
            data.total == "Total amount not found" &&
            data.products == "Products not found") {
            this.dialog.open(PhotoInstructionDialogComponent);
          }
          else {
            this.receiptData = data;
          }
        }
      })
    }
    else {
      this.snackBar.open('No file selected !', 'Close', { duration: 3000 })
    }
  }

  handleCancel() {
    this.receiptData = null;
    this.receipFile = null;
  }


  onProcess() {
    this.onUpload();
  }

  onSubmit() {
    const datePattern = /(\d{2})[.\-/ ](\d{2})[.\-/ ](\d{4})/;
    const match = this.receiptData.date.match(datePattern);
    let formattedDate: string;
    if (match) {
      const day = parseInt(match[1]);
      const month = parseInt(match[2]);
      const year = parseInt(match[3]);
      formattedDate = `${year}-${month}-${day}`;
    } else {
      this.snackBar.open('Date cannot be parsed !', 'Close', { duration: 3000 })
      return
    }


    let expenseObj: Expense = {
      expenseId: 0,
      amount: this.receiptData.total,
      note: this.receiptData.products,
      transactionDate: new Date(formattedDate)
    };

    // Convert the object to JSON, but use the formatted date string for transactionDate
    const payload = JSON.stringify(expenseObj, (key, value) => {
      if (key === 'transactionDate') {
        return formattedDate; // Use the formatted date string
      }
      return value;
    });

    this.expenseService.addExpnese(JSON.parse(payload), this.categoryId).subscribe({
      error: e => { this.snackBar.open('Error creating expenses !', 'Close', { duration: 3000 }) },
      next: data => {
        this.getExpenses(this.categoryId);
        this.snackBar.open(`Expense saved successfully!`, 'Close', { duration: 3000 });
        this.updateChart();
        this.receiptData = null;
      }
    })

  }

  dateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value) {
        let parts = value.split(".");
        if (parts.length == 1) {
          parts = value.split("-");
        }
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

  getExpenses(categoryId: number) {
    this.expenseService.getExpensesByCategory(categoryId, this.selectedMonth, this.selectedYear).subscribe(data => {
      this.expenses = data.monthlyExpenses;
      this.expenses.sort((a, b) => {
        const dateA = new Date(a.transactionDate).getTime();
        const dateB = new Date(b.transactionDate).getTime();
        return dateB - dateA;
      });
      this.monthPredictions = data.currentMonthPrediction;
      this.totalExpenses = this.expenses.reduce((accumulator, expense) => accumulator + expense.amount, 0);
      this.getBudget();
      this.totalItems = this.expenses.length;
      this.paginate();
    })
  }

  paginate() {
    const start = (this.pageNumber - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedExpenses = this.expenses.slice(start, end);
  }

  nextPage() {
    if (this.pageNumber * this.pageSize < this.totalItems) {
      this.pageNumber++;
      this.paginate();
    }
  }

  previousPage() {
    if (this.pageNumber > 1) {
      this.pageNumber--;
      this.paginate();
    }
  }

  updateExpensesByMonth() {
    this.getExpenses(this.categoryId);
  }


  deleteExpense(expense: Expense) {
    this.expenseService.deleteExpense(expense, this.categoryId).subscribe({
      error: e => { this.snackBar.open('Error deleting expense !', 'Close', { duration: 3000 }) },
      next: data => {
        this.snackBar.open(`Expense deleted successfully!`, 'Close', { duration: 3000 });
        this.getExpenses(this.categoryId);
        this.updateChart();
      }
    })
  }

  addExpense() {

    this.formSubmitted = true;

    if (!this.isFormValid()) {
      return;
    }

    this.expenseService.addExpnese(this.newExpense, this.categoryId).subscribe({
      error: e => this.snackBar.open('Error creating expense !', 'Close', { duration: 3000 }),
      next: data => {
        this.getExpenses(this.categoryId);
        this.snackBar.open(`Expense saved successfully!`, 'Close', { duration: 3000 });
        this.updateChart();
      }
    });
  }

  isFormValid() {
    return (
      this.newExpense.note &&
      this.newExpense.amount &&
      (this.newExpense.amount.valueOf() > 0)
    );
  }
  updateChart() {

    this.expenseService.getAverageByMonthPerCategory(this.categoryId).subscribe({
      error: e => { this.snackBar.open('Error fetching average !', 'Close', { duration: 3000 }) },
      next: (data: any) => {
        const resultMap = new Map<string, number>();
        for (const [key, value] of Object.entries(data)) {
          resultMap.set(key, Number(value));
        }
        this.chartLabels = Array.from(resultMap.keys());
        this.chartData = [
          {
            data: Array.from(resultMap.values()),
            label: 'Average Expenses'
          }
        ];
      }
    });
  }


  toggleBudgetCard(): void {
    if (!this.showBudgetCard) {
      this.tempBudgetAmount = this.budgetAmount;
    }
    this.showBudgetCard = !this.showBudgetCard;
  }

  saveBudget(budget: number) {
    this.budgetFormSubmitted = true;
    if (this.tempBudgetAmount && this.tempBudgetAmount > 0 && this.tempBudgetAmount < 100000) {
      this.budgetAmount = this.tempBudgetAmount;

      this.categoryService.setBudget(this.categoryId, this.budgetAmount, Number.parseInt(this.selectedMonth.toString()), this.selectedYear).subscribe({
        error: e => this.snackBar.open('Error saving budget !', 'Close', { duration: 3000 }),
        next: data => {
          this.snackBar.open(`Budget saved successfully!`, 'Close', { duration: 3000 });
          this.getBudget();
          this.showBudgetCard = !this.showBudgetCard;
        }
      });
    }
  }

  getBudget() {
    this.categoryService.getBudget(this.categoryId, this.selectedMonth, this.selectedYear).subscribe({
      error: e => this.snackBar.open('Error loading  budget !', 'Close', { duration: 3000 }),
      next: data => {
        if ((this.selectedMonth != new Date().getMonth()) || (this.selectedYear != new Date().getFullYear())) {
          this.currentDailyAverage = (this.totalExpenses / (new Date(this.selectedYear, this.selectedMonth, 0).getDate()));
        }
        else {
          this.currentDailyAverage = (this.totalExpenses / (new Date().getDate()));
        }
        this.budgetAmount = data;
        this.calculateBudgetProgress();
        this.isOverBudget();
        this.populateChartData();
        this.calculatePredictedAverage();
      }
    });
  }

  calculatePredictedAverage() {
    let leftFromBudget = this.budgetAmount - this.totalExpenses;
    if ((leftFromBudget > 0) &&
      (((Number.parseInt(this.selectedMonth.toString())) == new Date().getMonth()) &&
        ((Number.parseInt(this.selectedYear.toString())) == new Date().getFullYear()))) {
      const today = new Date();
      const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);
      this.forecastedDailyAverage = leftFromBudget / (endOfMonth.getDate() - today.getDate());
    }
    else {
      this.forecastedDailyAverage = 0;
    }

  }

  calculateBudgetProgress(): string {
    const percentage = (this.totalExpenses / this.budgetAmount) * 100;
    return Math.min(percentage, 100) + "%";
  }

  isOverBudget(): boolean {
    return this.totalExpenses > this.budgetAmount;
  }

  getDatesForMonth(month: number, year: number): number[] {

    month = Number.parseInt(month.toString());
    year = Number.parseInt(year.toString());
    const endOfMonth = new Date(year, month + 1, 0).getDate();
    let dateArray = []
    for (let i = 1; i <= endOfMonth; i++) {
      dateArray.push(i);
    }
    return dateArray;
  }

  populateChartData() {
    this.lineChartData = [];
    this.lineChartLabels = [...this.getDatesForMonth(this.selectedMonth, this.selectedYear).map(date => date.toString())];
    let cumulativeExpense = 0.0;
    let expensesData = [];
    let predictionData = [];
    let budgetData = [];
    let predictionValue = 0;

    let currentDate: number = 0;

    if ((this.selectedMonth != new Date().getMonth()) || (this.selectedYear != new Date().getFullYear())) {
      const date = new Date(Number.parseInt(this.selectedYear.toString()), Number.parseInt(this.selectedMonth.toString()) + 1, 0);
      currentDate = date.getDate();
    }
    else currentDate = new Date().getDate();

    for (let i = 0; i < this.lineChartLabels.length; i++) {

      let currentDayExpense = this.expenses.filter(exp => new Date(exp.transactionDate).getDate().toString() == this.lineChartLabels[i])
        .reduce((acc, exp) => acc + exp.amount, 0);
      cumulativeExpense += currentDayExpense;

      if (Number.parseInt(this.lineChartLabels[i]) < currentDate) {
        expensesData.push(cumulativeExpense);
        predictionData.push(null);
      }
      else if (Number.parseInt(this.lineChartLabels[i]) == currentDate) {
        expensesData.push(cumulativeExpense);
        predictionData.push(cumulativeExpense);
        predictionValue = cumulativeExpense;
      }
      else {
        predictionValue = this.monthPredictions[i - new Date().getDate()];
        expensesData.push(null);
        predictionData.push(predictionValue);
      }

      if (this.budgetAmount > 0) {
        budgetData.push(this.budgetAmount);
      } else {
        budgetData.push(null);
      }

    }
    if ((this.selectedMonth == new Date().getMonth()) && (this.selectedYear == new Date().getFullYear())) {
      this.lineChartData.push({ data: predictionData, label: 'Predictions', fill: false, borderDash: [5, 5], pointRadius: 0 })
    }

    if (this.budgetAmount > 0) {
      this.lineChartData.push({ data: budgetData, label: 'Budget', fill: false, borderDash: [5, 5], pointRadius: 0 })
    }
    this.lineChartData.push({ data: expensesData, label: 'Expenses' })

  }


}