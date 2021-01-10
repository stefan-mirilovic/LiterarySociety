import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from 'src/app/login/login.component';
import { RegisterComponent } from 'src/app/register/register.component';
import { AddPaymentComponent } from './add-payment/add-payment.component';
import { Choose_pageComponent } from './choose_page/choose_page.component';
import {ChosePaymentComponent} from "./chose-payment/chose-payment.component";
import { MerchantPageComponent } from './merchant-page/merchant-page.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {path: 'register' , component: RegisterComponent},
  {path: 'login' , component: LoginComponent},
  {path: 'choose/type', component: Choose_pageComponent},
  {path: 'choose/payment', component: ChosePaymentComponent},
  {
    path: 'dashboard',
    component: MerchantPageComponent
  },
  {
    path: 'add-payment',
    component: AddPaymentComponent
  }

]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {



 }
