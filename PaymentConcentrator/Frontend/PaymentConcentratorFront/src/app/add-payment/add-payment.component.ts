import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CardDetails } from '../model/CardDetails';
import { PaymentService } from '../model/PaymentService';
import { GenericService } from '../service/generic.service';
import { MerchantService } from '../service/merchant.service';
import { PayService } from '../service/pay-service';

@Component({
  selector: 'app-add-payment',
  templateUrl: './add-payment.component.html',
  styleUrls: ['./add-payment.component.css']
})
export class AddPaymentComponent implements OnInit {
  allPaymentServices: PaymentService[];
  paymentMethod: PaymentService;
  bankForm: any;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private payService: PayService,
    private merchantService: MerchantService
  ) { }

  ngOnInit(): void {
    this.payService.discoverAllPaymentType().subscribe(
      res => {
        this.allPaymentServices = res;
      }
    );
    this.bankForm = this.formBuilder.group({
      cardNumber: new FormControl("", [Validators.required]),
      expDate: new FormControl("", [Validators.required]),
      securityCode: new FormControl("", [Validators.required]),
      cardHolderName: new FormControl("", [Validators.required]),
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

}
