
// Represente un menu
export interface Menu {
    idMenu: string;
    listePlat: Plat[];
    listeGenre: string[];
    nombreMenuCommandés?: number;
}
// Plat qui compose un Menu
export interface Plat {
    idPlat: string;
    nomPlat: string;
    type: string;
    prix: string;
    photo?: string;
    ingredients: string[];
}