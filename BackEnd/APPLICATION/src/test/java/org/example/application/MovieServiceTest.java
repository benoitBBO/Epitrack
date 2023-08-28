package org.example.application;

import org.example.application.util.CalculServiceImpl;
import org.example.application.util.ICalculService;
import org.example.domaine.catalog.Movie;
import org.example.domaine.exceptions.ResourceAlreadyExistsException;
import org.example.infrastructure.repository.IMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MovieServiceImpl.class, IMovieRepository.class,
        CalculServiceImpl.class})
public class MovieServiceTest {

    @MockBean
    IMovieRepository movieRepository;

    @Autowired
    IMovieService movieService;
    @Autowired
    ICalculService calculService;
    Movie mockedMovie;
    Movie mockedExistingMovie;
    List<Movie> mockedMovies;

    @BeforeEach
    public void init(){
        mockedMovie = new Movie();
        mockedMovie.setId(65L);
        mockedMovie.setTitle("Titre film");
        mockedMovie.setOverview("résumé du film");
        mockedMovie.setReleaseDate(LocalDate.of(2018, 7, 23));
        mockedMovie.setTotalRating(3000);
        mockedMovie.setVoteCount(126);
        mockedMovie.setImageLandscapeUrl("url");
        mockedMovie.setGenres(new ArrayList<>());
        mockedMovie.setActors(new ArrayList<>());
        mockedMovie.setImdbRef("a125874");
        //...
        mockedExistingMovie = new Movie();
        mockedExistingMovie.setId(48L);
        mockedExistingMovie.setTitle("Titre film pour test create d'un movie déjà existant en base");
        mockedExistingMovie.setReleaseDate(LocalDate.of(2018, 7, 23));
        mockedExistingMovie.setTotalRating(3000);
        mockedExistingMovie.setVoteCount(126);
        mockedExistingMovie.setImageLandscapeUrl("url");
        mockedExistingMovie.setGenres(new ArrayList<>());
        mockedExistingMovie.setActors(new ArrayList<>());
        mockedExistingMovie.setImdbRef("a111");
        //...
        mockedMovies = Arrays.asList(mockedMovie, mockedExistingMovie);

        when(movieRepository.save(mockedMovie)).thenReturn(mockedMovie);
        when(movieRepository.findById(65L)).thenReturn(Optional.of(mockedMovie));
        when(movieRepository.findByImdbRef("a125874")).thenReturn(Optional.empty());
        when(movieRepository.findByImdbRef("a111")).thenReturn(Optional.of(mockedExistingMovie));
        when(movieRepository.findById(153L)).thenReturn(Optional.empty());
        when(movieRepository.findAll()).thenReturn(Collections.unmodifiableList(mockedMovies));

    }
    @Test
    public void test_should_return_newId_when_create_newMovie(){
        assertThat(movieService.create(mockedMovie)).isEqualTo(65L);
    }

    @Test
    public void test_should_resourceAlreadyExistsException_when_create_existingMovie(){
        assertThrows(ResourceAlreadyExistsException.class,
                () -> movieService.create(mockedExistingMovie));
    }


    @Test
    public void test_should_return_mockedMovie_when_findById_OK(){
        assertThat(movieService.findById(65L)).isEqualTo(mockedMovie);
    }
    @Test
    public void test_should_return_NotFoundException_when_findById_KO(){
        assertThrows(EntityNotFoundException.class,
                () -> movieService.findById(153L));
    }
    @Test
    public void test_should_return_movieList_when_findAll(){
        assertThat(movieService.findAll().size()).isEqualTo(mockedMovies.size());
        assertThat(movieService.findAll()).isEqualTo(mockedMovies);
    }
}
