package org.example.infrastructure.repository;

import org.example.domaine.catalog.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findFirst4ByOrderByTotalRatingDesc();
    List<Movie> findByTitleContainsIgnoreCase(String title);
    Optional<Movie> findByImdbRef(String imdbRef);

    @Modifying
    @Query("update Movie movie set movie.totalRating = :newrating where movie.id = :movieId")
    void updateMovieRating(@Param(value = "movieId")Long movieId, @Param(value = "newrating")Integer newRating);

    @Modifying
    @Query("update Movie movie set movie.voteCount = :newVoteCount where movie.id = :movieId")
    void updateMovieVotes(@Param(value = "movieId")Long movieId, @Param(value = "newVoteCount") Integer newVoteCount);
}
