package org.dromara.sms.yunpian.config;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.yunpian.service.YunPianSmsImpl;

public class YunPianSmsConfig {


    public YunpianConfig yunpianConfig(){
        return new YunpianConfig();
    }


    public ForestConfiguration forestConfiguration(YunpianConfig yunpianConfig) {
        return Forest.config().setBackendName("httpclient");
    }


    public SmsBlend smsBlend() {
        return new YunPianSmsImpl();
    }
}
