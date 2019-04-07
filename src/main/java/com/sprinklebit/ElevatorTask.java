package com.sprinklebit;

import java.util.Objects;
import java.util.Random;

public class ElevatorTask
{
    private int code;
    private final Elevator.Direction direction;
    private final int toFloor;
    private final int fromFloor;
    private boolean isStopped;
    public static final int quantityOfPeople = 1;
    public static final Random generatorTaskCode = new Random();

    public ElevatorTask(Elevator.Direction direction, int toFloor, int fromFloor, boolean isStopped)
    {
        int taskCode = generatorTaskCode.nextInt();
        this.direction = direction;
        this.toFloor = toFloor;
        this.fromFloor = fromFloor;
        this.isStopped = isStopped;
        this.code = taskCode;
    }

    public int getToFloor()
    {
        return toFloor;
    }

    public int getCode()
    {
        return code;
    }

    public int getFromFloor()
    {
        return fromFloor;
    }

    public Elevator.Direction getDirection()
    {
        return direction;
    }

    public boolean isStopped()
    {
        return isStopped;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorTask that = (ElevatorTask) o;
        return toFloor == that.toFloor &&
                fromFloor == that.fromFloor;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(toFloor, fromFloor);
    }
}
