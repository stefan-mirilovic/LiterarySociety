import { Component, OnInit } from '@angular/core';
import {LiterarySociety} from '../model/LiterarySociety';
import {Router} from "@angular/router"
 

@Component({
	selector: 'register',
	templateUrl: 'register.component.html',
	styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit {

	literarySociety : LiterarySociety = {name:'', username:'',password:'',email:'' };

	constructor(private router: Router) {

	}
	ngOnInit() { }

	onRegister() {
		console.log(this.literarySociety.name + this.literarySociety.username + this.literarySociety.password +this.literarySociety.email);
		this.router.navigate(['/login']);		
	}
}