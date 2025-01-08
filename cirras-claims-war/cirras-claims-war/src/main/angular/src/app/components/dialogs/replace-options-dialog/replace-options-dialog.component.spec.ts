import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ReplaceOptionsDialogComponent } from './replace-options-dialog.component';

describe('ReplaceOptionsDialogComponent', () => {
  let component: ReplaceOptionsDialogComponent;
  let fixture: ComponentFixture<ReplaceOptionsDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ReplaceOptionsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReplaceOptionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
