import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MovieResponse } from '../tmdb-data/Movie';

@Component({
  selector: 'app-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.scss']
})
export class MovieComponent implements OnInit {

 @Input() movie: MovieResponse;
 @Input() numeroFilm: number;
 showPortal;

 // Le film est t-il selectionne ?
 movieIsSelected = false;
 
 @Output() movieComponentSelected = new EventEmitter<MovieResponse>();

  constructor() { 
  }

  ngOnInit(): void {
      // console.log('Movie compo : ' + this.movie.title + ' ' + this.movie.release_date);
  }

  // retourne le titre du film
  getMovieTitle(){
    let aretourner  = '';
    if(this.movie === undefined)
      aretourner = 'Titre inconnu';
    else
    {
      if(this.movie.title.length > 12)
      {
        aretourner = this.movie.title.substring(0,12) + ' [...]';
      }
      else
        aretourner = this.movie.title;
    }
    return aretourner;
  }
  getMovieRelease(){
    let aretourner  = '';
    if(this.movie === undefined)
      aretourner = 'Annee inconnu';
    else
      aretourner = this.movie.release_date;
    return aretourner;
  }

  getMovieGenre(){
    let aretourner  = '';
    if(this.movie === undefined)
      aretourner = 'genre inconnu';
    else
     {
       this.movie.genres.forEach( (genre) => {aretourner += genre.name + ' '} );
       if(aretourner.length > 10)
       {
         aretourner = aretourner.substring(0,10);
       }
     }
    return aretourner;
  }

  getMovieOrigine(){
    let aretourner  = '';
    if(this.movie === undefined)
      aretourner = 'origine inconnu';
    else
     {
      aretourner = this.movie.original_language;
     }
    return aretourner;
  }

  // retourne le budget du film si il existe
  getBudget(){
    let aretourner = '';
    if(!this.movie.budget)
    {
      aretourner = 'Inconnu';
    }
    else{
      aretourner += this.movie.budget;
    }
    return aretourner;
  }

  getRevenue(){
    let aretourner = '';
    if(!this.movie.revenue)
    {
      aretourner = 'Inconnu';
    }
    else{
      aretourner += this.movie.revenue;
    }
    return aretourner;
  }


  // Quand l'utilisateur selectionne un film
  selectMovie(){
    this.movieIsSelected = !this.movieIsSelected;
    this.movieComponentSelected.emit(this.movie);
  }

  // retourne le chemin du poster du film si il existe sinon retourne undefined
  getMoviePosterPath(){
    let aretourner = '';
    // Si il y a une image pour le poster
    if(this.movie.poster_path)
    {
      aretourner += 'https://image.tmdb.org/t/p/w500/' + this.movie.poster_path;
    }
    else
    {
      aretourner = undefined;
    }
    return aretourner;
  }
 

  showDetail(){
    this.showPortal = true;
  }
 hideDetai(){
   this.showPortal = false;
 }

}
