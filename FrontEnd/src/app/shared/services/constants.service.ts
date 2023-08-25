import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConstantsService {
  readonly EPITRACK_API = 'http://localhost:8080/api/v1';
  readonly TMDB_API = 'https://api.themoviedb.org/3';
  readonly APIKEY_TMDB = '503f9b6e89ed3a77b5a426dbc8e1094f';
  readonly TMDB_IMG_URL = 'https://image.tmdb.org/t/p/w500';
  readonly STATUS_UNWATCHED = "UNWATCHED";
  readonly STATUS_WATCHED = "WATCHED";
  readonly STATUS_ONGOING = "ONGOING";
}
