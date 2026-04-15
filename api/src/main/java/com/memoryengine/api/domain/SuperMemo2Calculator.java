package com.memoryengine.api.domain;

public class SuperMemo2Calculator implements SpacedRepetitionAlgorithm{

    @Override
    public ReviewState calculateNexReview(ReviewState currentState, int quality){

        if(quality<0 || quality>5){
            throw new IllegalArgumentException("Quality must be between 0 to 5");
        }

        int repetitions = currentState.repetitions();
        double easeFactor = currentState.easeFactor();
        int intervalDays = currentState.intervalDays();

        if(quality >= 3){
            if(repetitions == 0){
                intervalDays = 1;
            }
            else if(repetitions == 1){
                intervalDays = 6;
            }
            else {
               intervalDays = (int) Math.round(currentState.intervalDays() * easeFactor);
            }
            repetitions++;

        }else{
            repetitions = 0;
            intervalDays = 1;
        }

        easeFactor = easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));

        if(easeFactor <= 1.3){
            easeFactor = 1.3;
        }


      return new ReviewState(repetitions,easeFactor,intervalDays);
    }
    
}
