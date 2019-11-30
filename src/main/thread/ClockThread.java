package main.thread;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * @author lenovo
 */
public class ClockThread extends Thread{

    private Label clock;
    private int hour;
    private int minute;
    private int seconds;

    public ClockThread(Label clock) {
        this.clock = clock;
        this.hour = this.minute = this.seconds = 0;
    }

    @Override
    public void run() {
        boolean var1 = true;

        while(true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ClockThread.this.clock.setText(String.format("%02d:%02d:%02d", ClockThread.this.hour, ClockThread.this.minute, ClockThread.this.seconds));
                }
            });

            try {
                Thread.sleep(1000L);
                ++this.seconds;
                if (this.seconds >= 60) {
                    this.seconds -= 60;
                    ++this.minute;
                    if (this.minute >= 60) {
                        this.minute -= 60;
                        ++this.hour;
                        if (this.hour >= 24) {
                            this.hour = this.minute = this.seconds = 0;
                        }
                    }
                }
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }
    }

}
