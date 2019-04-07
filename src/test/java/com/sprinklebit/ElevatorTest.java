package com.sprinklebit;

public class ElevatorTest
{
    private static final int FOURTY_SECONDS = 40000;
    private IElevator elevator;
    private ElevatorTask firstPerson;
    private ElevatorTask secondPersonWithStop;
    private ElevatorTask secondPerson;
    private ElevatorTask thirdPerson;

    public ElevatorTest(IElevator elevator)
    {
        this.elevator = elevator;
        firstPerson = new ElevatorTask(Elevator.Direction.UP, 4, 1, false);
        secondPersonWithStop = new ElevatorTask(Elevator.Direction.DOWN, 2, 3, true);
        secondPerson = new ElevatorTask(Elevator.Direction.DOWN, 2, 3, false);
        thirdPerson = new ElevatorTask(Elevator.Direction.DOWN, 1, 4, false);
    }

//  В тестовых целях имеется 3 человека,
//-	один находится на 1м этаже, хочет ехать на 4 этаж
//-	второй на 3м этаже, хочет ехать на 2 этаж
//-	третий на 4м этаже, хочет ехать на 1 этаж

    public void testWithStopButton()
    {

        elevator.board(firstPerson);
        elevator.board(secondPersonWithStop);
        elevator.board(thirdPerson);
        try
        {
            Thread.sleep(FOURTY_SECONDS);
            elevator.pressButtonStop(secondPersonWithStop.getCode(), false);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    public void test()
    {
        elevator.board(firstPerson);
        elevator.board(secondPerson);
        elevator.board(thirdPerson);
    }

    public static void main(String[] args)
    {
        Elevator elevator = new Elevator();
        elevator.working();
        ElevatorTest elevatorTest = new ElevatorTest(elevator);
        elevatorTest.testWithStopButton();
    }
}
