import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-dashcard',
  templateUrl: './dashcard.component.html',
  styleUrls: ['./dashcard.component.css']
})
export class DashcardComponent {
  @Input() categoryId: string = "";
  @Input() title: string = "";
  @Input() content: string = "";
  @Input() isAddCard: Boolean = false;

  @Output() deleteCategory: EventEmitter<string> = new EventEmitter<string>();
  @Output() editCategory: EventEmitter<{categoryId: string, title: string, content: string}> = new EventEmitter<{categoryId: string, title: string, content: string}>();

  deleteClicked(): void {
    this.deleteCategory.emit(this.categoryId);
  }

  
  editClicked(): void {
    this.editCategory.emit({categoryId: this.categoryId,title:  this.title,content: this.content});
  }
}
