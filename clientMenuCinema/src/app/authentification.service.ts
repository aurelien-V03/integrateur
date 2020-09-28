import { logging } from 'protractor';
import { User, auth } from 'firebase';
import { RequeteServeurService } from './requete-serveur.service';
import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthentificationService {

  constructor(private requeteServeur: RequeteServeurService) { }

  async seLoger(u: User) {
    return await this.requeteServeur.POST('http://localhost:8090/api/client/authentification',
      { login: u.uid, nom: u.displayName }).catch((err: HttpErrorResponse) => {
        alert("LE SERVEUR METIER N'EST PAS DISPONIBLE")
      });
  }
  async getInfosclient(idClient:string){
    return await this.requeteServeur.POST('http://localhost:8090/api/client/infos',
    { login:idClient}).catch((err: HttpErrorResponse) => {
      alert("LE SERVEUR METIER N'EST PAS DISPONIBLE")
    });
  }

}
