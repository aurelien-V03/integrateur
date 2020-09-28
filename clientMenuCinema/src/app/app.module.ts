import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {TmdbService} from './tmdb.service';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import { environment } from '../environments/environment';
import { AngularFireModule } from '@angular/fire';

import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireAnalyticsModule } from '@angular/fire/analytics';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { LoginComponent } from './login/login.component';
import { MovieComponent } from './movie/movie.component';
import { FilterPipe } from './filter.pipe';
import {CommandeClientComponent} from "./commande-client/commande-client.component";
import { MenuComponent } from './menu/menu.component';
import { UtilisateurInfoComponent } from './utilisateur-info/utilisateur-info.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MovieComponent,
    FilterPipe,
    CommandeClientComponent,
    MenuComponent,
    UtilisateurInfoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireAnalyticsModule,
    AngularFireAuthModule,
    AngularFirestoreModule,
    AppRoutingModule
  ],
  providers: [TmdbService, HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
