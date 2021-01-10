import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CardDetails } from '../model/CardDetails';
import { MerchantConnect } from '../model/MerchantConnect';
import { PaymentService } from '../model/PaymentService';
import { MerchantService } from '../service/merchant.service';
import { PayService } from '../service/pay-service';

@Component({
  selector: 'app-add-payment',
  templateUrl: './add-payment.component.html',
  styleUrls: ['./add-payment.component.css']
})
export class AddPaymentComponent implements OnInit {
  allPaymentServices: PaymentService[];
  paymentMethod: PaymentService = undefined;
  bankForm: any;
  genericForm: any;

  constructor(
    public router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private payService: PayService,
    private merchantService: MerchantService
  ) { }

  ngOnInit(): void {
    this.payService.discoverAllPaymentType().subscribe(
      res => {
        this.allPaymentServices = res;
        this.merchantService.getPaymentTypes(localStorage.getItem("loggedInMerchantId")).subscribe({
          next: (result) => {
            let temp: PaymentService[] = [];
            for (let p of this.allPaymentServices) {
              if (result.findIndex(ps => ps.name === p.name) === -1) {
                temp.push(p);
              }
            }
            this.allPaymentServices = temp;
          },
          error: data => {
            if (data.error && typeof data.error === "string")
              this.toastr.error(data.error);
            else
              this.toastr.error("Error getting payment services!");
            }
        });
      }
    );
    this.bankForm = this.formBuilder.group({
      cardNumber: new FormControl("", [Validators.required]),
      expDate: new FormControl("", [Validators.required]),
      securityCode: new FormControl("", [Validators.required]),
      cardHolderName: new FormControl("", [Validators.required]),
    });
    this.genericForm = this.formBuilder.group({
      username: new FormControl("", [Validators.required]),
      password: new FormControl(""),
    });
  }

  onBankSubmit() {
    let cardDetails = new CardDetails(this.bankForm.value.cardNumber, this.bankForm.value.securityCode, this.bankForm.value.cardHolderName,
      this.bankForm.value.expDate, this.paymentMethod.name, this.paymentMethod.url);
    this.merchantService.addPaymentTypeBank(localStorage.getItem("loggedInMerchantId"), cardDetails).subscribe({
			next: () => {
        this.toastr.success("Added payment method!");
        this.router.navigate(['/dashboard']);
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Failed to load payment methods!");
			  }
		});
  }

  onGenericSubmit() {
    let temp = new MerchantConnect(this.genericForm.value.username, this.genericForm.value.password, this.paymentMethod.name, this.paymentMethod.url);
    this.merchantService.addPaymentType(localStorage.getItem("loggedInMerchantId"), temp).subscribe({
			next: () => {
        this.toastr.success("Added payment method!");
        this.router.navigate(['/dashboard']);
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Failed to load payment methods!");
			  }
		});
  }

}
