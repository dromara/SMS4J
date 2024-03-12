package org.dromara.sms4j.jiguang.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.jdcloud.service.JdCloudSmsImpl;
import org.dromara.sms4j.jiguang.service.JiguangSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;

/**
 * JiguangConfig
 * <p> 极光对象建造者
 *
 * @author :Wind
 * 2023/4/8  14:54
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JiguangFactory extends AbstractProviderFactory<JiguangSmsImpl, JiguangConfig> {

    private static final JiguangFactory INSTANCE = new JiguangFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static JiguangFactory instance() {
        return INSTANCE;
    }

    /**
     * 客户端对象
     *
     * @param config 极光短信配置属性
     * @return 客户端对象
     */
    public SMSClient client(JiguangConfig config) {
        //appKey https://www.jiguang.cn/dev2/#/overview/appCardList 开发者服务--->应用设置--->应用信息--> Master Secret
        String appKey = config.getAppKey();
        // masterSecret https://www.jiguang.cn/dev2/#/overview/appCardList 开发者服务--->应用设置--->应用信息--> Master Secret
        String masterSecret =  config.getMasterSecret();
        //初始化发短信客户端
        SMSClient smsClient = new SMSClient(masterSecret, appKey);
        return smsClient;
    }

    /**
     * 创建极光短信实现
     */
    @Override
    public JiguangSmsImpl createSms(JiguangConfig config) {
        return new JiguangSmsImpl(
                this.client(config),
                config
        );
    }
    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return "jiguang";
    }

}
