import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IssuerDetailsFormComponent } from './forms/issuer-details-form/issuer-details-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RegisterFormComponent } from './forms/register-form/register-form.component';
import { AccountInfoComponent } from './forms/account-info/account-info.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material-module';
import { AddFundsDialogComponent } from './forms/add-funds-dialog/add-funds-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    IssuerDetailsFormComponent,
    RegisterFormComponent,
    AccountInfoComponent,
    AddFundsDialogComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    ToastrModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
