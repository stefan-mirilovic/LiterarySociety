import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IssuerDetails } from '../model/issuer-details';
import { TransactionCompleted } from '../model/transaction-completed';

@Injectable({
  providedIn: 'root'
})
export class BankService {

  constructor(private http: HttpClient) { }

  purchase(data: IssuerDetails): Observable<TransactionCompleted> {
    return this.http.post<TransactionCompleted>(`${environment.baseUrl}/pay-part-2`, data);
  }
}
