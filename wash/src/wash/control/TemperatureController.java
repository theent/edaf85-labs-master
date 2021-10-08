package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class TemperatureController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    WashingIO io;
    double targetTemp;
    boolean heating;


    int dt = 10;
    double mu = 0.678; // 0.478 + 0.2 för marginal
    double ml = 0.0952; // Prov Varför funkar inte 0.00952
    boolean goal;

    public TemperatureController(WashingIO io) {
        // TODO
        this.io = io;
        targetTemp = 0;
        heating = false;
        goal = false;
    }

    @Override
    public void run() {
        int command;
        ActorThread<WashingMessage> sender = null;
        // TODO
while(true) {
    try {
        WashingMessage m = receiveWithTimeout(dt * 1000 / Settings.SPEEDUP);

        if(m != null && io.getWaterLevel() > 0) {
            command = m.getCommand();
            sender = m.getSender();
            if(command == 4) {          //Temp idle
                heating = false;
                targetTemp = 0;
            }
            else if(command == 5) {     //TempSet
                heating = true;
                targetTemp = m.getValue();
            }
            goal = false;
            System.out.println("got " + m);
            sender.send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));

        }

        //Undre
        if(heating) {
            double currTemp = io.getTemperature();
            if(currTemp < (targetTemp - 2 + ml)) {
                io.heat(true);
            }
            //Övre
            else if(currTemp > targetTemp - mu) {
                io.heat(false);
                if(!goal) {
                    goal = true;
                    sender.send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
                }
            }


        }


    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

    }
}
