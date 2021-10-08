package wash.control;
import actor.ActorThread;
import wash.io.WashingIO;
import wash.simulation.WashingSimulator;

public class Wash {

    public static void main(String[] args) throws InterruptedException {

        final int BUTTTON_STOP = 0;
        final int BUTTTON_ONE = 1;
        final int BUTTTON_TWO = 2;
        final int BUTTTON_THREE = 3;

        ActorThread<WashingMessage> currentProgram = new NullWashProgram();

        WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);

        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();

        //WashingProgram3 wash3 = new WashingProgram3(io, temp, water, spin);


        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);

            // TODO:
            // if the user presses buttons 1-3, start a washing program
            switch (n) {
                case BUTTTON_STOP:
                    currentProgram.interrupt();
                    currentProgram = new NullWashProgram();
                    break;
                case BUTTTON_ONE:
                    currentProgram = new WashingProgram1(io, temp, water, spin);
                    currentProgram.start();
                    break;
                case BUTTTON_TWO:
                    currentProgram = new WashingProgram2(io, temp, water, spin);
                    currentProgram.start();
                    break;
                case BUTTTON_THREE:
                    currentProgram = new WashingProgram3(io, temp, water, spin);
                    currentProgram.start();
                    break;

            }

            // if the user presses button 0, and a program has been started, stop it
        }


    }




};
