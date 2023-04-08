package kim.wind.sms.yunpian.config;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.yunpian.service.YunPianSmsImpl;

public class YunPianSmsConfig {


    public YunpianConfig yunpianConfig(){
        return new YunpianConfig();
    }


    public ForestConfiguration forestConfiguration(YunpianConfig yunpianConfig) {
        return Forest.config().setBackendName("httpclient").setLogEnabled(yunpianConfig.getHttpLog());
    }


    public SmsBlend smsBlend() {
        return new YunPianSmsImpl();
    }
}
