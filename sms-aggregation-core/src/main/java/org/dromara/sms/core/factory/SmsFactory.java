package org.dromara.sms.core.factory;

import org.dromara.sms.aliyun.config.AlibabaSmsConfig;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.comm.enumerate.SupplierType;
import org.dromara.sms.comm.exception.SmsBlendException;
import org.dromara.sms.core.config.SupplierFactory;
import org.dromara.sms.huawei.config.HuaweiSmsConfig;
import org.dromara.sms.tencent.config.TencentSmsConfig;
import org.dromara.sms.unisms.config.UniSmsConfig;

/**
 * SmsFactory
 * <p>
 * @author :Wind
 * 2023/4/8  15:55
 **/
public class SmsFactory {
    private SmsFactory(){};

    /**
     *  createSmsBlend
     * <p>获取各个厂商的实现类
     * @param supplierType 厂商枚举
     * @author :Wind
    */
    public static SmsBlend createSmsBlend(SupplierType supplierType){
        switch (supplierType){
            case ALIBABA:
                return AlibabaSmsConfig.createAlibabaSms(SupplierFactory.getAlibabaConfig());
            case HUAWEI:
                return HuaweiSmsConfig.createHuaweiSms(SupplierFactory.getHuaweiConfig());
            case UNI_SMS:
                return UniSmsConfig.createUniSms(SupplierFactory.getUniConfig());
            case TENCENT:
                return TencentSmsConfig.createTencentSms(SupplierFactory.getTencentConfig());
        }
        throw new SmsBlendException("An attempt to construct a SmsBlend object failed. Please check that the enumeration is valid");
    }
}
