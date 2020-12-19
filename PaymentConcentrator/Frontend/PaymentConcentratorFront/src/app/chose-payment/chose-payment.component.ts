import { Component, OnInit } from '@angular/core';
import {Order} from "../model/Order";
import {PayService} from "../service/pay-service";

@Component({
  selector: 'app-chose-payment',
  templateUrl: './chose-payment.component.html',
  styleUrls: ['./chose-payment.component.css']
})
export class ChosePaymentComponent implements OnInit {

  order : Order = {
    merchantId:'84074cf2-3d74-11eb-9d51-0242ac130002',
    amount:200,
    paymentMethod:'bank',
    successUrl: "https://screenmessage.com/hxqx",
    failedUrl: "https://screenmessage.com/keib",
    errorUrl: "https://screenmessage.com/gbvv"
  }

  constructor(private payService: PayService) { }

  ngOnInit(): void {
    /*this.payService.getOrder().subscribe(
      res=>{
        this.order=res;
      },
      error => {
        alert('Something wrong');
      });*/
  }

  public proceedPayment(orderReady: Order){
    this.payService.paymentProceed(this.order).subscribe(
      {
        next: (response) => {
          window.location.href = response.url;
        }
      }
    );

  }



}
