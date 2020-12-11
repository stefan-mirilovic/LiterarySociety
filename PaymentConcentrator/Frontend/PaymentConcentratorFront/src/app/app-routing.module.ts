import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from 'src/app/login/login.component';
import { RegisterComponent } from 'src/app/register/register.component';
import { Choose_pageComponent } from './choose_page/choose_page.component';
import {ChosePaymentComponent} from "./chose-payment/chose-payment.component";


const routes: Routes = [
  {path: 'register' , component: RegisterComponent},
  {path: 'login' , component: LoginComponent},
  {path: 'choose/type', component: Choose_pageComponent},
  {path: 'chose/payment', component: ChosePaymentComponent}

]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {



 }
