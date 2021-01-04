import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Account } from '../model/account';
import { AccountAddFunds } from '../model/account-add-funds';
import { AccountCreate } from '../model/account-create';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) {
  }
  
  register(data: AccountCreate): Observable<Account> {
    return this.http.post<Account>(`${environment.baseUrl}/accounts`, data);
  }

  addFunds(data: AccountAddFunds): Observable<Account> {
    return this.http.post<Account>(`${environment.baseUrl}/accounts/add-funds`, data)
  }

  getAccount(id: number): Observable<Account> {
    return this.http.get<Account>(`${environment.baseUrl}/accounts/${id}`)
  }
}
