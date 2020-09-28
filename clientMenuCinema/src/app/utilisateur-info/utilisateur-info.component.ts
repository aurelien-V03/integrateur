import { HttpResponse } from '@angular/common/http';
import { User } from 'firebase';
import { Component, OnInit, Input } from '@angular/core';
import { RequeteServeurService } from '../requete-serveur.service';

@Component({
  selector: 'app-utilisateur-info',
  templateUrl: './utilisateur-info.component.html',
  styleUrls: ['./utilisateur-info.component.scss']
})
export class UtilisateurInfoComponent implements OnInit {
  @Input() utilisateur: { idClient: string, nom: string, photo: string, email: string, tel: string, adresse: string } = {};
  isConnecter :boolean;

  constructor(private requeteServeur: RequeteServeurService) { }

  ngOnInit(): void {
    console.log("recu ", this.user)
  }
  getDonner(event){
    console.log("donnne recue de lautre",event);
    this.utilisateur=event;
    this.isConnecter=true;
  }
  // Quand l'utilisateur veut modifier ses informations
  async modifierInfoClient() {
    const newName: string = document.getElementById('newName').value;
    const newPhotoUrl: string = document.getElementById('newPhotoUrl').value;
    const newMail: string = document.getElementById('newMail').value;
    const newNumberPhone: string = document.getElementById('newNumber').value;
    const newAdresse: string = document.getElementById('newAdresse').value;
    const urlAPI: string = 'http://localhost:8090/api/client/modifierInfos';
    let urlAPIParams: { idClient: string; nom: string; photo: string; email: string; tel: string; adresse: string } = {};
    urlAPIParams.idClient = this.utilisateur.idClient;
    // SI L'UTILISATEUR VEUT MODIFIER SON NOM
    if (newName.length > 0) {
      urlAPIParams.nom = newName;
    }
    // SI L'UTILISATEUR VEUT MODIFIER SA PHOTO DE PROFIL
    if (newPhotoUrl.length > 0) {
      urlAPIParams.photo = newPhotoUrl;
    }
    // SI L'UTILISATEUR VEUT MODIFIER SON MAIL
    if (newMail.length > 0) {
      urlAPIParams.email = newMail;
    }
    // SI L'UTILISATEUR VEUT MODIFIER SON NUMERO DE TELEPHONE
    if (newNumberPhone.length > 0) {
      urlAPIParams.tel = newNumberPhone;
    }
    // SI L'UTILISATEUR VEUT MODIFIER SON ADRESSE
    if (newAdresse.length > 0) {
      urlAPIParams.adresse = newAdresse;
    }
    const responseServeur: HttpResponse = await this.requeteServeur.POST(urlAPI, urlAPIParams);
    //this.requeteServeur.POST(urlAPI,urlAPIParams);
    if (responseServeur.body === 'true') {
      alert("Infos modifier avec succes")
    }
    else {
      alert("Il n'ya pas eu de mdofication")
    }
  }

}
