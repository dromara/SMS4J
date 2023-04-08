package kim.wind.sms.comm.delayedTime;

import java.util.*;

/**
 * <p>类名: DelayedTime
 * <p>说明：  定时器队列
 *
 * @author :Wind
 * 2023/3/25  21:22
 **/
public class DelayedTime {

    private final List<Task> queue = Collections.synchronizedList(new ArrayList<>());

    public DelayedTime() {
        Timer timer = new Timer(true);
        Thread t = new Thread(() -> {
            while (true) try {
                if (queue.size() == 0){
                    continue;
                }
                Task take = queue.remove(0);
                timer.schedule(take.getRunnable(), take.getTime());
            } catch (Exception e) {
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
            queue.add(tasks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
