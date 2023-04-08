package kim.wind.sms.unisms.config;

import com.apistd.uni.Uni;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.comm.factory.BeanFactory;
import kim.wind.sms.unisms.service.UniSmsImpl;

/**
 * UniSmsConfig
 * <p>合一短信建造对象
 * @author :Wind
 * 2023/4/8  15:46
 **/
public class UniSmsConfig {

    private UniSmsConfig(){}

    private static UniSmsConfig uniSmsConfig;

    private static UniSmsImpl uniSmsImpl;


    /** 短信配置*/
    private void buildSms(UniConfig uniConfig){
        if (uniConfig.getIsSimple()){
            Uni.init(uniConfig.getAccessKeyId());
        }else {
            Uni.init(uniConfig.getAccessKeyId(),uniConfig.getAccessKeySecret());
        }
    }

    /**
     *  createUniSms
     * <p>建造一个短信实现对像
     * @author :Wind
    */
    public static UniSmsImpl createUniSms(UniConfig uniConfig){
        if (uniSmsConfig == null){
            uniSmsConfig = new UniSmsConfig();
        }
        if (uniSmsImpl == null){
            uniSmsConfig.buildSms(uniConfig);
            uniSmsImpl = new UniSmsImpl(
                    uniConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime()
            );
        }
        return uniSmsImpl;
    }

    /**
     *  refresh
     * <p>刷新对象
     * @author :Wind
    */
    public static UniSmsImpl refresh(UniConfig uniConfig){
        if (uniSmsConfig == null){
            uniSmsConfig = new UniSmsConfig();
        }
        uniSmsConfig.buildSms(uniConfig);
        uniSmsImpl = new UniSmsImpl(
                uniConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return uniSmsImpl;
    }
}
