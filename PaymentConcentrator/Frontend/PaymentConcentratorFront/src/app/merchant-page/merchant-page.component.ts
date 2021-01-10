import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PaymentService } from '../model/PaymentService';
import { MerchantService } from '../service/merchant.service';

@Component({
  selector: 'app-merchant-page',
  templateUrl: './merchant-page.component.html',
  styleUrls: ['./merchant-page.component.css']
})
export class MerchantPageComponent implements OnInit {
  email: string;
  paymentTypes: PaymentService[];

  constructor(
    private router: Router,
    private merchantService: MerchantService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.email = localStorage.getItem("loggedIn");
    this.merchantService.getPaymentTypes(localStorage.getItem("loggedInMerchantId")).subscribe({
			next: (result) => {
        this.paymentTypes = result;
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Failed to load payment methods!");
			  }
		});
  }

  logOut() {
    localStorage.removeItem("loggedIn");
    localStorage.removeItem("loggedInMerchantId")
    this.router.navigate(['/login']);		
  }

}
