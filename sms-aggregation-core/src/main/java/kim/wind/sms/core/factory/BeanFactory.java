package kim.wind.sms.core.factory;

import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.core.config.SmsConfig;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * BeanFactory
 * <p> 单例bean工具管理,确保spring中管理的对象在这里都能拿到
 * @author :Wind
 * 2023/4/7  15:26
 **/
public class BeanFactory {

    /** 定时器*/
    private static DelayedTime delayedTime;

    /** 线程池*/
    private static Executor executor;

    /** 核心配置信息*/
    private static SmsConfig smsConfig;


    public static DelayedTime getDelayedTime() {
      if (delayedTime == null){
          delayedTime = new DelayedTime();
      }
      return delayedTime;
    }

    public static Executor setExecutor(SmsConfig config) {
        if (executor == null){
            // 创建一个线程池对象
            ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
            ex.setCorePoolSize(config.getCorePoolSize());
            ex.setMaxPoolSize(config.getMaxPoolSize());
            ex.setQueueCapacity(config.getQueueCapacity());
            ex.setKeepAliveSeconds(config.getKeepAliveSeconds());
            ex.setThreadNamePrefix(config.getThreadNamePrefix());
            ex.setWaitForTasksToCompleteOnShutdown(true);
            // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
            ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            //初始化线程池
            ex.initialize();
            executor = ex;
        }
        return executor;
    }

    public static Executor getExecutor() {
        return executor;
    }

    public static SmsConfig getSmsConfig(){
        if (smsConfig == null){
            smsConfig = new SmsConfig();
        }
        return smsConfig;
    }
}
