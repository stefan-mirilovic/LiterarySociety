import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Order} from "../model/Order";
import {Observable} from "rxjs";
import { BankResponse } from "../model/BankResponse";
import { environment } from "src/environments/environment";
import {RedirectDto} from "../model/RedirectDto";

@Injectable({
  providedIn: 'root'
})

export class PayService {

  constructor(private http: HttpClient) {
  }

  public getOrder(): Observable<Order>{
    return this.http.get <Order>('http://localhost:8081/api/chose/payment');
  }

  public paymentProceed(order: Order){
    return this.http.post<BankResponse>(`${environment.baseUrl}/pay/bank`, order);
  }

  public paymentServiceProceed(order: Order):Observable<RedirectDto>{
    return this.http.post<RedirectDto>(`${environment.baseUrl}/payment/type/pay`,order);
  }

  public discoverAllPaymentType(): Observable<any>{
    return this.http.get <any>(`${environment.baseUrl}/get/all/payments`);
  }


}
