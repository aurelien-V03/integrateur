import { Injectable } from '@angular/core';
import { HttpResponse, HttpParams, HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RequeteServeurService {

  constructor(private http: HttpClient) { 
    console.log("SERVICE REQUETE");
  }

  public async POST(url: string, params: { [key: string]: string }): Promise<HttpResponse<string>> {
    const P = new HttpParams({ fromObject: params });
    return this.http.post(url, P, {
      observe: 'response',
      responseType: 'text',
      headers: { 'content-type': 'application/x-www-form-urlencoded' }
    }).toPromise()
  }
}
