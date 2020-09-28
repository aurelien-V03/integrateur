import { Injectable } from '@angular/core';
import { Menu, Plat } from './menus-data/menus';
import { RequeteServeurService } from './requete-serveur.service';
import { HttpResponse, HttpParams, HttpClient, HttpErrorResponse } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class MenusService {

  constructor(private requeteServeur: RequeteServeurService, private http: HttpClient) {
    console.log("SERVICE MENU");
  }

  /* Methode qui retourne une liste de Menu
  */
  //xmlMenus: string
  async getMenusFromXML() {
    // Namespace des menus
    let ns: string = 'http://integrateur/carte';
    let listMenus = [];
    // URL de l'API

    let urlAPI = 'http://localhost:8090/api/menus';
    //a supprrimer quand api/menu sera OK
    //urlAPI = 'http://localhost:8090/api/commande/rechercher';
    const reponseServeurMenus: HttpResponse = await this.requeteServeur.POST(urlAPI, {}).catch((err: HttpErrorResponse) => {
      console.log("LE SERVEUR METIER N'EST PAS DISPONIBLE")
    });


    if (reponseServeurMenus !== undefined) {
      let domparser = new DOMParser();
      let doc: Document = domparser.parseFromString(reponseServeurMenus.body, 'text/xml');

      // liste des menus XML 
      let menuXML = doc.getElementsByTagNameNS(ns, 'Menu');
      // lite des plats XML
      let listePlatsDisponibles = doc.getElementsByTagNameNS(ns, 'Plats');
      // On recupere chaque menu
      for (let i = 0; i < menuXML.length; i++) {
        let menu: Menu = {};
        // On recupere le numero du menu
        let menuId = menuXML.item(i).getElementsByTagNameNS(ns, 'idMenu').item(0).textContent;

        // On recupere la liste des genres XML
        let listeGenreXML = menuXML.item(i).getElementsByTagNameNS(ns, 'genre');
        let listGenre: string[] = [];
        for (let i = 0; i < listeGenreXML.length; i++) {
          listGenre.push(listeGenreXML.item(i).textContent)
        }
        //console.log(listGenre);

        // On recupere la liste des plats
        let listPlatsNumberXML = menuXML.item(i).getElementsByTagNameNS(ns, 'idPlat');
        let listePlatsMenu = [];
        for (let i = 0; i < listPlatsNumberXML.length; i++) {
          let numeroPlat: string = listPlatsNumberXML.item(i).textContent;
          //console.log(numeroPlat);
          // On recupere l'XML correspondant a ce numero
          let platCorrespondant;
          for (let k = 0; k < listePlatsDisponibles.length; k++) {
            if (listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'idPlat').item(0).textContent == numeroPlat) {
              platCorrespondant = listePlatsDisponibles.item(k);
              let plat: Plat = {};
              plat.idPlat = listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'idPlat').item(0).textContent;
              plat.nomPlat = listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'nomPlat').item(0).textContent;
              plat.type = listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'type').item(0).textContent;
              plat.prix = listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'prix').item(0).textContent;
              plat.photo = listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'photo').item(0).textContent;

              // On recupere les ingredients XML de ce plat 
              let platIngredientsXML = listePlatsDisponibles.item(k).getElementsByTagNameNS(ns, 'ingredient');
              let ingredientsList = [];
              for (let j = 0; j < platIngredientsXML.length; j++) {
                ingredientsList.push(platIngredientsXML.item(j).textContent);
              }
              plat.ingredients = ingredientsList;
              listePlatsMenu.push(plat);
              //console.log(plat);
            }
          }
        }
        menu.idMenu = menuId;
        menu.listeGenre = listGenre;
        menu.listePlat = listePlatsMenu;
        // On ajoute ce menu a la liste des menus
        listMenus.push(menu);
        console.log(menu);
      }
    }

    return listMenus;
  }
}
