import {Menu} from './menus';
import {MovieResponse} from '../tmdb-data/Movie';
export interface Commande {
    numeroCommande: string;
    numeroClient: string;
    dateCommande: string;
    adresseLivraison: string;
    prix: string;

    // test 
    listeMenusCommandes?:string[];
    filmCommande?:string;

    listeMenusCommandes?: Menu[];
    filmCommande?: MovieResponse;
}