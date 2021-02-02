import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { CardDetails } from '../model/CardDetails';
import { MerchantConnect } from '../model/MerchantConnect';
import { PaymentService } from '../model/PaymentService';
import { MerchantService } from '../service/merchant.service';
import { PayService } from '../service/pay-service';

@Component({
  selector: 'app-add-payment-dialog',
  templateUrl: './add-payment-dialog.component.html',
  styleUrls: ['./add-payment-dialog.component.css']
})
export class AddPaymentDialogComponent implements OnInit {
  allPaymentServices: PaymentService[];
  paymentMethod: PaymentService = undefined;
  bankForm: any;
  genericForm: any;
  editMode: boolean = false;
  loading: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private payService: PayService,
    private merchantService: MerchantService,
    private dialogRef: MatDialogRef<AddPaymentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data
  ) {
    if (data.paymentMethod) {
      this.paymentMethod = data.paymentMethod;
      this.editMode = true;
    }
  }

  ngOnInit(): void {
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
    this.payService.discoverAllPaymentType().subscribe(
      res => {
        this.allPaymentServices = res;
        if (!this.editMode) {
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
      }
    );
  }

  onBankSubmit() {
    let cardDetails = new CardDetails(this.bankForm.value.cardNumber, this.bankForm.value.securityCode, this.bankForm.value.cardHolderName,
      this.bankForm.value.expDate, this.paymentMethod.name, this.paymentMethod.url);
    this.merchantService.addPaymentTypeBank(localStorage.getItem("loggedInMerchantId"), cardDetails).subscribe({
			next: () => {
        this.toastr.success("Added payment method!");
        this.dialogRef.close();
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
        this.dialogRef.close();
      },
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Failed to add payment method!");
			  }
		});
  }

  delete() {
    this.merchantService.deletePaymentType(localStorage.getItem("loggedInMerchantId"), this.paymentMethod.id, null).subscribe({
			next: () => {
        this.toastr.success("Deleted payment method!");
        this.dialogRef.close();
      },
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Failed to delete payment method!");
			  }
		});
  }

}
