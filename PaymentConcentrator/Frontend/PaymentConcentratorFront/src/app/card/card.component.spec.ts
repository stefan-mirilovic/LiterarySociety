import { TestBed, inject } from '@angular/core/testing';

import { CardComponent } from './card.component';

describe('a card component', () => {
	let component: CardComponent;

	// register all needed dependencies
	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [
				CardComponent
			]
		});
	});

	// instantiation through framework injection
	beforeEach(inject([CardComponent], (CardComponent) => {
		component = CardComponent;
	}));

	it('should have an instance', () => {
		expect(component).toBeDefined();
	});
});