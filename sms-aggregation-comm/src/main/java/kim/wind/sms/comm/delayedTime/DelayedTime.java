package kim.wind.sms.comm.delayedTime;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * <p>类名: DelayedTime
 * <p>说明：  定时器队列
 *
 * @author :Wind
 * 2023/3/25  21:22
 **/
public class DelayedTime {

    private final BlockingQueue<Task> queue = new PriorityBlockingQueue<>();



    public DelayedTime() {
        Timer timer = new Timer(true);
        Thread t = new Thread(() -> {
            while (true) try {
                Task take = queue.take();
                timer.schedule(take.getRunnable(), take.getTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
       t.start();
    }

    /**
     * 延迟队列添加新任务
     */
    public void schedule(TimerTask task, long delay) {
        try {
            Task tasks = new Task();
            tasks.setTime(delay);
            tasks.setRunnable(task);
            queue.put(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
