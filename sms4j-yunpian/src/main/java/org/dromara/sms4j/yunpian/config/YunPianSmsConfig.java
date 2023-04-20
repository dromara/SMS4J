package org.dromara.sms4j.yunpian.config;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.yunpian.service.YunPianSmsImpl;

public class YunPianSmsConfig {


    public YunpianConfig yunpianConfig(){
        return YunpianConfig.builder().build();
    }


    public ForestConfiguration forestConfiguration(YunpianConfig yunpianConfig) {
        return Forest.config().setBackendName("httpclient");
    }


    public SmsBlend smsBlend() {
        return new YunPianSmsImpl();
    }
}
