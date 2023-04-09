package org.dromara.sms.comm.delayedTime;

import java.util.*;

/**
 * <p>类名: DelayedTime
 * <p>说明：  定时器
 * @author :Wind
 * 2023/3/25  21:22
 **/
public class DelayedTime {

    private final Timer timer = new Timer(true);


    /**
     * 延迟队列添加新任务
     */
    public void schedule(TimerTask task, long delay) {
        timer.schedule(task,delay);
    }

}
