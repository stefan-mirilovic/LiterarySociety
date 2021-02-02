import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddPaymentDialogComponent } from '../add-payment-dialog/add-payment-dialog.component';
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
  loading: boolean = false;

  constructor(
    public router: Router,
    private merchantService: MerchantService,
    private toastr: ToastrService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.email = localStorage.getItem("loggedIn");
    this.fetchData();
  }

  fetchData() {
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

  openDialog(paymentMethod) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.width = "40vw";
    dialogConfig.minWidth = "450px";

    dialogConfig.data = {
      paymentMethod: paymentMethod
    };
    this.dialog.open(AddPaymentDialogComponent, dialogConfig);
    this.dialog.afterAllClosed.subscribe({
      next: () => {
        this.fetchData();
      }
    })
  }

  logOut() {
    localStorage.removeItem("loggedIn");
    localStorage.removeItem("loggedInMerchantId")
    this.router.navigate(['/login']);		
  }

}
