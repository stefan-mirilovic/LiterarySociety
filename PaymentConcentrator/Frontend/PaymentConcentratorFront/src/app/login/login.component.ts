import { Component, OnInit } from '@angular/core';
import { LiterarySociety } from '../model/LiterarySociety';
import {Router} from "@angular/router"
import { MerchantService } from '../service/merchant.service';
import { Merchant } from '../model/Merchant';
import { ToastrService } from 'ngx-toastr';
@Component({
	selector: 'login',
	templateUrl: 'login.component.html',
	styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

	literarySociety : LiterarySociety = {name:'',email:'',password:'',username:''};
	
	constructor (
		public router: Router,
		private merchantService: MerchantService,
		private toastr: ToastrService
		) {

	}

	ngOnInit() { }

	onLogin() {
		let merchant: Merchant = {id:undefined, email:this.literarySociety.email, merchantId:undefined, merchantPassword:this.literarySociety.password};
		this.merchantService.login(merchant).subscribe({
			next: (result) => {
				this.toastr.success("Login successful!");
				localStorage.setItem("loggedIn", result.email)
				localStorage.setItem("loggedInMerchantId", result.merchantId)
				this.router.navigate(['/dashboard']);		
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Error logging in!");
			  }
		});
	}
}