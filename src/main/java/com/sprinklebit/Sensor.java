package com.sprinklebit;

public class Sensor implements ISensor
{
    @Override
    public boolean isOverLoaded(int passengersOnBoard)
    {
        return (passengersOnBoard * AVERAGE_OF_PERSON_WEIGHT) > (CAPACITY * AVERAGE_OF_PERSON_WEIGHT);
    }
}
