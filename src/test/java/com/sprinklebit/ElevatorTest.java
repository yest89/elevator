package com.sprinklebit;

public class ElevatorTest
{
    private IElevator elevator;

    public ElevatorTest(IElevator elevator)
    {
        this.elevator = elevator;
    }

//  В тестовых целях имеется 3 человека,
//-	один находится на 1м этаже, хочет ехать на 4 этаж
//-	второй на 3м этаже, хочет ехать на 2 этаж
//-	третий на 4м этаже, хочет ехать на 1 этаж

    public void test()
    {
        elevator.board(Elevator.Direction.UP, 4, 1, 1);
        elevator.board(Elevator.Direction.DOWN, 3, 2, 1);
        elevator.board(Elevator.Direction.DOWN, 1, 4, 1);
    }

    public static void main(String[] args)
    {
        ElevatorTest elevatorTest = new ElevatorTest(new Elevator());
        elevatorTest.test();
    }
}
