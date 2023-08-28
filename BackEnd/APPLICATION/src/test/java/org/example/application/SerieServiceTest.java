package org.example.application;

import org.example.application.util.CalculServiceImpl;
import org.example.application.util.ICalculService;
import org.example.domaine.catalog.Episode;
import org.example.domaine.catalog.Movie;
import org.example.domaine.catalog.Season;
import org.example.domaine.catalog.Serie;
import org.example.domaine.exceptions.ResourceAlreadyExistsException;
import org.example.infrastructure.repository.ISerieRepository;
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

@SpringBootTest(classes = {SerieServiceImpl.class, ISerieRepository.class,
                            CalculServiceImpl.class})
public class SerieServiceTest {

    @MockBean
    ISerieRepository serieRepository;

    @Autowired
    SerieServiceImpl serieService;   //pas l'interface pour pouvoir tester la fonction sort déclarée uniquement dans implémentation

    @Autowired
    ICalculService calculService;

    Episode mockedEpisode1;
    Episode mockedEpisode2;
    Episode mockedEpisode3;
    Episode mockedEpisode4;
    Season mockedSeason1;
    Season mockedSeason2;
    Serie mockedSerie;
    Serie mockedExistingSerie;

    @BeforeEach
    public void init(){
        mockedEpisode1 = new Episode("num1", 1, "test", "", LocalDate.now());
        mockedEpisode1.setId(1L);
        mockedEpisode2 = new Episode("num2", 2, "test", "", LocalDate.now());
        mockedEpisode2.setId(2L);
        mockedEpisode3 = new Episode("num3", 3, "test", "", LocalDate.now());
        mockedEpisode3.setId(3L);
        mockedEpisode4 = new Episode("num4", 4, "test", "", LocalDate.now());
        mockedEpisode4.setId(4L);

        List<Episode> episodes1 = Arrays.asList(mockedEpisode1, mockedEpisode2);
        List<Episode> episodes2 = Arrays.asList(mockedEpisode3, mockedEpisode4);

        mockedSeason1 = new Season("title1", 1, "résumé1", "url", LocalDate.now(), episodes1);
        mockedSeason1.setId(1L);
        mockedSeason2 = new Season("title2", 2, "résumé2", "url", LocalDate.now(), episodes2);
        mockedSeason2.setId(2L);
        List<Season> seasons = Arrays.asList(mockedSeason1, mockedSeason2);

        mockedSerie = new Serie();
        mockedSerie.setId(65L);
        mockedSerie.setTitle("Titre série1");
        mockedSerie.setOverview("résumé de série");
        mockedSerie.setReleaseDate(LocalDate.of(2018, 7, 23));
        mockedSerie.setTotalRating(3000);
        mockedSerie.setVoteCount(126);
        mockedSerie.setImageLandscapeUrl("url");
        mockedSerie.setGenres(new ArrayList<>());
        mockedSerie.setActors(new ArrayList<>());
        mockedSerie.setImdbRef("a125874");
        mockedSerie.setSeasons(seasons);
        //...
        mockedExistingSerie = new Serie();
        mockedExistingSerie.setId(12L);
        mockedExistingSerie.setTitle("Titre série2");
        mockedExistingSerie.setOverview("résumé de série");
        mockedExistingSerie.setReleaseDate(LocalDate.of(2018, 7, 23));
        mockedExistingSerie.setTotalRating(3000);
        mockedExistingSerie.setVoteCount(126);
        mockedExistingSerie.setImageLandscapeUrl("url");
        mockedExistingSerie.setGenres(new ArrayList<>());
        mockedExistingSerie.setActors(new ArrayList<>());
        mockedExistingSerie.setImdbRef("a125874");
        mockedExistingSerie.setSeasons(seasons);
        //...


        when(serieRepository.save(mockedSerie)).thenReturn(mockedSerie);
        when(serieRepository.findById(65L)).thenReturn(Optional.of(mockedSerie));
        when(serieRepository.findByTitleAndReleaseDate("Titre série1", LocalDate.of(2018,7,23))).thenReturn(Optional.empty());
        when(serieRepository.findByTitleAndReleaseDate("Titre série2", LocalDate.of(2018,7,23))).thenReturn(Optional.of(mockedExistingSerie));
        when(serieRepository.findById(153L)).thenReturn(Optional.empty());
        //when(serieRepository.findAll()).thenReturn(Collections.unmodifiableList(mockedMovies));

    }

    @Test
    public void test_should_return_sortedEpisodeList_when_sortByEpisodeNumber(){
        Episode episode1 = new Episode("num1", 1, "test", "", LocalDate.now());
        Episode episode2 = new Episode("num2", 2, "test", "", LocalDate.now());
        Episode episode3 = new Episode("num3", 3, "test", "", LocalDate.now());
        Episode episode4 = new Episode("num4", 4, "test", "", LocalDate.now());
        Episode episode5 = new Episode("num5", 5, "test", "", LocalDate.now());

        List<Episode> episodeListIn = new ArrayList<>();
        episodeListIn.add(episode2);
        episodeListIn.add(episode5);
        episodeListIn.add(episode1);
        episodeListIn.add(episode4);
        episodeListIn.add(episode3);

        List<Episode> episodeListSorted = new ArrayList<>();
        episodeListSorted.add(episode1);
        episodeListSorted.add(episode2);
        episodeListSorted.add(episode3);
        episodeListSorted.add(episode4);
        episodeListSorted.add(episode5);

        assertThat(serieService.sortByEpisodeNumber(episodeListIn)).isEqualTo(episodeListSorted);
    }

    @Test
    public void test_should_return_newId_when_create_newSerie(){
        assertThat(serieService.create(mockedSerie)).isEqualTo(65L);
    }

    @Test
    public void test_should_resourceAlreadyExistsException_when_create_existingSerie(){
        assertThrows(ResourceAlreadyExistsException.class,
                () -> serieService.create(mockedExistingSerie));
    }

    @Test
    public void test_should_return_mockedSerie_when_findById_OK(){
        assertThat(serieService.findById(65L)).isEqualTo(mockedSerie);
    }
    @Test
    public void test_should_return_NotFoundException_when_findById_KO(){
        assertThrows(EntityNotFoundException.class,
                () -> serieService.findById(153L));
    }

}
