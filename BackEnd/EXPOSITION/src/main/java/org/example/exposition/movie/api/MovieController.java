package org.example.exposition.movie.api;

import org.example.application.IMovieService;
import org.example.domaine.catalog.Movie;
import org.example.domaine.exceptions.ResourceAlreadyExistsException;
import org.example.exposition.movie.converter.MovieConverter;
import org.example.exposition.movie.dto.MovieDetailDto;
import org.example.exposition.movie.dto.MovieMinDto;
import org.example.exposition.tmdb.dto.TmdbDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    IMovieService movieService;
    @Autowired
    MovieConverter movieConverter;
    @PostMapping
    public ResponseEntity<Long> createMovie(@RequestBody TmdbDto detailDto) {
       Long newMovieId = movieService.create(movieConverter.convertTmdbDtoToEntity(detailDto));
       return ResponseEntity.ok().body(newMovieId);
    }

    @PostMapping("/mass")
    public ResponseEntity<String> createMovies(@RequestBody List<TmdbDto> dtoList) {
        int createdMovieCount = 0;
        int existingMovieCount = 0;
        for (TmdbDto dto : dtoList){
            try {
                movieService.create(movieConverter.convertTmdbDtoToEntity(dto));
                createdMovieCount +=1;
            }
            catch (ResourceAlreadyExistsException e){
                existingMovieCount +=1;
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovieCount+" films créés / "+existingMovieCount+" films non créés car déjà existants");
    }
    @GetMapping("/{id}")
    public MovieDetailDto findById(@PathVariable("id")Long id) {
        return movieConverter.convertEntityToDetailDto(movieService.findById(id));
    }
    @GetMapping
    public List<MovieDetailDto> findAll(){
        List<Movie> moviesEntity = movieService.findAll();
        List<MovieDetailDto> moviesDetailDto = new ArrayList<>();
        for (Movie movie:moviesEntity) {
            MovieDetailDto movieDetailDto = movieConverter.convertEntityToDetailDto(movie);
            moviesDetailDto.add(movieDetailDto);
        }
        return moviesDetailDto;
    }

    @GetMapping("/best4")
    public List<MovieMinDto> findFirst4ByOrderByTotalRatingDesc(){
        List<Movie> moviesEntity = movieService.findFirst4ByOrderByTotalRatingDesc();
        List<MovieMinDto> moviesMinDto = new ArrayList<>();
        for (Movie movie:moviesEntity) {
            MovieMinDto movieMinDto = movieConverter.convertEntityToMinDto(movie);
            moviesMinDto.add(movieMinDto);
        }
        return moviesMinDto;
    }
    @GetMapping("/search")
    public List<MovieMinDto> findByTitleContains(@RequestParam("query") String title){
        List<Movie> moviesEntity = movieService.findByTitleContains(title);
        List<MovieMinDto> moviesMinDto = new ArrayList<>();
        for (Movie movie:moviesEntity) {
            MovieMinDto movieMinDto = movieConverter.convertEntityToMinDto(movie);
            moviesMinDto.add(movieMinDto);
        }
        return moviesMinDto;
    }

}
