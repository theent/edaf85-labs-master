import lift.LiftThread;
import lift.LiftView;
import lift.Monitor;
import lift.PassengerThread;

import java.util.ArrayList;
import java.util.List;

public class LiftMain {
    public static void main(String[] args) {

        LiftView view = new LiftView();
        Monitor monitor = new Monitor(view);
        List<Thread> threads = new ArrayList<>();
        LiftThread liftThread = new LiftThread(view, monitor);
        liftThread.start();
        PassengerThread p1;

        for(int i = 0; i < 20; i++) {
            p1 = new PassengerThread(view, monitor);
            threads.add(p1);
        }


        for (Thread tr : threads) {
            tr.start();
        }






    }
}
