package com.sprinklebit;

import java.util.*;
import java.util.stream.Collectors;

public class Elevator implements IElevator
{
    public static final int CAPACITY = 10;
    public static final int NUMBER_OF_FLOORS = 4;
    public static final int SPEED_OF_ELEVATOR = 1; // m/s
    public static final int HEIGHT_OF_ONE_FLOOR = 1; // m/s
    public static final int TIME_FOR_CLOSING_DOOR = 3; // s
    public static final int TIME_FOR_OPENING_DOOR = 3; // s
    public static final int TIME_FOR_BOARDING = 3; // s
    public static final int AVERAGE_OF_PERSON_WEIGTH = 70; // kg

    private Direction directionOfMoving;
    private boolean isAllowanceOfTakingTask;
    private int currentFloor;
    private int passengersOnBoard;

    private static Set<ElevatorTask> tasksInProcessing = new LinkedHashSet<>();
    private static Set<ElevatorTask> tasksInElevator = new LinkedHashSet<>();
    private static Set<ElevatorTask> tasksInWaiting = new LinkedHashSet<>();

    public enum Direction
    {
        UP, DOWN, STOPPED
    }

    private class ElevatorTask
    {
        private final int toFloor;
        private final int fromFloor;
        private final Direction direction;
        public static final int quantityOfPeople = 1;

        public ElevatorTask(int toFloor, int fromFloor, Direction direction)
        {
            this.toFloor = toFloor;
            this.fromFloor = fromFloor;
            this.direction = direction;
        }

        public Direction getDirection()
        {
            return direction;
        }

        public int getFromFloor()
        {
            return fromFloor;
        }

        public int getToFloor()
        {
            return toFloor;
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

    //InterfaceForUser
    public void goBoarding(int quantityOfPassengers)
    {
        try
        {
            System.out.println("Person is on board.");
            Thread.sleep(TIME_FOR_BOARDING);
            passengersOnBoard += quantityOfPassengers;
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }

    public void goOffBoarding()
    {
        try
        {
            System.out.println("Person is going off.");
            Thread.sleep(TIME_FOR_BOARDING);
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }

    public boolean call(Direction direction, int fromFloor)
    {
        directionOfMoving = direction;
        return true;
    }

    public void floorButtonPressed(int toFloor)
    {

    }

    private boolean closeDoor()
    {
        if (isOverWeight())
        {
            System.out.println("It's beyond its allowed capacity.");
            return false;
        }
        try
        {
            System.out.println("Door is closing...");
            Thread.sleep(TIME_FOR_CLOSING_DOOR);
            isAllowanceOfTakingTask = false;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isOverWeight()
    {
        return (passengersOnBoard * AVERAGE_OF_PERSON_WEIGTH) < (CAPACITY * AVERAGE_OF_PERSON_WEIGTH);
    }

    private boolean openDoor()
    {
        try
        {
            System.out.println("Door is opening...");
            Thread.sleep(TIME_FOR_OPENING_DOOR);
            isAllowanceOfTakingTask = true;
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
        return true;
    }

    public void board(Direction direction, int toFloor, int fromFloor)
    {
        working();
        if (toFloor > 0 && fromFloor > 0)
        {
            ElevatorTask task = new ElevatorTask(toFloor, fromFloor, direction);
            if (isAllowanceOfTakingTask)
            {
                tasksInProcessing.addAll(tasksInWaiting);
                tasksInProcessing.add(task);
            }
            else
            {
                tasksInWaiting.add(task);
            }
        }
    }

    private void working()
    {
        while (!tasksInProcessing.isEmpty() || directionOfMoving == Direction.STOPPED)
        {
            Iterator<ElevatorTask> iterator = tasksInProcessing.iterator();
            if (iterator.hasNext())
            {
                ElevatorTask currentTask = iterator.next();
                iterator.remove();
                tasksInElevator.add(currentTask);
                if (currentFloor == currentTask.fromFloor)
                {
                    openDoor();
                }
                else
                {
                    movingToFloor(currentTask.fromFloor);
                    currentFloor = currentTask.fromFloor;
                    openDoor();
                }
                goBoarding(ElevatorTask.quantityOfPeople);
                closeDoor();
                movingToFloor(currentTask.toFloor);
                openDoor();
                passengersOnBoard -= ElevatorTask.quantityOfPeople;

            }
        }
    }

    private void movingToFloor(Integer toFloor)
    {

        //process from taskInProcessing;
//        tasksInProcessing.stream().filter(t -> (t.toFloor.stream().filter( )).findFirst();
//        List<ElevatorTask> collect = tasksInProcessing
//                .stream()
//                .filter(t -> t.toFloor
//                        .stream()
//                        .anyMatch(f -> (f > currentFloor) && (f < toFloor)))
//                .collect(Collectors.toList());

//        tasksInProcessing.stream().forEach(t ->
//                {
//                    t.toFloor
//                }
//        );

        try
        {

            int dest;
            if (currentFloor > toFloor)
            {
                dest = currentFloor - toFloor;
            }
            else
            {
                dest = toFloor - currentFloor;
            }
            System.out.println("Moving to floor #" + toFloor);

            for (int i = 0; i < dest; i++)
            {
                Thread.sleep(HEIGHT_OF_ONE_FLOOR/SPEED_OF_ELEVATOR);
                currentFloor++;

                List<ElevatorTask> tasksOffBoard = tasksInElevator.stream().filter(t -> t.toFloor == currentFloor).collect(Collectors.toList());
                if (!tasksOffBoard.isEmpty())
                {
                    tasksInElevator.removeAll(tasksOffBoard);
                    openDoor();
                    goOffBoarding();
                }
                List<ElevatorTask> tasksOnBoard = tasksInProcessing.stream().filter(t -> t.fromFloor == currentFloor).collect(Collectors.toList());
                if (!tasksOnBoard.isEmpty())
                {
                    tasksInElevator.addAll(tasksOnBoard);
                    openDoor();
                    goBoarding(ElevatorTask.quantityOfPeople);
                    closeDoor();
                }
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }
}
