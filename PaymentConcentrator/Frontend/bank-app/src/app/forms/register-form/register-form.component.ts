import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AccountCreate } from 'src/app/model/account-create';
import { AccountService } from 'src/app/service/account.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  registerForm;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private accountService: AccountService,
    private toastr: ToastrService
    ) { }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name: new FormControl("", [Validators.required]),
      surname: new FormControl("", [Validators.required])
    });
  }

  onSubmit(data) {
    let account = new AccountCreate(this.registerForm.value.name, this.registerForm.value.surname)  
      this.accountService.register(account).subscribe(
        {
          next: (response) => {
            this.toastr.success("Successfully registered!");
            this.router.navigate([`/account/${response.id}`])
          },
          error: data => {
            if (data.error && typeof data.error === "string")
              this.toastr.error(data.error);
            else
              this.toastr.error("Error registering!");
          }
        }
      );
    }
  
}
