import { Component, OnInit } from '@angular/core';
import {LiterarySociety} from '../model/LiterarySociety';
import {Router} from "@angular/router"
import { MerchantService } from '../service/merchant.service';
import { Merchant } from '../model/Merchant';
import { ToastrService } from 'ngx-toastr';
 

@Component({
	selector: 'register',
	templateUrl: 'register.component.html',
	styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit {

	literarySociety : LiterarySociety = {name:'', username:'',password:'',email:'' };

	constructor(
		private router: Router,
		private merchantService: MerchantService,
		private toastr: ToastrService
	) {

	}
	ngOnInit() { }

	onRegister() {
		let merchant: Merchant = {id:undefined, email:this.literarySociety.email, merchantId:undefined, merchantPassword:this.literarySociety.password};
		this.merchantService.create(merchant).subscribe({
			next: (result) => {
				this.toastr.success("Registered successfully!");
				localStorage.setItem("loggedIn", result.email)
				this.router.navigate(['/dashboard']);		
				localStorage.setItem("loggedInMerchantId", result.merchantId)
			},
			error: data => {
				if (data.error && typeof data.error === "string")
				  this.toastr.error(data.error);
				else
				  this.toastr.error("Error registering!");
			  }
		});
	}
}