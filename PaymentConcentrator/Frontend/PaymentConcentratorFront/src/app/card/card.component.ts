import { Component, Input, OnInit } from '@angular/core';
import { PaymentType } from '../model/PaymentType';

@Component({
	selector: 'card',
	templateUrl: 'card.component.html',
	styleUrls: ['./card.component.css']
})

export class CardComponent implements OnInit {

	@Input() paymentType : PaymentType

	ngOnInit() { }

	typeYes() {
		this.paymentType.choose=true;
	}
}