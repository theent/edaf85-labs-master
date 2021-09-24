package lift;


import java.util.Random;

public class PassengerThread extends Thread {

    private LiftView view;
    private Monitor monitor;

    public PassengerThread(LiftView view, Monitor monitor) {
        this.view = view;
        this.monitor = monitor;
    }

    public void run() {


        Passenger pass = view.createPassenger();
        int fromFloor = pass.getStartFloor();
        int toFloor = pass.getDestinationFloor();

        int delay = new Random().nextInt(46);

        try {
            sleep(delay * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        pass.begin();                        // walk in (from left)

        monitor.addArriveRequests(fromFloor);


//        try {
//            monitor.waitForElevator(fromFloor);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        try {
            if (monitor.enterElevator(pass)) {
                pass.enterLift();                    // step inside
                monitor.removeRequests(fromFloor);
                monitor.rideElevator(toFloor);

                pass.exitLift();                     // leave lift
                monitor.exitElevator(toFloor);

                pass.end();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//           view.closeDoors();
//            view.moveLift(fromFloor, toFloor);   // ride lift
//            view.openDoors(toFloor);


        //view.closeDoors();
    }// walk out (to the right)

}







