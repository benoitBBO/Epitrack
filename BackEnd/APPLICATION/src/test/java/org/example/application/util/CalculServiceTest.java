package org.example.application.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculServiceTest {

    @InjectMocks
    private CalculServiceImpl calculService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testComputeAverageWithNewRating1() {
        Integer totalRating = 4000;
        Integer voteCount = 1;
        Integer newVote = 5;
        Integer previousVote = null;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(4500), result);
    }
    @Test
    public void testComputeAverageWithNewRating2() {
        Integer totalRating = 4500;
        Integer voteCount = 2;
        Integer newVote = 2;
        Integer previousVote = null;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(3666), result);
    }
    @Test
    public void testComputeAverageWithNewRating3() {
        Integer totalRating = 3666;
        Integer voteCount = 3;
        Integer newVote = 4;
        Integer previousVote = null;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(3749), result);
    }
    @Test
    public void testComputeAverageWithNewRating() {
        Integer totalRating = 1000;
        Integer voteCount = 20;
        Integer newVote = 5;
        Integer previousVote = null;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(1190), result);
    }

    @Test
    public void testComputeAverageWithExistingRating() {
        Integer totalRating = 1500;
        Integer voteCount = 30;
        Integer newVote = 5;
        Integer previousVote = 3;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(1566), result);
    }
}
