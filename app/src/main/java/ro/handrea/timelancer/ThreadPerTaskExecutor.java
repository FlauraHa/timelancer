package ro.handrea.timelancer;

import java.util.concurrent.Executor;

/**
 * Created on 11/25/17.
 */

public class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }
}