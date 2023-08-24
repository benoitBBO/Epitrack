package org.example.application;

import org.example.application.util.CalculServiceImpl;
import org.example.application.util.ICalculService;
import org.example.domaine.catalog.Episode;
import org.example.infrastructure.repository.ISerieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SerieServiceImpl.class, ISerieRepository.class,
                            CalculServiceImpl.class})
public class SerieServiceTest {

    @MockBean
    ISerieRepository serieRepository;

    @Autowired
    SerieServiceImpl serieService;   //pas l'interface pour pouvoir tester la fonction sort déclarée uniquement dans implémentation

    @Autowired
    ICalculService calculService;

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

}
