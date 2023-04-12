package org.dromara.sms4j.comm.delayedTime;

import lombok.Data;

import java.util.TimerTask;

@Data
public class Task{

    private TimerTask runnable;//描述要执行的任务
    private Long time;//什么时间执行,用时间戳来表示

}
