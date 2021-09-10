import clock.AlarmClockEmulator;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;
import clock.io.Monitor;

import java.util.concurrent.Semaphore;

public class ClockMain {

    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput in = emulator.getInput();
        ClockOutput out = emulator.getOutput();


        Monitor monitor = new Monitor();
        monitor.setAlarmState(false);

        Semaphore lock = new Semaphore(1);

        Thread t1 = new Thread(() -> {

            long actualTime, deviation; //System.current gjorde den till long
            actualTime = System.currentTimeMillis();
            int count = 0;
            while (true) {

                actualTime += 1000; //uppdateras var 1000 sekund
                deviation = actualTime - System.currentTimeMillis(); //skillnaden ska vara 0

                try {
                    if (deviation > 0) { //clockrate är inte 1000, låter tråden sova den skillnaden i tiden
                        //sleep(deviation);
                        Thread.sleep(deviation);

                    }
                    lock.acquire();
                    monitor.incTimeNorm();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    out.displayTime(monitor.getH(), monitor.getM(), monitor.getS());                    //Fixa

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                try {
                    if (monitor.checkAlarm()) {
                        out.alarm();
                        out.setAlarmIndicator(true);
                        count = 0;
                        monitor.setAlarmState(true);
                    } else if (monitor.isAlarmState() && count == 20) {
                        count = 0;
                        out.setAlarmIndicator(false);
                    } else if (monitor.isAlarmState()) {
                        out.alarm();
                        count++;
                    }
                    lock.release();





                } catch (InterruptedException e) {
                    e.printStackTrace();
                }




            }

            });



        //out.displayTime(1, 2, 37);   // arbitrary time: just an example
        Thread t2 = new Thread(() -> {
            while (true) {
                try {
                    in.getSemaphore().acquire();
                    lock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UserInput userInput = in.getUserInput();
                int choice = userInput.getChoice();
                int h = userInput.getHours();
                int m = userInput.getMinutes();
                int s = userInput.getSeconds();



                switch (choice) {
                    case ClockInput.CHOICE_SET_TIME:
                        try {
                            monitor.setTimeNorm(h, m, s);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    case ClockInput.CHOICE_SET_ALARM:
                        try {
                            monitor.setTimeAlarm(h, m, s);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ClockInput.CHOICE_TOGGLE_ALARM:
                        //out.setAlarmIndicator();
                        out.setAlarmIndicator(false);
                        monitor.setAlarmState(false);
                        break;
                }
                    lock.release();
                //System.out.println("choice=" + choice + " h=" + h + " m=" + m + " s=" + s);
            }
        });


        t1.start();
        t2.start();

    }
}
