package com.sprinklebit;

public interface IElevator
{
    void board(ElevatorTask task);
    void pressButtonStop(int codeTask, boolean stopped);
}
