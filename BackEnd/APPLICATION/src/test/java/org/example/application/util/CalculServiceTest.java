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
    public void testComputeAverageWithNewRating() {
        Integer totalRating = 100;
        Integer voteCount = 20;
        Integer newVote = 5;
        Integer previousVote = null;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(95), result);
    }

    @Test
    public void testComputeAverageWithExistingRating() {
        Integer totalRating = 150;
        Integer voteCount = 30;
        Integer newVote = 4;
        Integer previousVote = 3;

        Integer result = calculService.computeAverage(totalRating, voteCount, newVote, previousVote);

        assertEquals(Integer.valueOf(150), result);
    }
}
