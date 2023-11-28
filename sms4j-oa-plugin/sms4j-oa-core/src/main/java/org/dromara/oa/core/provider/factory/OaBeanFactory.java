package org.dromara.oa.core.provider.factory;

import org.dromara.oa.comm.task.delayed.DelayedTime;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.core.provider.config.OaConfig;

import java.util.concurrent.*;

public class OaBeanFactory {

    /** 定时器*/
    private static DelayedTime delayedTime;

    /** 线程池*/
    private static Executor executor;

    /** 核心配置信息*/
    private static OaConfig oaConfig;

    /** 优先级队列*/
    private static PriorityBlockingQueue<Request> priorityBlockingQueue;

    /** 优先级队列*/
    private static Boolean priorityExecutorThreadStatus = false;

    private OaBeanFactory() {
    }

    public static DelayedTime getDelayedTime() {
      if (delayedTime == null){
          delayedTime = new DelayedTime();
      }
      return delayedTime;
    }

    public static Executor setExecutor(OaConfig config) {
        if (executor == null){
            // 创建一个线程池对象
            ThreadPoolExecutor ex = new ThreadPoolExecutor(
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getQueueCapacity(),
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(config.getMaxPoolSize())
            );
            // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
            ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor = ex;
        }
        return executor;
    }

    public static PriorityBlockingQueue<Request> initPriorityBlockingQueue() {
        if (priorityBlockingQueue == null){
            // 创建一个线程池对象
            priorityBlockingQueue=new PriorityBlockingQueue<>();
        }
        return priorityBlockingQueue;
    }

    public static Executor getExecutor() {
        return executor;
    }

    public static OaConfig getSmsConfig(){
        if (oaConfig == null){
            oaConfig = new OaConfig();
        }
        return oaConfig;
    }

    public static PriorityBlockingQueue<Request> getPriorityBlockingQueue(){
        return priorityBlockingQueue;
    }

    public static Boolean getPriorityExecutorThreadStatus() {
        return priorityExecutorThreadStatus;
    }

    public static Boolean setPriorityExecutorThreadStatus(Boolean bo) {
        priorityExecutorThreadStatus=bo;
        return priorityExecutorThreadStatus;
    }

}
