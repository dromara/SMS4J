package org.dromara.sms4j.core.factory;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.smsProxy.SmsInvocationHandler;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

import java.lang.reflect.Proxy;

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

    private SmsFactory() {
    }

    /**
     * createSmsBlend
     * <p>获取各个厂商的实现类
     *
     * @param config 短信配置
     * @author :Wind
     */
    public static SmsBlend createSmsBlend(SupplierConfig config) {
        BaseProviderFactory factory = ProviderFactoryHolder.requireForConfig(config);
        SmsBlend sms = factory.createSms(config);
        SmsHolder.put(sms);
        return sms;
    }

    /**
     *  createSmsBlend
     * <p> 创建一个指定厂商开启短信拦截后的实例，isRestricted为true时，创建出来的实例带有短信拦截性质，拦截的参数取决于配置文件
     * @param config 短信配置
     * @param isRestricted 是否为开启了短信拦截的实例
     * @author :Wind
    */
    public static SmsBlend createSmsBlend(SupplierConfig config,Boolean isRestricted){
        if (isRestricted){
            SmsBlend restrictedSmsBlend = getRestrictedSmsBlend(config);
            SmsHolder.put(restrictedSmsBlend);
            return restrictedSmsBlend;
        }
        return createSmsBlend(config);
    }

    // /**
    //  * getRestrictedSmsBlend
    //  * <p>获取某个厂商的带有短信拦截的实现
    //  *
    //  * @param supplierType 厂商枚举
    //  * @author :Wind
    //  */
    // public static SmsBlend getRestrictedSmsBlend(SupplierType supplierType) {
    //     SmsBlend smsBlend = beans.get(supplierType);
    //     if (Objects.isNull(smsBlend)) {
    //         smsBlend = getSmsBlend(supplierType);
    //         beans.put(supplierType, smsBlend);
    //     }
    //     return smsBlend;
    // }
    //
    // /**
    //  *  refreshRestrictedSmsBlend
    //  * <p>刷新带有短信拦截的对象实现
    //  * @param supplierType 厂商枚举
    //  * @author :Wind
    // */
//     public static void refreshRestrictedSmsBlend(SupplierType supplierType) {
//         refresh(supplierType);
//         beans.put(supplierType,getSmsBlend(supplierType));
//     }
//
     private static SmsBlend getRestrictedSmsBlend(SupplierConfig supplierType) {
         BaseProviderFactory factory = ProviderFactoryHolder.requireForConfig(supplierType);
         SmsBlend sms = factory.createSms(supplierType);
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
