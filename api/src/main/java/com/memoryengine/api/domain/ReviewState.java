package com.memoryengine.api.domain;


//record ile oluşturulan nesneler sabittir,oluşturulduktan sonra değiştirilemezler.
public record ReviewState(int repetitions, double easeFactor , int intervalDays) {

    public static ReviewState createInitialState(){   
        return new ReviewState(0,2.5,0);
    }
    
}
