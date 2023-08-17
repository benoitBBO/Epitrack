package org.example.application.util;

import org.springframework.stereotype.Service;

@Service
public class CalculServiceImpl implements ICalculService {
    @Override
    public Integer computeAverage(Integer totalRating, Integer voteCount, Integer newVote, Integer previousVote) {
        Boolean isNewRating = previousVote == null ? true : false;
        previousVote = isNewRating ? 0 : previousVote;
        return isNewRating ? (((totalRating*voteCount)+ newVote*1000) / (voteCount + 1)) : (((totalRating*voteCount) - previousVote*1000 + newVote*1000) / (voteCount));
    }
}
