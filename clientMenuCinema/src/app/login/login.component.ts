import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { auth, User } from 'firebase/app';
import { AngularFireAuth } from '@angular/fire/auth';
import { RequeteServeurService } from '../requete-serveur.service';
import { AuthentificationService } from '../authentification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [RequeteServeurService]
})
export class LoginComponent implements OnInit {
  public button: HTMLElement;
  private isConnecter = false;
  @Input() idMenu: string;
  @Input() lesParatremets: { idMovie: string; idMenu: string; livr: string; idClient: string };
  @Input() infoFilmsetPlats: any;
  @Output() loginUserEventMitter = new EventEmitter<any>();
  @Output() envoieDonner = new EventEmitter<any>();



  panier: boolean;
  valider: boolean;
  user: User;
  modifierInf: boolean;
  utilisateur: { idClient: string, nom: string, photo: string, email: string, tel: string, adresse: string } = {};


  constructor(private afAuth: AngularFireAuth, private connection: AuthentificationService, private requeteServeur: RequeteServeurService) {
    afAuth.user.subscribe(async (u: User) => {
      console.log('L’utilisateur Firebasse est ', u);
      if (u != null) {
        this.idMenu = '';
        this.isConnecter = true;
        this.user = u;
        this.loginUserEventMitter.emit(this.user);
        // On contacte le serveur métier pour l'informer si un nouvelutilisateur existe ou bien on le creer:
        const reponseServeur = await this.connection.seLoger(this.user);
        if (reponseServeur == undefined) {
          this.isConnecter = false;
          this.user = undefined;
          u = null;
          console.log("LE SERVEUR METIER N'EST PAS DISPONIBLE");
          this.loginUserEventMitter.emit(this.user);
        }
        const infosClients = await this.connection.getInfosclient(u.uid);
        //console.log("Les infos recuperer sur la BD", infosClients.body)
        this.unUser(infosClients.body);
        //test à suprimer apres
        if (this.utilisateur.nom === "--") {
          this.utilisateur.nom = this.user.displayName
        }
        if (this.utilisateur.idClient === "--") {
          this.utilisateur.idClient = this.user.uid;
        }
        if (this.utilisateur.email === "--") {
          this.utilisateur.email = this.user.email
        }
        if (this.utilisateur.photo === "--") {
          this.utilisateur.photo = this.user.photoURL
        }
        this.envoieDonner.emit(this.utilisateur);
      }
    });
  }
  async loginGoogle() {
    await this.afAuth.signInWithPopup(new auth.GoogleAuthProvider());
    this.isConnecter = true;
  }
  async logoutGoogle() {
    await this.afAuth.signOut();
    this.isConnecter = false;
    // L'attribut User du parent devient undefined
    this.loginUserEventMitter.emit(undefined);
  }
  async passerCommander() {
    // URL API
    let urlAPI: string = 'http://localhost:8090/api/commande/valider';
    const responseServeur = await this.requeteServeur.POST(urlAPI, this.lesParatremets);
    alert("Votre commande a ete validée");
    this.idMenu = '';
    this.valider = true;
  }
  voirFacture() {
    //on masque tout sauf la facuture
    document.getElementById("principale").style.display = 'none';
    document.getElementById("facture").style.display = 'block';
  }
  ngOnInit(): void {
    // Evenement clique button 'LOGIN'
    this.idMenu = '';
    this.button = document.getElementById('buttonLogin');
  }

  get nomUser() {
    if (this.utilisateur !== undefined) {
      return this.utilisateur.nom;
    }
  }
  get estConnecter() {
    return this.isConnecter;
  }
  get email() {
    if (this.utilisateur !== undefined) {
      return this.utilisateur.email;
    }
  }
  getProfilPhotoPath() {
    if (this.utilisateur !== undefined) {
      return this.utilisateur.photo;
    }
  }
  modifierInfos() {
    this.modifierInf = true;
    
    document.getElementById("cacher").style.display = 'none';
    document.getElementById("principale").style.display = 'none';
    document.getElementById("divBlockInfoClient").style.display = 'block';
  }
  unUser(xmlCommandes: string) {
    let nameSpace: string = 'http://integrateur/menu';
    let domparser = new DOMParser();
    let doc: Document = domparser.parseFromString(xmlCommandes, 'text/xml');
    let listCommandes = doc.getElementsByTagName('p:client');
    this.utilisateur.idClient = listCommandes.item(0).getElementsByTagNameNS(nameSpace, 'id').item(0).textContent;
    this.utilisateur.nom = listCommandes.item(0).getElementsByTagNameNS(nameSpace, 'nom').item(0).textContent;
    this.utilisateur.photo = listCommandes.item(0).getElementsByTagNameNS(nameSpace, 'photo').item(0).textContent;
    this.utilisateur.email = listCommandes.item(0).getElementsByTagNameNS(nameSpace, 'email').item(0).textContent;
    this.utilisateur.tel = listCommandes.item(0).getElementsByTagNameNS(nameSpace, 'tel').item(0).textContent;
    this.utilisateur.adresse = listCommandes.item(0).getElementsByTagNameNS(nameSpace, 'adresse').item(0).textContent;
  }
}
