import { Component, OnInit } from '@angular/core';
import { PaymentType } from '../model/PaymentType';

@Component({
	selector: 'choose_page',
	templateUrl: 'choose_page.component.html',
	styleUrls: ['./choose_page.component.css']
})

export class Choose_pageComponent implements OnInit {

	paymentsTypes : PaymentType[] = [
		{type:'Bank',description: 'Description about bank.Text about something to see align. La la la la.',choose:false },
		{type:'PayPal',description:' Description about payPal.Text about something to see align. La la la la.',choose:false},
		{type:'BitCoin',description:'Description about bitCoin.Text about something to see align. La la la la.',choose:false}]


	ngOnInit() { }

	
}