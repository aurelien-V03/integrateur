import { Component, OnInit } from '@angular/core';
import { MovieResponse } from './tmdb-data/Movie';
import { TmdbService } from './tmdb.service';
import { MenusService } from './menus.service';
import { environment } from '../environments/environment';
import { User } from 'firebase';
import { Menu, Plat } from './menus-data/menus';
import { RequeteServeurService } from './requete-serveur.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [MenusService]
})
export class AppComponent implements OnInit {

  //les elements à transmetres au panier
  urlParmetres: any;
  idMenu: string;
  valider: boolean;
  infoFilmsetPlats: any;

  // liste des films disponibles
  movieList = [];
  // liste des menus disponibles
  menusList: [];

  //adresse de livraison
  adresseLivraison: string = '';

  // Utilisateur Google
  u: User = undefined;

  //Faire une recherche parmis la liste des films
  filterFilms: '';

  // film et menus que l'utilisateur a choisit
  movieSelected: MovieResponse;
  menusSelected: Menu[] = [];
  // Variables qui permettent de savoir si l'utilisateur a choisit des menus et des films
  isMovieSelected: boolean = false;
  isMenuSelected: boolean = false;

  // numero du film
  movieNumber: number = 1;

  // bouton pour afficher les films / en afficher plus
  buttonDisplayMoreMovies: HTMLElement;

  buttonDisplayMovies: HTMLElement;
  buttonDisplayMenus: HTMLElement;

  commanderButton: HTMLElement;
  commanderButoonMenus: HTMLElement;

  buttonSortPopularity: HTMLElement;
  buttonSortRelease: HTMLElement;

  // HTML contenant les films / menus
  listMoviesDiv: HTMLElement;
  listMenusDiv: HTMLElement;

  displayMovies: boolean = false;
  displayMenus: boolean = false;
  selection: boolean;

  constructor(private tmdb: TmdbService, private menusService: MenusService, private requeteServeur: RequeteServeurService) {
    this.init();
  }

  ngOnInit() {
    // On recupere le bouton 'LISTE DES FILMS' + onClick
    this.buttonDisplayMovies = document.getElementById('listMoviesButton');
    this.listMoviesDiv = document.getElementById('listMovies');
    //on masque la facture pour le momment
    document.getElementById('facture').style.display = 'none';
    document.getElementById('divBlockInfoClient').style.display = 'none';

    // On n'affiche pas le bloc de film de base
    this.listMoviesDiv.style.display = 'none';
    this.buttonDisplayMovies.addEventListener('click', () => {
      // On cache la liste des menus au cas ou
      this.listMenusDiv.style.display = 'none';
      // On enleve les films
      if (this.displayMovies) {
        this.listMoviesDiv.style.display = 'none';
        this.displayMovies = false;
      }
      // On affiche les films
      else {
        this.listMoviesDiv.style.display = '';
        this.displayMovies = true;
      }

    });
    // Button 'Affcier Plus' + onClick (on recupere 20 films en plus)
    this.buttonDisplayMoreMovies = document.getElementById('displayMoreButton');
    this.buttonDisplayMoreMovies.addEventListener('click', () => {
      this.getMovieList();
    });

    // Button 'Commander un menu'
    // L'utilisateur ne peux cliquer dessus que si il a auparavant cliquer un sur film
    this.commanderButton = document.getElementById('commanderButton');
    this.commanderButton.addEventListener('click', () => {
      // l'utilisateur a cliquer sur un film (on cache la liste des films + affiche liste des menus)
      if (this.isMovieSelected) {
        this.listMoviesDiv.style.display = 'none';
        this.listMenusDiv.style.display = '';
      }
      //l' utilisateur n'a pas clique sur un film
      else {
        alert('Vous devez choisir un film ! ');
      }

    });
    // Bouton pour trier par popularite
    this.buttonSortPopularity = document.getElementById('buttonSortPopularity');
    this.buttonSortPopularity.addEventListener('click', () => {
      this.movieList.sort(
        this.popularitySort
      );
    });
    // Bouton pour trier par annee de sortie
    this.buttonSortRelease = document.getElementById('buttonSortReleaseDate');
    this.buttonSortRelease.addEventListener('click', () => { this.movieList.sort(this.yearSort); });

    /*
         LISTE DES MENUS
    */
    this.buttonDisplayMenus = document.getElementById('listMenusButton');
    this.listMenusDiv = document.getElementById('listeMenus');
    //this.listMenusDiv.style.display = 'none';
    this.buttonDisplayMenus.addEventListener('click', () => {
      // On cache la liste des films
      this.listMoviesDiv.style.display = 'none';
      // On enleve les films
      if (this.displayMenus) {
        this.listMenusDiv.style.display = 'none';
        this.displayMenus = false;
      }
      // On affiche les films
      else {
        this.listMenusDiv.style.display = '';
        this.displayMenus = true;
      }

      // bouton "commander un film"
      this.commanderButoonMenus = document.getElementById('menusCommander');
      this.commanderButoonMenus.addEventListener('click', () => {
        // Si l'utilisateur n'a pas choisit de menu 
        if (this.menusSelected.length == 0) {
          alert('Vous devez choisir au minimum 1 menu  !');
        }
        else {
          this.listMenusDiv.style.display = 'none';
          this.listMoviesDiv.style.display = '';
        }
      })
    });

  }

  // Recupere 20 films en plus
  async getMovieList() {

    for (let i = this.movieNumber; i < this.movieNumber + 20; i++) {
      try {
        // On recupere un film
        const movieRequest: MovieResponse = await this.tmdb.getMovie(i);
        //  console.log(i);
        // console.log(movieRequest);
        this.movieList[this.movieList.length] = movieRequest;

      } catch (e) {
        console.log(e);

      }
    }
    this.movieNumber += 20;
  }

  async init() {
    this.tmdb.init(environment.tmdbKey);
    this.movieList = [];
    // On recupere les 20 premiers films
    this.getMovieList();
    // on recupere les menus ensuite avec le service de requete
    // ex : this.getMenusList()
    //this.menusList = this.menusService.getMenus();
    this.menusList = await this.menusService.getMenusFromXML();
  }
  get film() {
    return this.movieList;
  }
  // Quand l'utilisateur clique sur un film (composant movie --> app.component )
  getMovieSelected(filmSelectionne: MovieResponse) {
    let movieSelectedDiv = document.getElementById('movieSelected');

    // Si aucun film n'a auparavant ete selectionne
    if (this.movieSelected == undefined) {
      this.movieSelected = filmSelectionne;
      this.isMovieSelected = true;
      movieSelectedDiv.innerText = 'Film selectionne : ' + filmSelectionne.title;
    }
    // un film a deja ete selectionne avant
    else {
      // l'utilisateur a selectionne 2 fois de suite le film (donc on le supprime)
      if (this.movieSelected.title == filmSelectionne.title) {
        this.movieSelected = undefined;
        this.isMovieSelected = false;
        movieSelectedDiv.innerText = '';
      }
      // l'utilisateur choisit un autre film
      else {
        this.movieSelected = filmSelectionne;
        this.isMovieSelected = true;
        movieSelectedDiv.innerText = 'Film selectionne : ' + filmSelectionne.title;
      }
    }

    // On supprime la classe 'isSelected' des films qui ne sont pas selectionnes
    if (this.isMovieSelected) {
      let listeMovieBlockSelected = document.getElementsByClassName('movieBlock isSelected');
      for (let i = 0; i < listeMovieBlockSelected.length; i++) {
        listeMovieBlockSelected.item(i).classList.remove('isSelected');
      }
      console.log('nb block = ' + listeMovieBlockSelected.length);

    }
  }

  // Quand l'utilisateur clique un menu
  // on ajout le menu dans la liste des menus selectionnes si il n'est pas dedans
  // sino non le supprime car cela veut dire qu'il a clique 2 fois de suite sur ce menu
  getMenuSelected(event: Menu) {
    let i = 0;
    let menuInsideArray: Boolean = false;
    // On cherche si notre liste de menus selectionne contient ou non le menu selectionne
    while (i < this.menusSelected.length && !menuInsideArray) {
      if (this.menusSelected[i].idMenu == event.idMenu) {
        menuInsideArray = true;
      }
      i++;
    }
    // On ajoute le menu selectionne
    if (menuInsideArray == false) {
      this.menusSelected.push(event);
      this.isMenuSelected = true;
    }
    // On enleve le menu
    else {
      this.menusSelected = this.menusSelected.filter((menu, index) => menu.idMenu != event.idMenu);
      if (this.menusSelected.length == 0)
        this.isMenuSelected = false;
    }

  }

  loginUser(event) {
    this.u = event;
    console.log("USER = " + event);
  }

  popularitySort(a: MovieResponse, b: MovieResponse) {
    if (a.popularity > b.popularity) {
      return 1;
    }
    else if (a.popularity < b.popularity) {
      return -1;
    }
    else
      return 0;
  }

  yearSort(a: MovieResponse, b: MovieResponse) {
    if (parseInt(a.release_date) < parseInt(b.release_date)) {
      return 1;
    }
    else if (parseInt(a.release_date) > parseInt(b.release_date)) {
      return -1;
    }
    else
      return 0;
  }

  // quant l'utilisateur valide sa commande
  // On envoie la liste des menus, des films et de l'adresse de livraison au serveur metier
  // /api/commande?idMovie=A&idMenu=B:C,B:C&livr=D   (A = id du film, B = id d'un menu, C = quantite du menu, D = adresse de livraison)
  async ajoutPanier() {

    // On cache les menus et les films
    this.listMenusDiv.style.display = 'none';
    this.listMoviesDiv.style.display = 'none';
    document.getElementById('ConfirmationCommande').style.display = 'none';
    let urlAPIParams: { idMovie: string; idMenu: string; livr: string; idClient } = {};

    // parametres de l'URL
    let listIdMenu = '';
    this.menusSelected.forEach((menu) => { for(let plat of menu.listePlat){
      listIdMenu += plat.idPlat +'-'+ menu.nombreMenuCommandés + ' ';
      this.idMenu = menu.idMenu;
      let tmp=parseFloat(plat.prix.toString());
    } });
    // on retire la dernier point virgule
    listIdMenu = listIdMenu.substring(0, listIdMenu.length - 1);
    urlAPIParams.idMovie = this.movieSelected.id.toString();
    urlAPIParams.idMenu = listIdMenu;
    urlAPIParams.livr = document.getElementById('livr').value;
    urlAPIParams.idClient = this.u.uid;
    //les paramettres à transmettre au panier
    this.urlParmetres = urlAPIParams;
    this.infoFilmsetPlats = { menu: this.menusSelected, film: this.movieSelected };
    alert("Selection ajouter dans votre panier");
    alert("Merci de passer commande");
  }
}

