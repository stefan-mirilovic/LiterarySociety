import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Order} from "../model/Order";
import {Observable} from "rxjs";

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
    return this.http.post("http://localhost:8081/api/pay/bank", order);
  }


}
