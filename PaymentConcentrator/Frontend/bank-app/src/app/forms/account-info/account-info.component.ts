import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountAddFunds } from 'src/app/model/account-add-funds';
import { AccountService } from 'src/app/service/account.service';
import { environment } from 'src/environments/environment';
import { AddFundsDialogComponent } from '../add-funds-dialog/add-funds-dialog.component';

@Component({
  selector: 'app-account-info',
  templateUrl: './account-info.component.html',
  styleUrls: ['./account-info.component.css']
})
export class AccountInfoComponent implements OnInit {
  id: number;
  addFundsToggle: boolean;
  registerForm;
  bankName = environment.bankName

  constructor(
    route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private accountService: AccountService,
    private toastr: ToastrService,
    private dialog: MatDialog
  ) {
    const temp: Observable<number> = route.params.pipe(map(p => p.id))
    temp.subscribe(id => {
      if (id)
        this.id = id;
    });
  }

  ngOnInit(): void {
    this.addFundsToggle = false;
    this.registerForm = this.formBuilder.group({
      name: new FormControl("", [Validators.required]),
      surname: new FormControl("", [Validators.required]),
      cardNumber: new FormControl("", [Validators.required]),
      securityCode: new FormControl("", [Validators.required]),
      expDate: new FormControl("", [Validators.required]),
      funds: new FormControl(null, [Validators.required]),
      addFunds: new FormControl(0, [Validators.required])
    });
    this.fetchData();
  }

  fetchData() {
    this.accountService.getAccount(this.id).subscribe(
      {
        next: (result) => {
          this.registerForm.patchValue({
            name: result.name,
            surname: result.surname,
            cardNumber: result.number,
            securityCode: result.securityCode,
            expDate: result.expDate,
            funds: result.funds
          })
        },
        error: data => {
          if (data.error && typeof data.error === "string")
            this.toastr.error(data.error);
          else
            this.toastr.error("Error fetching data!");
        }
      }
    );
  }

  onSubmit(data) {
    let account = new AccountAddFunds(this.id, this.registerForm.value.addFunds);
    this.accountService.addFunds(account).subscribe(
      {
        next: (result) => {
          //this.toastr.success("Added funds!");
          this.registerForm.patchValue({
            name: result.name,
            surname: result.surname,
            cardNumber: result.number,
            securityCode: result.securityCode,
            funds: result.funds
          })
        },
        error: data => {
          if (data.error && typeof data.error === "string")
            this.toastr.error(data.error);
          else
            this.toastr.error("Error adding funds!");
        }
      }
    );
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.width = "40vw";
    dialogConfig.minWidth = "450px";

    dialogConfig.data = {
      id: this.id,
      funds: this.registerForm.value.funds
    };
    this.dialog.open(AddFundsDialogComponent, dialogConfig);
    this.dialog.afterAllClosed.subscribe({
      next: () => {
        this.fetchData();
      }
    })
  }
}
