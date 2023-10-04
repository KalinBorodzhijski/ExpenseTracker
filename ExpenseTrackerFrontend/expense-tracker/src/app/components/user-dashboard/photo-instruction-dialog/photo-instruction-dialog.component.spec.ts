import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PhotoInstructionDialogComponent } from './photo-instruction-dialog.component';

describe('PhotoInstructionDialogComponent', () => {
  let component: PhotoInstructionDialogComponent;
  let fixture: ComponentFixture<PhotoInstructionDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PhotoInstructionDialogComponent]
    });
    fixture = TestBed.createComponent(PhotoInstructionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
