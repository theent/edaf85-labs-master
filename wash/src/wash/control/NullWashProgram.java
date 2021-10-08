package wash.control;

import actor.ActorThread;

public class NullWashProgram extends ActorThread implements WashingProgram{



    @Override
    public void run() {
        System.out.println("THis is the null program");
    }
}
