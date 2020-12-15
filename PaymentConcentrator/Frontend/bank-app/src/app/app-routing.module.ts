import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IssuerDetailsFormComponent } from './forms/issuer-details-form/issuer-details-form.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'purchase',
    pathMatch: 'full'
  },
  {
    path: 'purchase/:id',
    component: IssuerDetailsFormComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
