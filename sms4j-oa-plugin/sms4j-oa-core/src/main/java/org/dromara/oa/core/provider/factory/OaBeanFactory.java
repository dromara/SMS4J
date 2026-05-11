package org.dromara.oa.core.provider.factory;

import lombok.Getter;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.task.delayed.DelayedTime;
import org.dromara.oa.core.provider.config.OaConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OaBeanFactory {

    /** 定时器*/
    private static DelayedTime delayedTime;

    /** 线程池*/
    @Getter
    private static Executor executor;

    /** 核心配置信息*/
    private static OaConfig oaConfig;

    /** 优先级队列*/
    @Getter
    private static PriorityBlockingQueue<Request> priorityBlockingQueue;

    /** 优先级队列*/
    @Getter
    private static Boolean priorityExecutorThreadStatus = false;

    private OaBeanFactory() {
    }

    public static synchronized DelayedTime getDelayedTime() {
      if (delayedTime == null){
          delayedTime = new DelayedTime();
      }
      return delayedTime;
    }

    public static synchronized Executor setExecutor(OaConfig config) {
        if (executor == null){
            executor = createExecutor(config);
        }
        return executor;
    }

    public static synchronized PriorityBlockingQueue<Request> initPriorityBlockingQueue() {
        if (priorityBlockingQueue == null){
            // 创建一个线程池对象
            priorityBlockingQueue=new PriorityBlockingQueue<>();
        }
        return priorityBlockingQueue;
    }

    public static synchronized OaConfig getSmsConfig(){
        if (oaConfig == null){
            oaConfig = new OaConfig();
        }
        return oaConfig;
    }

    public static Boolean setPriorityExecutorThreadStatus(Boolean bo) {
        priorityExecutorThreadStatus=bo;
        return priorityExecutorThreadStatus;
    }

    private static ThreadPoolExecutor createExecutor(OaConfig config) {
        ThreadPoolExecutor ex = new ThreadPoolExecutor(
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                60L,
                TimeUnit.SECONDS,
                // queueCapacity 表示等待队列容量，不能误用 maxPoolSize，否则配置语义会失效。
                new ArrayBlockingQueue<>(config.getQueueCapacity())
        );
        // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return ex;
    }

}
