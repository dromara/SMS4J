package kim.wind.sms.comm.delayedTime;

import lombok.Data;

import java.util.TimerTask;

@Data
public class Task{

    private TimerTask runnable;//描述要执行的任务
    private long time;//什么时间执行,用时间戳来表示

}
