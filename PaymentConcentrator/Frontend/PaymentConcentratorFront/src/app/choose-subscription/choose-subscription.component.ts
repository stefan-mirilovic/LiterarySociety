import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Order } from '../model/Order';
import { PaymentService } from '../model/PaymentService';
import { MerchantService } from '../service/merchant.service';
import { PayService } from '../service/pay-service';

@Component({
  selector: 'app-choose-subscription',
  templateUrl: './choose-subscription.component.html',
  styleUrls: ['./choose-subscription.component.css']
})
export class ChooseSubscriptionComponent implements OnInit {
  allPaymentServices: PaymentService[];
  order: Order = {
    merchantId: '84074cf2-3d74-11eb-9d51-0242ac130002',
    amount: 3,
    paymentMethod: 'bank',
    successUrl: "https://screenmessage.com/hxqx",
    failedUrl: "https://screenmessage.com/keib",
    errorUrl: "https://screenmessage.com/gbvv",
    paymentUrl: "",
    cycles: 0,
    frequency: "MONTH",
    interval: "1"
  }
  loading: boolean = false;

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
          this.order.frequency = params.frequency;
          this.order.cycles = params.cycles;
          this.order.interval = params.interval;
          console.log(this.order);
        }
      }
    );

    /*this.merchantService.getPaymentTypes(this.order.merchantId).subscribe({
			next: (result) => {
        this.allPaymentServices = result;
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Error getting payment services!");
			  }
    });*/
    this.payService.discoverAllPaymentType().subscribe({
      next: (result) => {
        this.allPaymentServices = result;
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Error getting payment services!");
			  }
    })
  }

  proceed(orderReady: Order, paymentMethod: PaymentService) {
    orderReady.paymentMethod = paymentMethod.name.concat(':').concat(paymentMethod.url)
    orderReady.paymentUrl = paymentMethod.url;
    //alert(orderReady.paymentUrl);
    this.loading = true;
    this.payService.subscriptioServiceProceed(orderReady).subscribe(
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
