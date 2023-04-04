package kim.wind.sms.autoimmit.config;

import kim.wind.sms.comm.config.SmsBanner;
import lombok.Data;

@Data
public class SmsConfig {
    /**
     * 短信服务商
     */
    private String supplier;
    /**
     * 打印banner
     */
    private Boolean isPrint = true;

    /**
     * 是否开启短信限制
     */
    private String restricted;

    /**
     * 是否使用redis进行缓存
     */
    private String redisCache = "false";

    /**
     * 单账号每日最大发送量
     */
    private Integer accountMax;

    /**
     * 单账号每分钟最大发送
     */
    private Integer minuteMax;

    /**
     * 核心线程池大小
     */
    private Integer corePoolSize = 10;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize = 30;

    /**
     * 队列容量
     */
    private Integer queueCapacity = 50;

    /**
     * 活跃时间
     */
    private Integer keepAliveSeconds = 60;

    /**
     * 线程名字前缀
     */
    private String threadNamePrefix = "sms-executor-";

    /**
     * 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     */
    private Boolean shutdownStrategy = true;

void init(){
    if (isPrint) {
        SmsBanner.PrintBanner("v1.0.3");
    }
}
}
