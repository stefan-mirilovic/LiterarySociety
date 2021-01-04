import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AccountInfoComponent } from './forms/account-info/account-info.component';
import { IssuerDetailsFormComponent } from './forms/issuer-details-form/issuer-details-form.component';
import { RegisterFormComponent } from './forms/register-form/register-form.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'register',
    pathMatch: 'full'
  },
  {
    path: 'purchase/:id',
    component: IssuerDetailsFormComponent,
  },
  {
    path: 'register',
    component: RegisterFormComponent,
  },
  {
    path: 'account/:id',
    component: AccountInfoComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
