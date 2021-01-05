import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IssuerDetails } from 'src/app/model/issuer-details';
import { BankService } from 'src/app/service/bank.service';

@Component({
  selector: 'app-issuer-details-form',
  templateUrl: './issuer-details-form.component.html',
  styleUrls: ['./issuer-details-form.component.css']
})
export class IssuerDetailsFormComponent implements OnInit {
  paymentId: number;
  checkoutForm;

  constructor(
    route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private BankService: BankService,
    private toastr: ToastrService
    ) {
    const temp: Observable<number> = route.params.pipe(map(p => p.id))
    temp.subscribe( id => {
      if (id)
        this.paymentId = id;
    });
  }

  ngOnInit(): void {
    this.checkoutForm = this.formBuilder.group({
      cardNumber: new FormControl("", [Validators.required]),
      expDate: new FormControl("", [Validators.required]),
      securityCode: new FormControl("", [Validators.required]),
      cardHolderName: new FormControl("", [Validators.required]),
    });
  }

onSubmit(customerData) {
  let issuerDetails = new IssuerDetails(this.checkoutForm.value.cardNumber, this.checkoutForm.value.securityCode,
    this.checkoutForm.value.cardHolderName, this.checkoutForm.value.expDate, this.paymentId)  
    this.BankService.purchase(issuerDetails).subscribe(
      {
        next: (response) => {
          window.location.href = response.url;
        },
        error: data => {
          if (data.error && typeof data.error === "string")
            this.toastr.error(data.error);
          else
            this.toastr.error("Error paying!");
        }
      }
    );
  }
}
