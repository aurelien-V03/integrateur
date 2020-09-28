import { Component, OnInit, Input } from '@angular/core';
import { NgForm } from "@angular/forms";
import { Commande } from '../menus-data/commande';
import { User } from 'firebase';
import { RequeteServeurService } from '../requete-serveur.service';

@Component({
  selector: 'app-commande-client',
  templateUrl: './commande-client.component.html',
  styleUrls: ['./commande-client.component.scss'],
  providers: [RequeteServeurService]
})
export class CommandeClientComponent implements OnInit {

  // date de la commande
  selectedOptionsTime: string = "";
  // Liste des commandes du client
  commandesClient: Commande[] = [];
  enCours: boolean=true;

  @Input() user: User;


  displayCommandes = false;

  constructor(private requeteServeur: RequeteServeurService) { }

  ngOnInit(): void {
    document.getElementById('commandeFindButton').addEventListener('click', () => {
      this.displayCommandes = !this.displayCommandes;
    });
  }

  // methode qui permet de chercher dans la liste de commande  'commandesClients' la liste des commandes
  // qui correspondent au 2 parametres de la fonction
  getCommandesFilter(dateCommande, ingredient) {

  }
  // Quand l'utilisateur clique sur le bouton "Rechercher commande"
  async onSubmit(form: NgForm) {

    // On recupere la date saisie par l'utilisateur
    this.selectedOptionsTime = document.querySelector('input[type="date"]').value;

    // On vide la liste des commandes clients
    this.commandesClient = [];
    // On recupere la valeur saisie
    const composition: string = form.value['commandeComposition'];
   
    // URL de l'API
    let urlAPI: string = 'http://localhost:8090/api/commande/rechercher';
    // Objet parametre pour l'URL
    let urlAPIParams: { idClient: string; dateCommande: string; ingredient } = { idClient: this.user.uid };


    // L'utilisateur n'a entree aucune information
    if (composition.length == 0 && this.selectedOptionsTime.length == 0) {

    }
    // compisition vide et date remplis 
    else if (composition.length == 0 && this.selectedOptionsTime.length > 0) {
      urlAPIParams.dateCommande = this.selectedOptionsTime.substr(2);

    }
    // compisition remplis et date vide
    else if (composition.length > 0 && this.selectedOptionsTime.length == 0) {
      urlAPIParams.ingredient = composition;
    }
    // tous les champs remplis
    else {
      urlAPIParams.dateCommande = this.selectedOptionsTime;
      urlAPIParams.ingredient = composition;
    }
    //pour faire un test decommante cet id de la ligne suivante qui existe dans la BD
    //let id='A123456';
    console.log('API = ' + urlAPI);
    console.log('les paramettres:',urlAPIParams)
    //rajouter l'objet urlAPIParams comme paramettre quand le filtre de l'API/rechercheCommande serra OK
    const responseServeur=await this.requeteServeur.POST(urlAPI,urlAPIParams);
    let xmlCommandes = responseServeur.body;
    console.log("retour recherche commande:",responseServeur.body);
    //urlAPIParams.idClient;
    //parser le fichier xml recuperer
    let nameSpace: string = 'http://integrateur/menu';  
    let domparser = new DOMParser();
    let doc: Document = domparser.parseFromString(xmlCommandes, 'text/xml');
    let listCommandes = doc.getElementsByTagName('p:commande');
    for (let i = 0; i < listCommandes.length; i++) {
      let newCommande: Commande={};
      newCommande.numeroCommande = listCommandes.item(i).getElementsByTagNameNS(nameSpace, 'id').item(0).textContent;
      newCommande.numeroClient = listCommandes.item(i).getElementsByTagNameNS(nameSpace,'idClient').item(0).textContent;
      newCommande.filmCommande = listCommandes.item(i).getElementsByTagNameNS(nameSpace ,'idFilms').item(0).textContent;
      newCommande.dateCommande = listCommandes.item(i).getElementsByTagNameNS(nameSpace ,'date').item(0).textContent;
      newCommande.prix = listCommandes.item(i).getElementsByTagNameNS(nameSpace ,'prix').item(0).textContent;
      newCommande.adresseLivraison = listCommandes.item(i).getElementsByTagNameNS(nameSpace, 'adresseLivraison').item(0).textContent;
      // on recupere la liste des id de plats
      let listIdPlatsXML = listCommandes.item(i).getElementsByTagNameNS(nameSpace, 'idPlats');
      let listIdPlats = [];
      for (let j = 0; j < listIdPlatsXML.length; j++) {
        listIdPlats.push(listIdPlatsXML.item(j).textContent);
      }
      newCommande.listeMenusCommandes = listIdPlats;
      this.commandesClient.push(newCommande);
      console.log(newCommande);
    }
    this.enCours=false;
  }
}
