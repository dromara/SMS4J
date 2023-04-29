package org.dromara.sms4j.autoimmit.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.config.SmsSqlConfig;
import org.dromara.sms4j.comm.enumerate.ConfigType;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 配置源类型
     */
    private ConfigType configType = ConfigType.CONFIG_FILE;

    /**
     * 打印banner
     */
    private boolean isPrint = true;

    /**
     * 是否开启短信限制
     */
    private boolean restricted = false;

    /**
     * 是否使用redis进行缓存
     */
    private boolean redisCache = false;

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
    private int corePoolSize = 10;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 30;

    /**
     * 队列容量
     */
    private int queueCapacity = 50;

    /**
     * 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     */
    private boolean shutdownStrategy = true;

    /**
     * 是否打印http log
     */
    private boolean HttpLog = false;

    /**
     * sql config
     */
    private SmsSqlConfig sql;

    /**
     * alibaba
     */
    private AlibabaConfig alibaba;

    /**
     * huawei
     */
    private HuaweiConfig huawei;

    /**
     * yunpian
     */
    private YunpianConfig yunpian;

    /**
     * uni
     */
    private UniConfig uni;

    /**
     * tencent
     */
    private TencentConfig tencent;

    /**
     * jdcloud
     */
    private JdCloudConfig jdcloud;

    /**
     * cloopen
     */
    private CloopenConfig cloopen;

    /**
     * emay
     */
    private EmayConfig emay;

}
