package org.example.application;

import org.example.domaine.catalog.Serie;
import org.example.domaine.userselection.UserRating;

import java.util.List;

public interface ISerieService {

    Long create(Serie newSerie);
    Serie findById(Long id);
    List<Serie> findAll();
    List<Serie> findFirst4ByOrderByTotalRatingDesc();
    List<Serie> findByTitleContains(String title);
    void updateSerieTotalRatingAndVoteCount(Serie serie, UserRating userRating);
}
