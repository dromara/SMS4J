package org.dromara.sms4j.comm.delayedTime;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>类名: DelayedTime
 * <p>说明：  定时器
 * @author :Wind
 * 2023/3/25  21:22
 **/
public class DelayedTime {

    // ScheduledExecutorService 比 Timer 更稳：任务异常不会终止整个调度线程。
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "sms4j-delayed-time");
        thread.setDaemon(true);
        return thread;
    });


    /**
     * 延迟队列添加新任务
     */
    public void schedule(TimerTask task, long delay) {
        executor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

}
