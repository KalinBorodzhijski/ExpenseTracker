import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChartDataset, ChartOptions } from 'chart.js';
import { ExpenseService } from 'src/app/services/expense_service/expense.service';
import { NetworthService } from 'src/app/services/networth_service/networth.service';

@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit{

  toggleAnalysis: boolean = true;
  monthlyExpensesSum: { [key: string]: number } = {};
  isRisingTrend: boolean = false;
  predictedExpenses: { [key: string]: number } = {};
  public trendChartData: ChartDataset[] = [
    { data: [], label: 'Monthly Expenses', backgroundColor: 'rgba(75, 192, 192, 0.2)', borderColor: 'rgba(75, 192, 192, 1)' },
    { data: [], label: 'Predicted Expenses', backgroundColor: 'rgba(255, 206, 86, 0.2)', borderColor: 'rgba(255, 206, 86, 1)' }
  ];
  public trendChartLabels: string[] = [];


  expenseDistributionData: ChartDataset[] = [];
  expenseDistributionLabels: string[] = [];


  netWorthChartData: any[] = [];
  netWorthChartLabels: string[] = [];

  
  categoryData: Map<string, Map<string, number>> = new Map<string, Map<string, number>>();
  chartDataAverageExpensePerMonth: ChartDataset[] = [];
  chartLabelsAverageExpensePerMonth: string[] = [];

  chartDataMonthlyPredictions: ChartDataset[] = [];
  chartLabelsMonthlyPredictions: string[] = [];

  
  chartOptions: ChartOptions = {
    responsive: true,
    scales: {
      x: { display: true },
      y: { display: true }
    }
  };

  pieChartOptions: ChartOptions = {
    responsive: true,
  };

  constructor(private networthService: NetworthService,
              private expenseService: ExpenseService,
              private snackBar: MatSnackBar){
    
  }

  ngOnInit(): void {
    this.reload();
  }

  reload(){
    this.processPredictionChartsData();
    this.getNetWorth();
    this.processChartData();
    this.loadExpenseDistributionData();
    this.loadTrendAnalysisData();
  }


  
  getNetWorth() {
    this.networthService.getNetWorth().subscribe({
      error: e => { this.snackBar.open('Error loading net worth !', 'Close', { duration: 3000 }) },
      next: data => {
        const actualData = data.actualNetWorth;
        const predictedData = data.predictedNetWorth;
        const nullArray: (null)[] = new Array(Object.values(actualData).length - 1).fill(null);
        const actualValues = Object.values(actualData);
        const predictedValues = Object.values(predictedData);
        const combinedValues = [actualValues[actualValues.length - 1], ...predictedValues];

        this.netWorthChartData = [
          {
            label: 'Actual Net Worth',
            data: Object.values(actualData)
          },
          {
            label: 'Predicted Net Worth',
            data: [...nullArray, ...combinedValues],
            fill: false
          }
        ];

        this.netWorthChartLabels = [...Object.keys(actualData), ...Object.keys(predictedData)]
      }
    })

  }

  
  processChartData() {

    this.expenseService.getAverageByMonth().subscribe({
      error: e => { this.snackBar.open('Error loading average expenses !', 'Close', { duration: 3000 }) },
      next: data => {
        this.chartDataAverageExpensePerMonth = [];
        this.chartLabelsAverageExpensePerMonth = [];
        this.categoryData.clear();
        const cateogries: string[] = [];
        let months = new Set<string>();

        for (const [category, value] of Object.entries(data)) {
          const expensesPerDate = new Map<string, number>();
          cateogries.push(category);
          for (const [key, result] of Object.entries(value)) {
            months.add(key);
            expensesPerDate.set(key, Number(result));
          }
          this.categoryData.set(category, expensesPerDate);
        }

        let datesArray = Array.from(months);
        datesArray.sort((a: string, b: string) => {
          if (a < b) return -1;
          else if (a > b) return 1;
          else return 0;
        });
        let amountArray: number[][] = [];
        datesArray.forEach((date: string) => {
          let amountArrayPerCategory: number[] = [];
          cateogries.forEach((categoryName: string) => {
            let amount = 0;
            const categoryInfo: Map<string, number> | undefined = this.categoryData.get(categoryName);
            if (categoryInfo) {
              const expenseAmount: number | undefined = categoryInfo.get(date);
              if (expenseAmount) {
                amount = expenseAmount;
              }
              amountArrayPerCategory.push(amount)
            }
          })
          amountArray.push(amountArrayPerCategory)

        });

        const amountArrayReversed = amountArray[0].map((_, colIndex) => amountArray.map(row => row[colIndex]));
        this.chartLabelsAverageExpensePerMonth = datesArray;
        let counter: number = 0;
        amountArrayReversed.forEach(((amounts: number[]) => {
          this.chartDataAverageExpensePerMonth.push({ data: amounts, label: cateogries.at(counter) })
          counter++;

        }));
      }
    });
  }

  loadExpenseDistributionData() {
    this.expenseService.getMonthlyExpensesPerCategory().subscribe({
      error: e => { },
      next: data => {
        const dataValues: number[] = (Object.values(data) as number[]).map(value => Number(value));
        this.expenseDistributionLabels = Object.keys(data);
        this.expenseDistributionData = [{ data: dataValues, label: 'Monthly Expeses' }];
      }
    })
  }

  loadTrendAnalysisData(): void {
    this.expenseService.getTrendsForClient().subscribe({
      error: e => this.snackBar.open('There was an error fetching the trend analysis data !', 'Close', { duration: 3000 }),
      next: data => {
        const monthlyData: Map<string, number> = new Map(Object.entries(data.monthlyExpensesSum));
        const predictionsData: Map<string, number> = new Map(Object.entries(data.predictedExpenses));
        this.trendChartLabels = Array.from(new Set([...monthlyData.keys(), ...predictionsData.keys()])).map(key => key.toString());
        this.trendChartData[0].data = this.trendChartLabels.map(label => monthlyData.get(label) || 0);
        this.trendChartData[1].data = this.trendChartLabels.map(label => predictionsData.get(label) || 0);
        this.isRisingTrend = data.risingTrend;
      }
    })
  }


  processPredictionChartsData() {

    this.expenseService.getMonthlyPredictions().subscribe({
      error: e => { this.snackBar.open('Error loading monthly predictions chart !', 'Close', { duration: 3000 }) },
      next: data => {
        this.chartDataMonthlyPredictions = [];
        this.chartLabelsMonthlyPredictions = [];
        const currentDate = new Date();
        const currentDayOfMonth = currentDate.getDate();
        currentDate.setMonth(currentDate.getMonth() + 1, 0);
        const numberOfDays = currentDate.getDate();


        for (let i = 1; i <= numberOfDays; i++) {
          this.chartLabelsMonthlyPredictions.push(i.toString());
        }

        const dataFirstHalf: number[] = data.slice(0, currentDayOfMonth);
        let dataSecondHalf: number[] = Array(data.length).fill(null);
        dataSecondHalf.splice(currentDayOfMonth - 1, data.length - currentDayOfMonth + 1, ...data.slice(currentDayOfMonth - 1));

        const datasetFirstHalf: ChartDataset = {
          data: dataFirstHalf,
          label: 'Monthly spendings',
          pointRadius: 0
        };

        const datasetSecondHalf: ChartDataset = {
          data: dataSecondHalf,
          label: 'Monthly Predictions',
          pointRadius: 0
        };
        this.chartDataMonthlyPredictions = [datasetFirstHalf, datasetSecondHalf];
      }
    })
  }


}
