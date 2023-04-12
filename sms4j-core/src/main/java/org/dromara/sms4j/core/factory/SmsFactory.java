package org.dromara.sms4j.core.factory;

import org.dromara.sms4j.emay.config.EmaySmsConfig;
import org.dromara.sms4j.aliyun.config.AlibabaSmsConfig;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.cloopen.config.CloopenSmsConfig;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.huawei.config.HuaweiSmsConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudSmsConfig;
import org.dromara.sms4j.tencent.config.TencentSmsConfig;
import org.dromara.sms4j.unisms.config.UniSmsConfig;

/**
 * SmsFactory
 * <p>
 *
 * @author :Wind
 * 2023/4/8  15:55
 **/
public class SmsFactory {
    private SmsFactory() {
    }

    ;

    /**
     * createSmsBlend
     * <p>获取各个厂商的实现类
     *
     * @param supplierType 厂商枚举
     * @author :Wind
     */
    public static SmsBlend createSmsBlend(SupplierType supplierType) {
        switch (supplierType) {
            case ALIBABA:
                return AlibabaSmsConfig.createAlibabaSms(SupplierFactory.getAlibabaConfig());
            case HUAWEI:
                return HuaweiSmsConfig.createHuaweiSms(SupplierFactory.getHuaweiConfig());
            case UNI_SMS:
                return UniSmsConfig.createUniSms(SupplierFactory.getUniConfig());
            case TENCENT:
                return TencentSmsConfig.createTencentSms(SupplierFactory.getTencentConfig());
            case JD_CLOUD:
                return JdCloudSmsConfig.createJdCloudSms(SupplierFactory.getJdCloudConfig());
            case CLOOPEN:
                return CloopenSmsConfig.createCloopenSms(SupplierFactory.getCloopenConfig());
            case EMAY:
                return EmaySmsConfig.createEmaySms(SupplierFactory.getEmayConfig());
        }
        throw new SmsBlendException("An attempt to construct a SmsBlend object failed. Please check that the enumeration is valid");
    }
}
