package kim.wind.sms.aliyun.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import kim.wind.sms.aliyun.service.AlibabaSmsImpl;
import kim.wind.sms.comm.exception.SmsBlendException;
import kim.wind.sms.comm.factory.BeanFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * AlibabaSmsConfig
 * <p> 阿里巴巴对象建造者
 * @author :Wind
 * 2023/4/8  14:54
 **/
@Slf4j
public class AlibabaSmsConfig {

    private static AlibabaSmsImpl alibabaSms;

    private static AlibabaSmsConfig alibabaSmsConfig;

    private  Client client(AlibabaConfig alibabaConfig){
        try {
            Config config = new Config()
                    //  AccessKey ID
                    .setAccessKeyId(alibabaConfig.getAccessKeyId())
                    //  AccessKey Secret
                    .setAccessKeySecret(alibabaConfig.getAccessKeySecret());
            // 访问的域名
            config.endpoint = alibabaConfig.getRequestUrl();
            return new Client(config);
        }catch  (Exception e){
            log.error(e.getMessage());
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     *  getAlibabaSms
     * <p> 建造一个短信实现对像
     * @author :Wind
    */
    public static AlibabaSmsImpl createAlibabaSms(AlibabaConfig alibabaConfig) {
        if (alibabaSmsConfig == null){
            alibabaSmsConfig = new AlibabaSmsConfig();
        }
        if (alibabaSms == null){
            alibabaSms = new AlibabaSmsImpl(alibabaSmsConfig.client(alibabaConfig),
                    alibabaConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime());
        }
        return alibabaSms;
    }

    /**
     *  refresh
     * <p> 刷新对象
     * @author :Wind
    */
    public static AlibabaSmsImpl refresh(AlibabaConfig alibabaConfig){
        // 如果配置对象为空则创建一个
        if (alibabaSmsConfig == null){
            alibabaSmsConfig = new AlibabaSmsConfig();
        }
        //重新构造一个实现对象
        alibabaSms= new AlibabaSmsImpl(alibabaSmsConfig.client(alibabaConfig),
                alibabaConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
        return alibabaSms;
    }

    private AlibabaSmsConfig() {
    }
}
