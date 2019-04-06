package com.sprinklebit;

import java.util.*;
import java.util.stream.Collectors;

public class Elevator implements IElevator
{
    public static final int CAPACITY = 10;
    public static final int NUMBER_OF_FLOORS = 4;
    public static final int SPEED_OF_ELEVATOR = 1; // m/s
    public static final int BEGINNING_FLOOR = 1;
    public static final int HEIGHT_OF_ONE_FLOOR = 1; // m/s
    public static final int TIME_FOR_CLOSING_DOOR = 3; // s
    public static final int TIME_FOR_OPENING_DOOR = 3; // s
    public static final int MILLISECONDS_IN_SECOND = 1000;
    public static final int TIME_FOR_BOARDING = 3; // s
    public static final int AVERAGE_OF_PERSON_WEIGTH = 70; // kg

    private Direction directionOfMoving;
    private boolean isAllowanceOfTakingTask;
    private int currentFloor;
    private int passengersOnBoard;

    private Thread workingElevator;
    private static Set<ElevatorTask> tasksInProcessing = new LinkedHashSet<>();
    private static Set<ElevatorTask> tasksInElevator = new LinkedHashSet<>();
    private static Set<ElevatorTask> tasksInWaiting = new LinkedHashSet<>();

    public enum Direction
    {
        UP, DOWN, STOPPED
    }

    public Elevator()
    {
        this.currentFloor = BEGINNING_FLOOR;
        this.isAllowanceOfTakingTask = true;
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

    public void goBoarding(int quantityOfPassengers)
    {
        try
        {
            System.out.println("Person is on board on " + currentFloor + " floor.");
            Thread.sleep(TIME_FOR_BOARDING * MILLISECONDS_IN_SECOND);
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
            System.out.println("Person is going off on " + currentFloor + " floor.");
            Thread.sleep(TIME_FOR_BOARDING * MILLISECONDS_IN_SECOND);
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
            System.out.println("Door is closing on " + currentFloor + " floor.");
            Thread.sleep(TIME_FOR_CLOSING_DOOR * MILLISECONDS_IN_SECOND);
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
        return (passengersOnBoard * AVERAGE_OF_PERSON_WEIGTH) > (CAPACITY * AVERAGE_OF_PERSON_WEIGTH);
    }

    private boolean openDoor()
    {
        try
        {
            System.out.println("Door is opening on " + currentFloor + " floor.");
            Thread.sleep(TIME_FOR_OPENING_DOOR * MILLISECONDS_IN_SECOND);
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

    public void working()
    {
        workingElevator = new Thread(() -> {
            while (true)
            {
                Iterator<ElevatorTask> iteratorProcess = tasksInProcessing.iterator();
                ElevatorTask taskInProcess = null;
                if (iteratorProcess.hasNext())
                {
                    taskInProcess = iteratorProcess.next();
                    tasksInElevator.add(taskInProcess);
                }
                Iterator<ElevatorTask> iteratorInElevator = tasksInElevator.iterator();
                if (iteratorInElevator.hasNext())
                {
                    ElevatorTask currentTask = iteratorInElevator.next();
                    if (taskInProcess == currentTask) {
                        if (currentFloor == currentTask.fromFloor) {
                            openDoor();
                        } else {
                            movingToFloor(currentTask.fromFloor);
                            currentFloor = currentTask.fromFloor;
                            openDoor();
                        }

                        goBoarding(ElevatorTask.quantityOfPeople);
                        closeDoor();
                    }
                    directionOfMoving = currentTask.direction;
                    System.out.println("Moving to floor #" + currentTask.toFloor);
                    movingToFloor(currentTask.toFloor);
                    tasksInProcessing.remove(currentTask);
                }
            }
        });
        workingElevator.start();
    }

    private void movingToFloor(Integer toFloor)
    {
        try
        {
            int amountOfFloors;
            if (currentFloor > toFloor)
            {
                amountOfFloors = currentFloor - toFloor;
            }
            else
            {
                amountOfFloors = toFloor - currentFloor;
            }
            if (tasksInElevator.isEmpty())
            {
                System.out.println("Moving to floor #" + toFloor);
                Thread.sleep((amountOfFloors/SPEED_OF_ELEVATOR) * MILLISECONDS_IN_SECOND);
            }
            else
            {
                for (int i = 0; i < amountOfFloors; i++)
                {
                    Thread.sleep((HEIGHT_OF_ONE_FLOOR/SPEED_OF_ELEVATOR) * MILLISECONDS_IN_SECOND);
                    switch (directionOfMoving)
                    {
                        case DOWN:
                            currentFloor--;
                            break;
                        case UP:
                            currentFloor++;
                            break;
                        case STOPPED:
                            break;
                    }

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
                        tasksInProcessing.removeAll(tasksOnBoard);
                        if (!isAllowanceOfTakingTask)
                        {
                            openDoor();
                        }
                        goBoarding(ElevatorTask.quantityOfPeople);
                    }
                    if(isAllowanceOfTakingTask)
                    {
                        closeDoor();
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }
}
