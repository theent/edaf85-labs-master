package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class WaterController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    WashingIO io;
    int dt = 2;
    double fillSpeed = 0.1;
    double drainSpeed = 0.2;
    boolean goal;


    public WaterController(WashingIO io) {
        // TODO
        this.io = io;
        goal = false;

    }

    @Override
    public void run() {
        // TODO

        int command = 0;
        double waterLevel = 0;
        ActorThread<WashingMessage> sender = null;

        while (true) {

            try {
                WashingMessage m = receiveWithTimeout(dt * 1000 / Settings.SPEEDUP);

                if(m != null) {
                    command = m.getCommand();
                    sender = m.getSender();

                    switch (command) {
                        case WashingMessage.WATER_IDLE:
                            io.fill(false);
                            io.drain(false);
                            break;
                        case WashingMessage.WATER_FILL:
                            waterLevel = m.getValue();
                            io.drain(false);
                            io.fill(true);
                            break;
                        case WashingMessage.WATER_DRAIN:
                            waterLevel = 0;
                            io.fill(false);
                            io.drain(true);
                            break;


                    }

                    goal = false;
                    System.out.println("got " + m);

                }


                if(command == WashingMessage.WATER_FILL) {
                    double currWaterLevel  = io.getWaterLevel();
                    if(currWaterLevel + fillSpeed * dt > waterLevel && !goal) {
                        goal = true;
                        io.fill(false);
                        sender.send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
                    }
                }
                else if(command == WashingMessage.WATER_DRAIN) {
                    double currWaterLevel  = io.getWaterLevel();
                    if(currWaterLevel == 0 && !goal) {
                        goal = true;
                        //io.drain(false);
                        sender.send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }
}
