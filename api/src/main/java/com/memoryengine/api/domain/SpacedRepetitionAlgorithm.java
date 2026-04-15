package com.memoryengine.api.domain;

public interface SpacedRepetitionAlgorithm {
 
    ReviewState calculateNexReview(ReviewState currentState, int quality);
}
