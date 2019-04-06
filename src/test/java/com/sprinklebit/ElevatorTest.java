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
        elevator.board(Elevator.Direction.UP, 4, 1);
        elevator.board(Elevator.Direction.DOWN, 2, 3);
        elevator.board(Elevator.Direction.DOWN, 1, 4);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        elevator.board(Elevator.Direction.UP, 3, 1);
        elevator.board(Elevator.Direction.UP, 2, 1);
        elevator.board(Elevator.Direction.DOWN, 1, 3);

    }

    public static void main(String[] args)
    {
        Elevator elevator = new Elevator();
        elevator.working();
        ElevatorTest elevatorTest = new ElevatorTest(elevator);
        elevatorTest.test();
    }
}
