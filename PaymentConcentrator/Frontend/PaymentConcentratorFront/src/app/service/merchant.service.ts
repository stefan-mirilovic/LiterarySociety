import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CardDetails } from '../model/CardDetails';
import { Merchant } from '../model/Merchant';
import { PaymentService } from '../model/PaymentService';

@Injectable({
  providedIn: 'root'
})
export class MerchantService {

  constructor(private http: HttpClient) { }

  public create(data: Merchant): Observable<Merchant>{
    return this.http.post<Merchant>(`${environment.baseUrl}/merchants`, data);
  }

  public login(data: Merchant): Observable<Merchant>{
    return this.http.post<Merchant>(`${environment.baseUrl}/merchants/login`, data);
  }

  public getPaymentTypes(id: string): Observable<PaymentService[]> {
    return this.http.get<PaymentService[]>(`${environment.baseUrl}/merchants/${id}/payment-types`);
  }

  public addPaymentTypeBank(id: string, data: CardDetails): Observable<PaymentService> {
    return this.http.post<PaymentService>(`${environment.baseUrl}/merchants/${id}/payment-types/bank`, data);
  }

}
