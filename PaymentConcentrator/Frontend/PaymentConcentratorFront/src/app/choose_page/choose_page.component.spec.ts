import { TestBed, inject } from '@angular/core/testing';

import { Choose_pageComponent } from './choose_page.component';

describe('a choose_page component', () => {
	let component: Choose_pageComponent;

	// register all needed dependencies
	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [
				Choose_pageComponent
			]
		});
	});

	// instantiation through framework injection
	beforeEach(inject([Choose_pageComponent], (Choose_pageComponent) => {
		component = Choose_pageComponent;
	}));

	it('should have an instance', () => {
		expect(component).toBeDefined();
	});
});