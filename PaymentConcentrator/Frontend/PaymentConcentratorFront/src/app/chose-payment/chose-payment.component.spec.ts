import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChosePaymentComponent } from './chose-payment.component';

describe('ChosePaymentComponent', () => {
  let component: ChosePaymentComponent;
  let fixture: ComponentFixture<ChosePaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChosePaymentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChosePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
