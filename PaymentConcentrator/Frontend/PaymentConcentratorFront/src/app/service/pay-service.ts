import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Order} from "../model/Order";
import {Observable} from "rxjs";
import { BankResponse } from "../model/BankResponse";
import { environment } from "src/environments/environment";

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


}
