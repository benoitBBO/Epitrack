import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { UserserieModel } from '../models/userserie.model';
import { ConstantsService } from './constants.service';

@Injectable({
  providedIn: 'root'
})
export class UserSerieService {

  public _userseries$ = new BehaviorSubject<UserserieModel[]>([]);
  public _userserie$ = new BehaviorSubject<any>(UserserieModel);
  
  constructor(private http:HttpClient,
              private constants: ConstantsService) { }
  
  postUserSerie(serieId:number, idUser:Number){
    let endpoint = '/userserie/'+serieId+"/"+idUser;
    let data = {};
    return this.http.post( this.constants.EPITRACK_API + endpoint, data, {responseType:'json'});
  }

  deleteUserSerie(userSerieId:Number, idUser:Number):any {
    let endpoint = '/userserie/' + userSerieId + "/" + idUser;
    return this.http.delete(this.constants.EPITRACK_API + endpoint);
  }

  getUserSeriesFromApi(userid:number):void {
    let endpoint = '/userserie/user/';
    this.http.get( this.constants.EPITRACK_API + endpoint + userid)
      .pipe( map( (response:any) => 
            response.map((serie:any) => new UserserieModel(serie)) ) )
      .subscribe(data => this._userseries$.next(data))
  }

  getBest4UserSeriesFromApi(userid:number):Observable<UserserieModel[]> {
    let endpoint = '/userserie/best4/';
    return this.http.get( this.constants.EPITRACK_API + endpoint + userid)
      .pipe( map( (response:any) => 
            response.map((serie:any) => new UserserieModel(serie)) ) );
  }

  getUserSerieById(id: number):Observable<UserserieModel> {
    let endpoint = '/userserie/' + id;
      return this.http.get(this.constants.EPITRACK_API + endpoint)
          .pipe( map( (response:any) => 
            new UserserieModel(response)) );   
  }

  updateUserRating(userRating:object):any{
    let endpoint = '/userserie/rating';
    let data = userRating;
    return this.http.put( this.constants.EPITRACK_API + endpoint + "/", data, {responseType:'text'});
  }

  changeStatusUserSerie(userSerieId:number, status:string):Observable<UserserieModel> {
    let endpoint = '/userserie/status/';
    let data = {};
    return this.http.put( this.constants.EPITRACK_API + endpoint + userSerieId + "/" + status, data)
        .pipe( map( (response:any) => new UserserieModel(response)) );
  } 

  changeStatusUserSeason(userSerieId:number, userSeasonId:number, status:string):Observable<UserserieModel> {
    let endpoint = '/userserie/status/';
    let data = {};
    return this.http.put( this.constants.EPITRACK_API + endpoint + userSerieId+"/"+userSeasonId+"/" + status, data)
        .pipe( map( (response:any) =>  new UserserieModel(response)) );
  } 

  changeStatusUserEpisode(userSerieId:number, userSeasonId:number, userEpisodeId:number, status:string):Observable<UserserieModel> {
    let endpoint = '/userserie/status/';
    let data = {};
    return this.http.put( this.constants.EPITRACK_API + endpoint + userSerieId+"/"+userSeasonId+"/"+userEpisodeId+"/" + status, data)
        .pipe( map( (response:any) => new UserserieModel(response)) ) ;
  } 

  get userseries$():Observable<UserserieModel[]> {
    return this._userseries$.asObservable();
  }

  setUserSeries$(data: UserserieModel[]) {
    this._userseries$.next(data);
  }

  get userseries():UserserieModel[] {
    return this._userseries$.getValue();
  }
  

}
