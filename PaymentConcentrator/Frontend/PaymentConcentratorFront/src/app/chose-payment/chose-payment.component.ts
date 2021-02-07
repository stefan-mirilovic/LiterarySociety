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
  loading: boolean = false;
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
    this.route.queryParams
      .subscribe(params => {
        console.log(params);
        if (params.merchantId) {
          this.order.merchantId = params.merchantId;
          this.order.amount = params.amount;
          this.order.successUrl = params.successUrl;
          this.order.failedUrl = params.failedUrl;
          this.order.errorUrl = params.errorUrl;
          console.log(this.order);
        }
      }
    );
  
    this.merchantService.getPaymentTypes(this.order.merchantId).subscribe({
			next: (result) => {
        let myPaymentMethods: PaymentService[] = [];
        this.payService.discoverAllPaymentType().subscribe(
          res => {
            for (let r1 of res) {
              for (let r2 of result) {
                if (r1.name === r2.name) {
                  myPaymentMethods.push(r2);
                  break;
                }
              }
            }
          }
        );
        this.allPaymentServices = myPaymentMethods;
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Error getting payment services!");
			  }
		});
  }

  public proceedPayment(orderReady: Order, paymentMethod: PaymentService) {
    orderReady.paymentMethod = paymentMethod.name.concat(':').concat(paymentMethod.url)
    orderReady.paymentUrl = paymentMethod.url;
    this.loading = true;
    this.payService.paymentServiceProceed(orderReady).subscribe(
      {
        next: (response) => {
          window.location.href = response.redirectLink;
        },
        error: () => {
          this.loading = false;
        }
      }
    );
  }




}
