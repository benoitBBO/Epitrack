import { Component } from '@angular/core';
import { MovieModel } from '../shared/models/movie.model';
import { MovieService } from '../shared/services/movie.service';
import { Router } from '@angular/router';
import { UserMovieService } from '../shared/services/user-movie.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from '../shared/services/message.service';
import { UsermovieModel } from '../shared/models/usermovie.model';
import { UserModel } from '../shared/models/user.model';
import { UserService } from '../shared/services/user.service';
import { BehaviorSubject } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent {
  movies: MovieModel[] = []; //Utilisé pour l'affichage
  originalMovies: MovieModel[] = []; //Sauvegarde des movies pour effectuer les tris/filtres
  genres: string[] = [];
  btnsSortClass = "btn btn-light btn-sm";
  btnsFilterClass = "btn btn-light btn-sm";
  displaySort: string = "";
  genreFiltered: string = "";
  displayBtnSort: boolean = false;
  displayResetFiterBtn: boolean = false;
  movie!: MovieModel;
  userMovies!: UsermovieModel[];
  loggedUser!:UserModel;
  dynamicCatalog: any[] = [];
  currentUrl!: string;
  pageloaded: boolean = false;

  constructor(private service: MovieService,
              private router:Router,
              private msgService:MessageService,
              private userMovie:UserMovieService,
              private userService:UserService,
              private spinner: NgxSpinnerService) {
  }

  ngOnInit() {
    //RG pour css Films/Series
    this.currentUrl = this.router.url;

    this.userService._loggedUser$.subscribe((user:any) => {
      this.loggedUser=user;
    });
    
    //Récupération des userMovies
    this.userMovie._usermovies$.subscribe(data => this.userMovies = data);
    //requete get API
    this.spinner.show();
    if (this.router.url == '/') {
      this.service.getBest4MoviesFromApi().subscribe( data => {
        this.movies = data;
        this.loadingDynamicCatalogVariable();
        this.spinner.hide();
      });
    } else if (this.router.url == '/movies') {
      this.service.getMoviesFromApi().subscribe( data => {
        this.movies = data;
        this.originalMovies = data;
        this.loadingDynamicCatalogVariable();

        //Chargement des genres pour les tris
        for(let movie of data){
          for(let genre of movie.genres){
            if(!this.genres.includes(genre.name)){
              this.genres.push(genre.name);
            }
          }
        }
        this.pageloaded = true;
        this.spinner.hide();
      });
    }
  }

  onClickAddMovie(idMovie:Number, index:number) {
    if (sessionStorage.getItem('token') && this.loggedUser.id !==0 && this.loggedUser.id !== undefined) {
        this.spinner.show();
        this.userService._loggedUser$.subscribe((user:any) => {
        this.loggedUser=user;
        this.userMovie.postUserMovie(idMovie, this.loggedUser.id)
          .subscribe( {
            next: (response:any) => {
              //Mise à jour de la selection User
              this.userMovie._usermovies$ = new BehaviorSubject<any>(response);
              
              this.msgService.show("Film ajouté avec succès", "success");
              this.dynamicCatalog[index] = !this.dynamicCatalog[index];
              this.spinner.hide();
            },
            error: (err:unknown) => {
              if (err instanceof HttpErrorResponse){
                let errorObj = JSON.parse(err.error);
                switch(err.status) {
                  case 404:
                    this.msgService.show(errorObj.description, "error");
                    break;
                  case 409:
                    this.msgService.show("Ce film est déjà suivi", "error");
                    break;
                  default:
                    this.msgService.show("code Http: "+errorObj.description+ "description: "+errorObj.description, "error");
                }          
              }
              this.spinner.hide();
            }
          });
        });
    } else {
      this.msgService.show("Vous devez être connecté pour accéder à cette fonctionnalité", "error");
        this.router.navigate(['/login']);
    }
  }

  onClickWithdrawMovie(idMovie:Number, index:number) {
    this.spinner.show();
    this.userService._loggedUser$.subscribe((user:any) => {
      this.loggedUser=user;
      this.userMovie.deleteUserMovie(idMovie, this.loggedUser.id)
        .subscribe( {
          next: (response:any) => {
            //Mise à jour de la selection User
            this.userMovie._usermovies$ = new BehaviorSubject<any>(response);

            this.msgService.show("Film retiré du catalogue avec succès", "success");
            this.dynamicCatalog[index] = !this.dynamicCatalog[index];
            this.spinner.hide();
          },
          error: (err:unknown) => {
            if (err instanceof HttpErrorResponse){
              let errorObj = JSON.parse(err.error);
              switch(err.status) {
                case 404:
                  this.msgService.show(errorObj.description, "error");
                  break;
                default:
                  this.msgService.show("code Http: "+errorObj.description+ "description: "+errorObj.description, "error");
              }          
            }
            this.spinner.hide();
          }
        });
      });
  };

  loadingDynamicCatalogVariable(){
    if(sessionStorage.length > 0){
      this.dynamicCatalog = [];
      for(let movie of this.movies){
        this.dynamicCatalog.push(this.isNotInCatalog(movie.id));
      }
    }
  }

  isNotInCatalog(idMovie:Number){
    if(sessionStorage.length > 0){
      for(let userMovie of this.userMovies){
        if(userMovie.movie.id === idMovie){
          return false;
        }
      }
      return true;
    }
    return true;
  }

  onClickSortByAlphabeticalOrderAz(){
    this.movies = this.movies.slice().sort((a, b) => a.title.localeCompare(b.title, 'fr', {ignorePunctuation: true}));
    this.displaySort = "Ordre alphabétique (A-Z)";
    this.displayBtnSort = true;
    this.btnsSortClass = "btn btn-warning btn-sm";
    this.loadingDynamicCatalogVariable();
  }

  onClickSortByAlphabeticalOrderZa(){
    this.movies = this.movies.slice().sort((a, b) => b.title.localeCompare(a.title, 'fr', {ignorePunctuation: true}));
    this.displaySort = "Ordre alphabétique (Z-A)";
    this.displayBtnSort = true;
    this.btnsSortClass = "btn btn-warning btn-sm";
    this.loadingDynamicCatalogVariable();    
  }

  onClickSortByRatingAsc(){
    this.movies = this.movies.slice().sort((a, b) => {
      if (a.rating < b.rating) {
        return -1;
      }
      if (a.rating > b.rating) {
        return 1;
      }
      return 0;
    });
    this.displaySort = "Notation (Croissante)";
    this.displayBtnSort = true;
    this.btnsSortClass = "btn btn-warning btn-sm";
    this.loadingDynamicCatalogVariable();
  }

  onClickSortByRatingDsc(){
    this.movies = this.movies.slice().sort((a, b) => {
      if (a.rating > b.rating) {
        return -1;
      }
      if (a.rating < b.rating) {
        return 1;
      }
      return 0;
    });
    this.displaySort = "Notation (Décroissante)";
    this.displayBtnSort = true;
    this.btnsSortClass = "btn btn-warning btn-sm";
    this.loadingDynamicCatalogVariable();
  }

  onClickResetSort(){
    if(this.genreFiltered === ""){
      this.movies = this.originalMovies;
    } else  {
      this.onClickFilter(this.genreFiltered);
    }
    this.displaySort = "";
    this.displayBtnSort = false;
    this.btnsSortClass = "btn btn-light btn-sm";
    this.loadingDynamicCatalogVariable();
  }

  onClickResetFilter(){
    this.movies = this.originalMovies;
    this.activateDisplaySortIfNeeded();
    this.genreFiltered = "";
    this.displayResetFiterBtn = false;
    this.btnsFilterClass = "btn btn-light btn-sm";
    this.loadingDynamicCatalogVariable();
  }

  onClickFilter(genre:string){
    this.movies = this.originalMovies.filter(movie => 
      {
        for(let genreCompare of movie.genres){
          if(genreCompare.name === genre){
            return true;
          }
        }
        return false;
      }
    );
    
    this.genreFiltered = genre;
    this.displayResetFiterBtn = true;
    this.btnsFilterClass = "btn btn-warning btn-sm";

    if(this.displaySort !== ""){
      this.activateDisplaySortIfNeeded();
    }

    this.loadingDynamicCatalogVariable();
  }

  activateDisplaySortIfNeeded(){
    switch(this.displaySort){
      case "Ordre alphabétique (A-Z)" : 
        this.onClickSortByAlphabeticalOrderAz();
        break;
      case "Ordre alphabétique (Z-A)" : 
        this.onClickSortByAlphabeticalOrderZa();
        break;
      case "Notation (Croissante)" : 
        this.onClickSortByRatingAsc();
        break;
      case "Notation (Décroissante)" : 
        this.onClickSortByRatingDsc();
        break;
    }
  }
}
