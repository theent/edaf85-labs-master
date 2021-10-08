package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class WashingProgram1 extends ActorThread<WashingMessage> {
    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;

    public WashingProgram1(WashingIO io,
                           ActorThread<WashingMessage> temp,
                           ActorThread<WashingMessage> water,
                           ActorThread<WashingMessage> spin) {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }

    @Override
    public void run() {
        try {
            System.out.println("washing program 3 started");

            // Lock the hatch
            io.lock(true);
            // Instruct SpinController to rotate barrel slowly, back and forth
            // Expect an acknowledgment in response.


            // Spin for five simulated minutes (one minute == 60000 milliseconds)
            //Thread.sleep(5 * 60000);
            water.send(new WashingMessage(this, WashingMessage.WATER_FILL, 10));
            WashingMessage ack4 = receive();
            System.out.println("washing program 1 got " + ack4);
            //water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));


            //Thread.sleep(5 * 60000);
            temp.send(new WashingMessage(this, WashingMessage.TEMP_SET, 40));
            WashingMessage ack5 = receive();
            System.out.println("washing program 1 got " + ack5);

            System.out.println("setting SPIN_SLOW...");
            //Thread.sleep(5 * 60000);
            spin.send(new WashingMessage(this, WashingMessage.SPIN_SLOW));
            WashingMessage ack1 = receive();


            Thread.sleep(42 * 60000 / Settings.SPEEDUP);

            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            receive();


            //water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));

            water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
            WashingMessage ack6 = receive();
            System.out.println("washing program 1 got " + ack6);
            Thread.sleep(1 * 60000 / Settings.SPEEDUP);

            for (int i = 0; i <= 5; i++) {


                water.send(new WashingMessage(this, WashingMessage.WATER_FILL, 10));
                receive();

                Thread.sleep(2 * 60000 / Settings.SPEEDUP);

                water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
                WashingMessage ack9 = receive();
                System.out.println("washing program 1 got " + ack9);


            }

            Thread.sleep(3 * 60000 / Settings.SPEEDUP);

            spin.send(new WashingMessage(this, WashingMessage.SPIN_FAST));
            WashingMessage ack10 = receive();
            Thread.sleep(5 * 60000 / Settings.SPEEDUP);

            //Thread.sleep(11 * 60000 / Settings.SPEEDUP);


            // Instruct SpinController to stop spin barrel spin.
            // Expect an acknowledgment in response.
            System.out.println("setting SPIN_OFF...");
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            WashingMessage ack2 = receive();
            System.out.println("washing program 1 got " + ack2);
            // Now that the barrel has stopped, it is safe to open the hatch.
            io.lock(false);

            System.out.println("washing program 1 finished");
        } catch (InterruptedException e) {

            // If we end up here, it means the program was interrupt()'ed:
            // set all controllers to idle
            temp.send(new WashingMessage(this, WashingMessage.TEMP_SET, 0));
            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            System.out.println("washing program terminated");
        }
    }
}
