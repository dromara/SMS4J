package org.dromara.oa.comm.task.delayed;

import java.util.Timer;
import java.util.TimerTask;

public class DelayedTime {

    private final Timer timer = new Timer(true);


    /**
     * 延迟队列添加新任务
     */
    public void schedule(TimerTask task, long delay) {
        timer.schedule(task,delay);
    }

}
