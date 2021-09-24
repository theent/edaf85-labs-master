package lift;

public class LiftThread extends Thread {

    private Monitor monitor;
    private LiftView view;

    public LiftThread(LiftView view, Monitor monitor) {
        this.view = view;
        this.monitor = monitor;
    }


    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        int fromFloor = 0;
        Boolean directionUp = false;

        while (true) {

            if (monitor.waitExit2.isEmpty()) {
                try {
                    monitor.waitForPassenger();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            monitor.moveElevator();

            view.openDoors(monitor.currFloor);
            monitor.liftOpenDoors();


            try {
                monitor.liftCloseDoors();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }


}
