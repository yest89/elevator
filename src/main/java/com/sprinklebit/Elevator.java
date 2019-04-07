package com.sprinklebit;

import java.util.*;

public class Elevator implements IElevator
{
    private ISensor sensor;
    private Direction directionOfMoving;
    private boolean stopElevator;
    private boolean isAllowanceOfTakingTask;
    private int currentFloor;
    private int passengersOnBoard;

    private Thread workingElevator;
    private IElevatorControlBlock controlBlock;
    public enum Direction
    {
        UP, DOWN
    }

    public Elevator()
    {
        this.currentFloor = ElevatorConfiguration.BEGINNING_FLOOR;
        this.isAllowanceOfTakingTask = true;
        this.sensor = new Sensor();
        this.controlBlock = new ElevatorControlBlock();
    }

    public void pressButtonStop(int codeTask, boolean stopped)
    {
        if (controlBlock.getTasksInElevator().stream().anyMatch(t -> t.getCode() == codeTask))
        {
            stopElevator = stopped;
        }
    }

    public void goBoarding(int quantityOfPassengers)
    {
        try
        {
            System.out.println("Person is on board on " + currentFloor + " floor.");
            Thread.sleep(ElevatorConfiguration.TIME_FOR_BOARDING * ElevatorConfiguration.MILLISECONDS_IN_SECOND);
            passengersOnBoard += quantityOfPassengers;
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }

    public void goOffBoarding(int quantityOfPassengers)
    {
        try
        {
            System.out.println("Person is going off on " + currentFloor + " floor.");
            Thread.sleep(ElevatorConfiguration.TIME_FOR_BOARDING * ElevatorConfiguration.MILLISECONDS_IN_SECOND);
            passengersOnBoard -= quantityOfPassengers;
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }

    private boolean closeDoor()
    {
        if (sensor.isOverLoaded(passengersOnBoard))
        {
            System.out.println("It's beyond its allowed capacity.");
            return false;
        }
        try
        {
            System.out.println("Door is closing on " + currentFloor + " floor.");
            Thread.sleep(ElevatorConfiguration.TIME_FOR_CLOSING_DOOR * ElevatorConfiguration.MILLISECONDS_IN_SECOND);
            isAllowanceOfTakingTask = false;
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
        return true;
    }

    private boolean openDoor()
    {
        try
        {
            System.out.println("Door is opening on " + currentFloor + " floor.");
            Thread.sleep(ElevatorConfiguration.TIME_FOR_OPENING_DOOR * ElevatorConfiguration.MILLISECONDS_IN_SECOND);
            isAllowanceOfTakingTask = true;
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
        return true;
    }

    public void board(ElevatorTask task)
    {
        if (validateInputData(task))
        {
            if (isAllowanceOfTakingTask)
            {
                controlBlock.addTasksInProcessing(controlBlock.getTasksInWaiting());
                controlBlock.getTasksInProcessing().add(task);
            }
            else
            {
                controlBlock.getTasksInWaiting().add(task);
            }
        }
    }

    private boolean validateInputData(ElevatorTask task)
    {
       return (task.getToFloor() > 0 && task.getToFloor() <= ElevatorConfiguration.NUMBER_OF_FLOORS)
               && (task.getFromFloor() > 0 && task.getFromFloor() <= ElevatorConfiguration.NUMBER_OF_FLOORS);
    }

    public void working()
    {
        workingElevator = new Thread(() ->
        {
            while (true)
            {
                ElevatorTask taskInProcess = null;
                if (!controlBlock.getTasksInProcessing().isEmpty())
                {
                    taskInProcess = controlBlock.getTasksInProcessing().get(0);
                    controlBlock.getTasksInElevator().add(taskInProcess);
                    controlBlock.getTasksInProcessing().remove(taskInProcess);
                }
                Iterator<ElevatorTask> iteratorInElevator =
                            controlBlock.getTasksInElevator().iterator();
                if (iteratorInElevator.hasNext())
                {
                    ElevatorTask currentTask = iteratorInElevator.next();
                    if (currentTask != null)
                    {
                        if (taskInProcess == currentTask)
                        {
                            if (currentFloor == currentTask.getFromFloor())
                            {
                                openDoor();
                            }
                            else
                            {
                                movingToFloor(currentTask.getFromFloor());
                                currentFloor = currentTask.getFromFloor();
                                openDoor();
                            }
                            goBoarding(ElevatorTask.quantityOfPeople);
                            closeDoor();
                        }
                        directionOfMoving = currentTask.getDirection();
                        if (!stopElevator)
                        {
                            System.out.println("Moving to floor #" + currentTask.getToFloor());
                            movingToFloor(currentTask.getToFloor());
                        }
                    }
                }
            }
        });
        workingElevator.start();
    }

    private void movingToFloor(Integer toFloor)
    {
        if(!stopElevator)
        {
            int amountOfFloors = countDistanceBetweenFloors(toFloor);
            for (int i = 0; i < amountOfFloors; i++)
            {
                goOneFloor();
                goWithinDirection();
                List<ElevatorTask> tasksOffBoard = controlBlock.getTasksGoingOffOnFloor(currentFloor);
                if (!tasksOffBoard.isEmpty())
                {
                    controlBlock.getTasksInElevator().removeAll(tasksOffBoard);
                    openDoor();
                    goOffBoarding(ElevatorTask.quantityOfPeople);
                }
                List<ElevatorTask> tasksOnBoard = controlBlock.getTasksGoingOnFloor(currentFloor);
                if (!tasksOnBoard.isEmpty())
                {
                    controlBlock.getTasksInElevator().addAll(tasksOnBoard);
                    controlBlock.getTasksInProcessing().removeAll(tasksOnBoard);
                    if (!isAllowanceOfTakingTask)
                    {
                        openDoor();
                    }
                    goBoarding(ElevatorTask.quantityOfPeople);
                }
                if (isAllowanceOfTakingTask)
                {
                    closeDoor();
                    if (tasksOnBoard.stream().anyMatch(ElevatorTask::isStopped))
                    {
                        System.out.println("Elevator is stopped.");
                        stopElevator = true;
                        return;
                    }
                }
            }
        }
    }

    private int countDistanceBetweenFloors(int toFloor)
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
        return amountOfFloors;
    }

    private void goOneFloor()
    {
        try
        {
            Thread.sleep((ElevatorConfiguration.HEIGHT_OF_ONE_FLOOR / ElevatorConfiguration.SPEED_OF_ELEVATOR
                                ) * ElevatorConfiguration.MILLISECONDS_IN_SECOND);
        }
        catch (InterruptedException e)
        {
            System.out.println("Something bad happened.");
        }
    }

    private void goWithinDirection()
    {
        switch (directionOfMoving)
        {
            case DOWN:
                currentFloor--;
                break;
            case UP:
                currentFloor++;
                break;
        }
    }

}
