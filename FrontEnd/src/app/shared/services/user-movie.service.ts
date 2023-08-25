import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { UsermovieModel } from '../models/usermovie.model';
import { ConstantsService } from './constants.service';

@Injectable({
  providedIn: 'root'
})
export class UserMovieService {
  
  public _usermovies$ = new BehaviorSubject<UsermovieModel[]>([]); //##Permet d'utiliser _usermovies$ à travers les composants
  public _usermovie$ = new BehaviorSubject<any>(UsermovieModel);

  constructor(private http: HttpClient,
              private constants: ConstantsService) { }

  postUserMovie(idMovie:Number, idUser:Number):any {
    let endpoint = '/usermovie';
    let data = {
      "movie":{
        "id":idMovie
      },
      "status":this.constants.STATUS_UNWATCHED,
      "userRatings":0,
      "statusDate":new Date().toLocaleDateString(),
      "user":{
          "id": idUser
      }
    }
    return this.http.post( this.constants.EPITRACK_API + endpoint, data, {responseType:'json'});
  }

  
  deleteUserMovie(userMovieId:Number, idUser:Number):any {
    let endpoint = '/usermovie/' + userMovieId + "/" + idUser;
    return this.http.delete(this.constants.EPITRACK_API + endpoint);
  }

  getUserMoviesFromApi(userid:number):void {
    let endpoint = '/usermovie/user/';
    this.http.get( this.constants.EPITRACK_API + endpoint + userid)
      .pipe( map( (response:any) => 
            response.map((movie:any) => new UsermovieModel(movie)) ) )
      .subscribe(data => this._usermovies$.next(data))
  }

  getBest4UserMoviesFromApi(userid:number):Observable<UsermovieModel[]> {
    let endpoint = '/usermovie/best4/';
    return this.http.get( this.constants.EPITRACK_API + endpoint + userid)
      .pipe( map( (response:any) => 
            response.map((movie:any) => new UsermovieModel(movie)) ) );
  }
  changeStatusUserMovie(userMovieId:number, status:string) {
    let endpoint = '/usermovie/status/';
    let data = {};
    return this.http.put( this.constants.EPITRACK_API + endpoint + userMovieId + "/" + status, data)
      .pipe( map( (response:any) => new UsermovieModel(response)) );
  }

  getUserMovieById(id: number):Observable<UsermovieModel> {
    let endpoint = '/usermovie/' + id;
      return this.http.get(this.constants.EPITRACK_API + endpoint)
          .pipe( map( (response:any) => 
            new UsermovieModel(response)) );   
  }

  updateUserRating(userRating:object):any{
    let endpoint = '/usermovie/rating';
    let data = userRating;
    return this.http.put( this.constants.EPITRACK_API + endpoint + "/", data, {responseType:'text'});
  }

  get usermovies$():Observable<UsermovieModel[]> {
    return this._usermovies$.asObservable();
  }
  setUserMovies$(data: UsermovieModel[]) {
    this._usermovies$.next(data);
  }
  get usermovie$():Observable<UsermovieModel> {
    return this._usermovie$.asObservable();
  }
  setUserMovie$(data: UsermovieModel) {
    this._usermovie$.next(data);
  }
}
