import { Component, OnInit } from '@angular/core';
import {Order} from "../model/Order";
import {PayService} from "../service/pay-service";

@Component({
  selector: 'app-chose-payment',
  templateUrl: './chose-payment.component.html',
  styleUrls: ['./chose-payment.component.css']
})
export class ChosePaymentComponent implements OnInit {

  order : Order = {merchantId:'',merchantOrderId:'',amount:0,paymentMethod:''}

  constructor(private payService: PayService) { }

  ngOnInit(): void {
    this.payService.getOrder().subscribe(
      res=>{
        this.order=res;
      },
      error => {
        alert('Something wrong');
      });
  }

  public proceedPayment(orderReady: Order){
    this.payService.paymentProceed(this.order).subscribe();

  }



}
