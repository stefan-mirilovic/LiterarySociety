import { Component, OnInit } from '@angular/core';
import { LiterarySociety } from '../model/LiterarySociety';
import {Router} from "@angular/router"
@Component({
	selector: 'login',
	templateUrl: 'login.component.html',
	styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

	literarySociety : LiterarySociety = {name:'',email:'',password:'',username:''};
	
	constructor (private router: Router) {

	}

	ngOnInit() { }

	onLogin() {
		this.router.navigate(['/choose/type']);
	}
}