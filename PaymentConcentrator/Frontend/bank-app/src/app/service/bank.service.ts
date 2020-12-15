import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IssuerDetails } from '../model/issuer-details';

@Injectable({
  providedIn: 'root'
})
export class BankService {

  constructor(private http: HttpClient) { }

  purchase(data: IssuerDetails): Observable<string> {
    return this.http.post<string>(`${environment.baseUrl}/pay`, data);
  }
}
