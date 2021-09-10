package clock.io;

import java.util.concurrent.Semaphore;

public class Monitor {

    Semaphore lock;
    Clock normClock;
    Clock alarmClock;

    boolean alarmState;

    public Monitor() {
        lock = new Semaphore(1);
        normClock = new Clock();
        alarmClock = new Clock();
    }

    public void setTimeNorm(int h, int m, int s) throws InterruptedException {
        lock.acquire();
        normClock.setTime(h, m, s);
        lock.release();
    }

    public void setTimeAlarm(int h, int m, int s) throws InterruptedException {
        lock.acquire();
        alarmClock.setTime(h, m, s);
        lock.release();
    }

    public boolean checkAlarm() throws InterruptedException {
        int h = getH();
        int m = getM();
        int s = getS();
        lock.acquire();
        if (h == alarmClock.getH() && m == alarmClock.getM() && s == alarmClock.getS()) {
            lock.release();
            return true;
        }
        lock.release();
        return false;
    }

    public void incTimeNorm() throws InterruptedException {
        lock.acquire();
        normClock.incTime();
        lock.release();
    }

    public int getH() throws InterruptedException {
        lock.acquire();
        int temp = normClock.getH();
        lock.release();
        return temp;
    }

    public int getM() throws InterruptedException {
        lock.acquire();
        int temp = normClock.getM();
        lock.release();
        return temp;
    }

    public int getS() throws InterruptedException {
        lock.acquire();
        int temp = normClock.getS();
        lock.release();
        return temp;
    }

    public void setAlarmState(boolean alarmState) {
        this.alarmState = alarmState;
    }

    public boolean isAlarmState() {
        return alarmState;
    }


}
