import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Menu } from '../menus-data/menus';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  // Liste des quantites possible pour un menu
  numberQuantity = [0,1,2,3,4,5,6,7,8,9,10];
  selectedQuantity = 0;
  // Menu de ce composant
  @Input() menu: Menu;

  // bloc DIV du menu
  menuDiv: HTMLElement;

  // le menu est t-il selectionne ?
  menuSelectionned: boolean = false;

  @Output() menuSelectionnedEvent = new EventEmitter<Menu>();

  constructor() { }

  ngOnInit(): void {
  }

// Quant l'utilisateur clique sur un menu
  onClickMenu(){
    if(this.selectedQuantity > 0){
      this.menuSelectionned = !this.menuSelectionned;
      this.menu.nombreMenuCommandés = this.selectedQuantity;
      this.menuSelectionnedEvent.emit(this.menu);
      console.log(this.selectedQuantity);
    }
    else{
      alert('Vous devez choisir une quantite superieur a 0 pour commander un plat');
    }
   
  }
  // retourne le numeo du menu
  getNumero(){
    return this.menu.idMenu;
  }
  getListePlats(){
    return this.menu.listePlat;
  }
}
