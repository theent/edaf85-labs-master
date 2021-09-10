package clock.io;

import java.util.concurrent.Semaphore;

public class Clock {

    int h;
    int m;
    int s;

    public void setTime(int h, int m, int s) {
        setH(h);
        setM(m);
        setS(s);
    }

    void setH(int h) {
        this.h = h;
    }

    void setM(int m) {
        this.m = m;
    }

    void setS(int s) {
        this.s = s;
    }


    public int getH() {
        return h;
    }

    public int getM() {
        return m;
    }

    public int getS() {
        return s;
    }

    public void incTime() throws InterruptedException {
        setTime(h, m, s + 1);
        if (getS() == 60) {
            setTime(h, m + 1, 0);
            incHour();
        }
    }

    public void incHour() throws InterruptedException {
        if (getM() == 60) {
            setTime(h + 1, 0, s);
            incDay();
        }
    }

    public void incDay() throws InterruptedException {
        if (getH() == 24) {
            setTime(0, 0, 0);
        }
    }
}
