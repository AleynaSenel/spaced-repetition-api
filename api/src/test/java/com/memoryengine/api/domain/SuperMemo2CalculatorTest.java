package com.memoryengine.api.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SuperMemo2CalculatorTest {
    
    @Test
    void calculateNextReview_ShouldIncreaseInterval_WhenAnswerIsCorrect(){

        SuperMemo2Calculator calculator = new SuperMemo2Calculator();
        ReviewState initialState = ReviewState.createInitialState();

        int quality = 4;
        ReviewState nexState = calculator.calculateNexReview(initialState, quality);

        assertEquals(1, nexState.repetitions(), "tekrar sayisi 1 artmaliydi");
        assertEquals(1, nexState.intervalDays(), "ilk doğru cevapta sonraki tekrar 1 gün sonra olmali");
        assertTrue(nexState.easeFactor() < 2.5, "zorluk 4 olduğu için kolaylik faktörü biraz düşmeliydi");
    }

    @Test
    void calculateNextReview_ShouldResetRepetitions_WhenAnswerIsWrong(){

        SuperMemo2Calculator calculator = new SuperMemo2Calculator();

        ReviewState advancedState = new ReviewState(5, 2.6, 12);

        int quality = 0;
        ReviewState nexState = calculator.calculateNexReview(advancedState, quality);

        assertEquals(0, nexState.repetitions(), "kelime unutulduğu için tekrar sayisi sifirlanmali");
        assertEquals(1, nexState.intervalDays(), "kelime unutulduğu için aralik 1 güne düşmeli");
        assertTrue(nexState.easeFactor() < 2.6, "kelime unutulduğu için kolaylik faktörü düşmeli");

    }

    @Test
    void calculateNexReview_ShouldCalculateCorrectly_ForFourthRepetition_WithQuality3(){

        SuperMemo2Calculator calculator = new SuperMemo2Calculator();
        ReviewState currenState = new ReviewState(3,2.5,10);

        int quality = 3;
        ReviewState nexState = calculator.calculateNexReview(currenState, quality);

        assertEquals(4,nexState.repetitions(), "repetitions sayisi 3'ten  4'e cikmalidir.");
        assertEquals(25, nexState.intervalDays(),"aralik 25 güne cikmali");
        assertTrue(nexState.easeFactor() < 2.5, "kalite 3 olduğu icin kolaylik faktörü düşmeli");


    }
}
