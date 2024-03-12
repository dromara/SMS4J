package org.dromara.sms4j.comm.delayedTime;

import lombok.Data;

import java.util.TimerTask;

@Data
public class Task {
    /**
     * 描述要执行的任务
     */
    private TimerTask runnable;
    /**
     * 什么时间执行,用时间戳来表示
     */
    private Long time;

}
