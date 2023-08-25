package org.example.exposition.movie.converter;

import org.example.domaine.catalog.Actor;
import org.example.domaine.catalog.Genre;
import org.example.domaine.catalog.Movie;
import org.example.exposition.movie.dto.MovieDetailDto;
import org.example.exposition.movie.dto.MovieMinDto;
import org.example.exposition.tmdb.dto.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MovieConverter {

    public MovieDetailDto convertEntityToDetailDto(Movie entity){
        MovieDetailDto dto = new MovieDetailDto();
        dto.setId(entity.getId());

        dto.setTitle(entity.getTitle());
        dto.setOverview(entity.getOverview());
        dto.setReleaseDate(entity.getReleaseDate());
        if (entity.getTotalRating() != null) {
            dto.setTotalRating((int) Math.floor((double) entity.getTotalRating() / 1000 + 0.5));
        }
        dto.setImagePosterUrl(entity.getImagePosterUrl());
        dto.setImageLandscapeUrl(entity.getImageLandscapeUrl());
        dto.setGenres(entity.getGenres());
        dto.setActors(entity.getActors());
        dto.setImdbRef(entity.getImdbRef());

        return dto;
    }
    public MovieMinDto convertEntityToMinDto(Movie entity){
        MovieMinDto dto = new MovieMinDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setReleaseDate(entity.getReleaseDate());
        if (entity.getTotalRating() != null) {
            dto.setTotalRating((int) Math.floor((double) entity.getTotalRating() / 1000 + 0.5));
        }
        dto.setImageLandscapeUrl(entity.getImageLandscapeUrl());
        return dto;
    }

    //Converter TMDB / Entity
    public Movie convertTmdbDtoToEntity(TmdbDto json){
        Movie movie = new Movie();

        movie.setTitle(json.getTitle());
        movie.setOverview(json.getOverview());
        movie.setReleaseDate(json.getRelease_date());
        movie.setTotalRating(json.getVote_average()/2);//TODO a multiplier par 1000 et enlever côté front
        movie.setVoteCount(json.getVote_count());
        movie.setImagePosterUrl(json.getPoster_path());
        movie.setImageLandscapeUrl(json.getBackdrop_path());
        movie.setImdbRef(json.getImdb_id());

        //Set genres
        List<Genre> genreList = new ArrayList<>();
        for (GenreDto genreDto : json.getGenres()){
            Genre genre = new Genre();
            genre.setName(genreDto.getName());
            genreList.add(genre);
        }
        movie.setGenres(genreList);

        //Set actors
        List<Actor> actorList = new ArrayList<>();
        for (CastDto castDto : json.getCredits().getCast()){
            Actor actor = new Actor();
            actor.setName(castDto.getName());
            actor.setTmdbRef((castDto.getId().toString()));
            actor.setPhotoUrl(castDto.getProfile_path());
            actorList.add(actor);
        }
        movie.setActors(actorList);


        return movie;
    }
}
