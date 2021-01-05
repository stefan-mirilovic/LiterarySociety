import { Component, OnInit } from '@angular/core';
import {Order} from "../model/Order";
import {PayService} from "../service/pay-service";
import {PaymentService} from "../model/PaymentService";

@Component({
  selector: 'app-chose-payment',
  templateUrl: './chose-payment.component.html',
  styleUrls: ['./chose-payment.component.css']
})
export class ChosePaymentComponent implements OnInit {
  allPaymentServices: PaymentService[];
  order : Order = {
    merchantId:'84074cf2-3d74-11eb-9d51-0242ac130002',
    amount:200,
    paymentMethod:'bank',
    successUrl: "https://screenmessage.com/hxqx",
    failedUrl: "https://screenmessage.com/keib",
    errorUrl: "https://screenmessage.com/gbvv",
    paymentUrl: ""
  }

  constructor(private payService: PayService) { }

  ngOnInit(): void {
    this.payService.discoverAllPaymentType().subscribe(
        res =>{
          this.allPaymentServices=res;
        }
    );
  }

  public proceedPayment(orderReady: Order){
    var splitted = orderReady.paymentMethod.split(":");
    if(splitted[3]==="bank"){
      this.payService.paymentProceed(this.order).subscribe(
          {
            next: (response) => {
              console.log(response);
              window.location.href = response.url;
            }
          }
      );
    }
    else{
      orderReady.paymentUrl=splitted[0].concat(":").concat(splitted[1]).concat(":").concat(splitted[2]);
      alert(orderReady.paymentUrl);
      this.payService.paymentServiceProceed(orderReady).subscribe(
          {
            next: (response) => {
              window.location.href = response.redirectLink;
            }
          }
      );
    }
  }




}
