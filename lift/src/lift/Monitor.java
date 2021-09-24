package lift;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Monitor {

    int currFloor; // the floor the lift is currently on
    boolean moving; // true if the lift is moving, false if standing still with doors open
    int direction; // +1 if lift is going up, ‐1 if going down
    int[] waitEntry; // number of passengers waiting to enter the lift at the various floors
    List<Integer> waitEntry2;
    int[] waitExit; // number of passengers (in lift) waiting to leave at the various floors
    List<Integer> waitExit2; // number of passengers (in lift) waiting to leave at the various floors
    int load; // number of passengers currently in the lift
    private LiftView view;
    int enteringElevator;

    public Monitor(LiftView view) {
        this.view = view;
        waitEntry = new int[7];
        waitExit = new int[7];
        waitEntry2 = new ArrayList<>();
        waitExit2 = new ArrayList<>();
        direction = -1;
        enteringElevator = 0;
    }


    public synchronized void addArriveRequests(int floor) {
        waitEntry2.add(floor);
        waitEntry[floor]++;
        notifyAll();
    }

    public synchronized void removeRequests(int floor) {
        waitEntry2.remove((Integer) floor);
        waitEntry[floor]--;
        enteringElevator--;
        notifyAll();
    }

    public synchronized Boolean enterElevator(Passenger pass) throws InterruptedException {
/*waitEntry[pass.getStartFloor()]++;
        //notifyAll();
        while(moving eller load eller floor inte är pass floor )
            try
                wait

        waitEntry2--
        enterlift
        waitEntry--
        waitExit(getDestination)++
        notifyAll
  */
        //!(!moving && pass.getStartFloor() == currFloor)

        while (pass.getStartFloor() != currFloor || moving || waitExit2.size() > 3) wait();
            waitExit2.add(pass.getDestinationFloor());
            waitExit[pass.getDestinationFloor()]++;
            enteringElevator++;
            return true;
    }

//    public synchronized void waitForElevator(int fromFloor) throws InterruptedException {
//        while (!(!moving && fromFloor == currFloor)) wait();
//
//    }


    public void moveElevator() {
        moving = true;
        int floori = -2;


        while (currFloor != floori) {
            if (currFloor == 0 || currFloor == 6) direction = direction * -1;


            if (direction > 0) {
                view.moveLift(currFloor, currFloor + 1);
                currFloor++;
            } else {
                view.moveLift(currFloor, currFloor - 1);
                currFloor--;
            }

            floori = getCloses(waitExit2, floori);
            floori = getCloses(waitEntry2, floori);
        }
    }


    public int getCloses(List<Integer> list, int floori) {
        if (list.contains(currFloor)) return currFloor;
        else return floori;
    }


    public synchronized void exitElevator(int toFloor) {
        waitExit2.remove((Integer) toFloor);
        waitExit[toFloor]--;
        notifyAll();
    }


    public synchronized void waitForPassenger() throws InterruptedException {
        while (waitEntry2.isEmpty()) {
            wait();
        }
    }

    public synchronized void rideElevator(int toFloor) throws InterruptedException {
        while (currFloor != toFloor) wait();
    }

    public synchronized void liftOpenDoors() {
        moving = false;
        notifyAll();
    }



    public synchronized void liftCloseDoors() throws InterruptedException {
        while ((waitExit[currFloor] > 0 || (waitEntry[currFloor] > 0 && waitExit2.size() < 4)) || enteringElevator > 0) wait();
        view.closeDoors();

    }



}
