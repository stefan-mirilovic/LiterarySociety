import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GenericService {

  constructor(
    private http: HttpClient
    ) { }

  public post(url: string, data: any): Observable<any> {
    return this.http.post<any>(`${url}`, data);
  }
}
