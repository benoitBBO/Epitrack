import { Component } from '@angular/core';
import { UserMovieService } from '../shared/services/user-movie.service';
import { Router } from '@angular/router';
import { UsermovieModel } from '../shared/models/usermovie.model';
import { UserModel } from '../shared/models/user.model';
import { UserService } from '../shared/services/user.service';

@Component({
  selector: 'app-user-movie-list',
  templateUrl: './user-movie-list.component.html',
  styleUrls: ['./user-movie-list.component.css']
})
export class UserMovieListComponent {
  movies: UsermovieModel[] = [];
  originalUserMovies: UsermovieModel[] = [];
  movie!: UsermovieModel;
  loggedUser!:UserModel;
  currentUrl!: string;
  
  constructor(private router:Router,
              private userMovie:UserMovieService,
              private userService:UserService) {
  }

  ngOnInit() {
    //RG pour css Films/Series
    this.currentUrl = this.router.url;

    //charger loggedUSer
    this.userService._loggedUser$.subscribe((user:any) => this.loggedUser=user );
    //requete get API
    if (this.router.url == '/user') {
      this.userMovie.getBest4UserMoviesFromApi(this.loggedUser.id).subscribe( data => this.movies = data);
      
      //Chargement du catalogue à la première connexion
      this.userMovie.getUserMoviesFromApi(this.loggedUser.id);
    } else if (this.router.url == '/user/movies') {
      this.userMovie.getUserMoviesFromApi(this.loggedUser.id);
      this.userMovie.usermovies$.subscribe( data => {
        this.movies = data;
        this.originalUserMovies = data;
      });
    }
    
  }

  onClickStatusBtn(value: string){
    switch(value){
      case "All":
        this.movies = this.originalUserMovies;
        break;
      case "UNWATCHED":
        this.movies = this.originalUserMovies.filter(userMovie => userMovie.status === value);
        break;
      case "ONGOING":
        this.movies = this.originalUserMovies.filter(userMovie => userMovie.status === value);
        break;
      case "WATCHED":
        this.movies = this.originalUserMovies.filter(userMovie => userMovie.status === value);
        break;
            
    }
  }
}
