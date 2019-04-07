package com.sprinklebit;

import java.util.Collection;
import java.util.List;

public interface IElevatorControlBlock
{
    List<ElevatorTask> getTasksInProcessing();
    void addTasksInProcessing(Collection<ElevatorTask> tasksInProcessing);
    List<ElevatorTask> getTasksInElevator();
    List<ElevatorTask> getTasksInWaiting();
    List<ElevatorTask> getTasksGoingOffOnFloor(int currentFloor);
    List<ElevatorTask> getTasksGoingOnFloor(int currentFloor);
}
