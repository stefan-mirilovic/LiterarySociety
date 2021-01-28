import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { AccountAddFunds } from 'src/app/model/account-add-funds';
import { AccountService } from 'src/app/service/account.service';

@Component({
  selector: 'app-add-funds-dialog',
  templateUrl: './add-funds-dialog.component.html',
  styleUrls: ['./add-funds-dialog.component.css']
})
export class AddFundsDialogComponent implements OnInit {
  removeMode: boolean;
  funds: number;
  id: number;
  balanceSuffix: string;
  balanceSuffixPositive: boolean;

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    @Inject(MAT_DIALOG_DATA) data
  ) {
    this.funds = data.funds;
    this.id = data.id
  }

  ngOnInit(): void {
    this.removeMode = false;
    this.balanceSuffixPositive = true;
  }

  addFunds(amount: number) {
    amount = this.removeMode ? -1 * amount: amount;
    let account = new AccountAddFunds(this.id, amount);
    this.accountService.addFunds(account).subscribe(
      {
        next: (result) => {
          this.funds = this.funds + amount;
          this.balanceSuffix = amount >= 0 ? "+" + amount + "€" : amount.toString() + "€";
          this.balanceSuffixPositive = amount >= 0;
        },
        error: data => {
          if (data.error && typeof data.error === "string")
            this.toastr.error(data.error);
          else
            this.toastr.error("Error adding funds!");
        }
      });
  }


}
