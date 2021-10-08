package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class SpinController extends ActorThread<WashingMessage> {

    // TODO: add attributes

    WashingIO io;
    int spinMode;

    public SpinController(WashingIO io) {
        // TODO
        this.io = io;
        spinMode = 1;

    }

    @Override
    public void run() {
        try {
            int command = 0;
            // ... TODO ...

            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);


                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    command = m.getCommand();
                    switch (command) {
                        case WashingMessage.SPIN_SLOW:
                        io.setSpinMode(2);
                            break;
                        case WashingMessage.SPIN_FAST:
                            io.setSpinMode(4);
                            break;
                        case WashingMessage.SPIN_OFF:
                            io.setSpinMode(1);
                            break;
                    }
                    spinMode = command;
                    System.out.println("got " + m);
                    m.getSender().send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));

                }

            if(command == WashingMessage.SPIN_SLOW) {
                if(spinMode == 2) {
                    io.setSpinMode(3);
                    spinMode = 3;
                } else if(spinMode == 3) {
                    io.setSpinMode(2);
                    spinMode = 2;
                }
            }

                // ... TODO ...
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
