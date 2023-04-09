package org.dromara.sms.comm.factory;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import org.dromara.sms.comm.config.SmsConfig;
import org.dromara.sms.comm.config.SmsSqlConfig;
import org.dromara.sms.comm.delayedTime.DelayedTime;
import org.dromara.sms.comm.utils.JDBCTool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    /** jdbc工具*/
    private static JDBCTool jdbcTool;

    /** 数据库配置*/
    private static SmsSqlConfig smsSqlConfig;

    /** 获取forest请求对象*/
    private static ForestConfiguration forestConfiguration;

    /** 实例化自身对象防止被GC*/
    private static final BeanFactory beanFactory = new BeanFactory();

    private BeanFactory() {
    }

    /** 获取请求用的对象*/
    public static ForestConfiguration getForestConfiguration() {
        if (forestConfiguration == null){
            forestConfiguration = Forest.config().setBackendName("httpclient").setLogEnabled(smsConfig.getHttpLog());
        }
        return forestConfiguration;
    }

    public static DelayedTime getDelayedTime() {
      if (delayedTime == null){
          delayedTime = new DelayedTime();
      }
      return delayedTime;
    }

    public static Executor setExecutor(SmsConfig config) {
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

    public static Executor getExecutor() {
        return executor;
    }

    public static SmsConfig getSmsConfig(){
        if (smsConfig == null){
            smsConfig = new SmsConfig();
        }
        return smsConfig;
    }

    public static SmsSqlConfig getSmsSqlConfig(){
        if (smsSqlConfig == null){
            smsSqlConfig = new SmsSqlConfig();
        }
        return smsSqlConfig;
    }

    public static JDBCTool getJDBCTool(){
        if (jdbcTool == null){
            jdbcTool = new JDBCTool(getSmsSqlConfig());
        }
        return jdbcTool;
    }
}
