package com.sprinklebit;

import java.util.*;
import java.util.stream.Collectors;

public class ElevatorControlBlock implements IElevatorControlBlock
{
    private static final List<ElevatorTask> tasksInProcessing = new LinkedList<>();
    private static final List<ElevatorTask> tasksInElevator = new LinkedList<>();
    private static final List<ElevatorTask> tasksInWaiting = new LinkedList<>();

    @Override
    public List<ElevatorTask> getTasksInProcessing()
    {
        return tasksInProcessing;
    }

    @Override
    public void addTasksInProcessing(Collection<ElevatorTask> tasksInProcessing)
    {
        ElevatorControlBlock.tasksInProcessing.addAll(tasksInProcessing);
    }

    @Override
    public List<ElevatorTask> getTasksInElevator()
    {
        return tasksInElevator;
    }

    @Override
    public List<ElevatorTask> getTasksInWaiting()
    {
        return tasksInWaiting;
    }

    @Override
    public List<ElevatorTask> getTasksGoingOffOnFloor(int currentFloor)
    {
        return tasksInElevator
                .stream()
                .filter(t -> t.getToFloor() == currentFloor)
                .collect(Collectors.toList());
    }

    @Override
    public List<ElevatorTask> getTasksGoingOnFloor(int currentFloor)
    {
        return tasksInProcessing
                .stream()
                .filter(t -> t.getFromFloor() == currentFloor)
                .collect(Collectors.toList());
    }
}
