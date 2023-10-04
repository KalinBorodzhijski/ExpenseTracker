import { Component, Inject  } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-add-category-dialog',
  templateUrl: './add-category-dialog.component.html',
  styleUrls: ['./add-category-dialog.component.css']
})
export class AddCategoryDialogComponent {

  categoryName: string;
  categoryDescription: string;
  showError: boolean = false;

  constructor(public dialogRef: MatDialogRef<AddCategoryDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.categoryName = data.categoryName;
    this.categoryDescription = data.categoryDescription;
  }

  saveCategory(): void {
    if (this.categoryName && this.categoryDescription) {
      this.showError = false;
      this.dialogRef.close({ name: this.categoryName, description: this.categoryDescription });
    }
    else this.showError = true;
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
