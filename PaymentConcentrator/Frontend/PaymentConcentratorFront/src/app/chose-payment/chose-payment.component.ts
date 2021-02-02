import { Component, OnInit } from '@angular/core';
import { Order } from "../model/Order";
import { PayService } from "../service/pay-service";
import { PaymentService } from "../model/PaymentService";
import { MerchantService } from '../service/merchant.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-chose-payment',
  templateUrl: './chose-payment.component.html',
  styleUrls: ['./chose-payment.component.css']
})
export class ChosePaymentComponent implements OnInit {
  allPaymentServices: PaymentService[];
  paymentMethod: PaymentService;
  order: Order = {
    merchantId: '84074cf2-3d74-11eb-9d51-0242ac130002',
    amount: 200,
    paymentMethod: 'bank',
    successUrl: "https://screenmessage.com/hxqx",
    failedUrl: "https://screenmessage.com/keib",
    errorUrl: "https://screenmessage.com/gbvv",
    paymentUrl: "",
    cycles: undefined,
    frequency: undefined,
    interval: undefined
  }

  constructor(
    private payService: PayService,
    private merchantService: MerchantService,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    /*this.payService.discoverAllPaymentType().subscribe(
      res => {
        this.allPaymentServices = res;
      }
    );*/
    /*this.route.queryParams
      .subscribe(params => {
        console.log(params);

        this.order.merchantId = params.merchantId;
        this.order.amount = params.amount;
        this.order.successUrl = params.successUrl;
        this.order.failedUrl = params.failedUrl;
        this.order.errorUrl = params.errorUrl;
        console.log(this.order);
      }
    );*/
  
    this.merchantService.getPaymentTypes(this.order.merchantId).subscribe({
			next: (result) => {
        this.allPaymentServices = result;
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Error getting payment services!");
			  }
		});
  }

  public proceedPayment(orderReady: Order) {
    /*var splitted = orderReady.paymentMethod.split(":");
    if(splitted[3].toLowerCase().startsWith("bank")){
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
    }*/
    orderReady.paymentMethod = this.paymentMethod.name.concat(':').concat(this.paymentMethod.url)
    orderReady.paymentUrl = this.paymentMethod.url;
    //alert(orderReady.paymentUrl);
    this.payService.paymentServiceProceed(orderReady).subscribe(
      {
        next: (response) => {
          window.location.href = response.redirectLink;
        }
      }
    );
  }




}
