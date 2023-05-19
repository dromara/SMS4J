package org.dromara.sms4j.core.factory;

import org.dromara.sms4j.aliyun.config.AlibabaSmsConfig;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.smsProxy.RestrictedProcess;
import org.dromara.sms4j.api.smsProxy.SmsInvocationHandler;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.cloopen.config.CloopenSmsConfig;
import org.dromara.sms4j.comm.config.SmsConfig;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.core.SupplierSqlConfig;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.ctyun.config.CtyunSmsConfig;
import org.dromara.sms4j.emay.config.EmaySmsConfig;
import org.dromara.sms4j.huawei.config.HuaweiSmsConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudSmsConfig;
import org.dromara.sms4j.tencent.config.TencentSmsConfig;
import org.dromara.sms4j.unisms.config.UniSmsConfig;
import org.dromara.sms4j.yunpian.config.YunPianSmsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * SmsFactory
 * <p>构造工厂，用于获取一个厂商的短信实现对象
 * 在调用对应厂商的短信发送方法前，请先确保你的配置已经实现，否则无法发送该厂商对应的短信，一般情况下厂商会回执因缺少的配置所造成的的异常，但不会
 * 以java异常的形式抛出
 *
 * @author :Wind
 * 2023/4/8  15:55
 **/
public abstract class SmsFactory {

    private static Map<SupplierType, SmsBlend> beans = new HashMap<>();

    private SmsFactory() {
    }

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
            case CTYUN:
                return CtyunSmsConfig.createCtyunSms(SupplierFactory.getCtyunConfig());
            case YUNPIAN:
                return YunPianSmsConfig.createTencentSms(SupplierFactory.getYunpianConfig());
        }
        throw new SmsBlendException("An attempt to construct a SmsBlend object failed. Please check that the enumeration is valid");
    }

    /**
     * refresh
     * <p>刷新配置，用于切换配置后的刷新，防止因厂商sdk内部的保存导致配置更新不及时
     * 此方法会造成一定的性能损失，不建议经常性调用
     *
     * @author :Wind
     */
    public static void refresh() {
        AlibabaSmsConfig.refresh(SupplierFactory.getAlibabaConfig());
        HuaweiSmsConfig.refresh(SupplierFactory.getHuaweiConfig());
        UniSmsConfig.refresh(SupplierFactory.getUniConfig());
        TencentSmsConfig.refresh(SupplierFactory.getTencentConfig());
        JdCloudSmsConfig.refresh(SupplierFactory.getJdCloudConfig());
        CloopenSmsConfig.refresh(SupplierFactory.getCloopenConfig());
        EmaySmsConfig.refresh(SupplierFactory.getEmayConfig());
        CtyunSmsConfig.refresh(SupplierFactory.getCtyunConfig());
        YunPianSmsConfig.refresh(SupplierFactory.getYunpianConfig());
    }

    /**
     * refresh
     * <p>根据厂商类型枚举刷新对应厂商的配置，此方法不会刷新全部厂商的配置对象，只会重构所选厂商的对象，性能损失相对较小
     *
     * @param supplierType 厂商类型枚举
     * @author :Wind
     */
    public static void refresh(SupplierType supplierType) {
        switch (supplierType) {
            case ALIBABA:
                AlibabaSmsConfig.refresh(SupplierFactory.getAlibabaConfig());
                break;
            case HUAWEI:
                HuaweiSmsConfig.refresh(SupplierFactory.getHuaweiConfig());
                break;
            case UNI_SMS:
                UniSmsConfig.refresh(SupplierFactory.getUniConfig());
                break;
            case TENCENT:
                TencentSmsConfig.refresh(SupplierFactory.getTencentConfig());
                break;
            case JD_CLOUD:
                JdCloudSmsConfig.refresh(SupplierFactory.getJdCloudConfig());
                break;
            case CLOOPEN:
                CloopenSmsConfig.refresh(SupplierFactory.getCloopenConfig());
                break;
            case EMAY:
                EmaySmsConfig.refresh(SupplierFactory.getEmayConfig());
                break;
            case CTYUN:
                CtyunSmsConfig.refresh(SupplierFactory.getCtyunConfig());
                break;
            case YUNPIAN:
                YunPianSmsConfig.refresh(SupplierFactory.getYunpianConfig());
                break;
            default:
                throw new SmsBlendException("An attempt to construct a SmsBlend object failed. Please check that the enumeration is valid");
        }
    }

    /**
     * refreshSqlConfig
     * <p>重新读取sql配置
     *
     * @author :Wind
     */
    public static void refreshSqlConfig() {
        SupplierSqlConfig.refreshSqlConfig();
    }


    /**
     * getRestrictedSmsBlend
     * <p>获取某个厂商的带有短信拦截的实现
     *
     * @param supplierType 厂商枚举
     * @author :Wind
     */
    public static SmsBlend getRestrictedSmsBlend(SupplierType supplierType) {
        SmsBlend smsBlend = beans.get(supplierType);
        if (Objects.isNull(smsBlend)) {
            smsBlend = getSmsBlend(supplierType);
            beans.put(supplierType, smsBlend);
        }
        return smsBlend;
    }

    /**
     *  refreshRestrictedSmsBlend
     * <p>刷新带有短信拦截的对象实现
     * @param supplierType 厂商枚举
     * @author :Wind
    */
    public static void refreshRestrictedSmsBlend(SupplierType supplierType) {
        refresh(supplierType);
        beans.put(supplierType,getSmsBlend(supplierType));
    }

    private static SmsBlend getSmsBlend(SupplierType supplierType) {
        SmsBlend sms = createSmsBlend(supplierType);
        SmsInvocationHandler smsInvocationHandler = SmsInvocationHandler.newSmsInvocationHandler(
                sms,
                BeanFactory.getSmsConfig()
        );
        return (SmsBlend) Proxy.newProxyInstance(
                sms.getClass().getClassLoader(),
                new Class[]{SmsBlend.class},
                smsInvocationHandler
        );
    }
}
