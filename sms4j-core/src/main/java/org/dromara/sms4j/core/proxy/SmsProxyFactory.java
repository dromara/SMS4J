package org.dromara.sms4j.core.proxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.manage.InterceptorStrategySmsManager;
import org.dromara.sms4j.api.proxy.Order;
import org.dromara.sms4j.api.proxy.Restricted;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.proxy.SupplierSupportedMethodInterceptor;
import org.dromara.sms4j.provider.config.SmsConfig;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SmsBlend代理工厂
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 * @see SmsMethodInterceptor
 * @see SmsInvocationHandler
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsProxyFactory {

    public static final int BLACK_LIST_RECORDING_INTERCEPTOR_ORDER = -2;
    public static final int CORE_PARAM_VALIDATE_METHOD_INTERCEPTOR_ORDER = -1;
    public static final int BLACK_LIST_METHOD_INTERCEPTOR_ORDER = 0;
    public static final int ACCT_RESTRICTED_METHOD_INTERCEPTOR = 3;
    public static final int SPAN_RESTRICTED_METHOD_INTERCEPTOR = 4;
    public static final int SINGLE_BLEND_RESTRICTED_METHOD_INTERCEPTOR_ORDER = 2;
    private static final List<SmsMethodInterceptor> INTERCEPTORS = new ArrayList<>();

    public static SmsBlend getProxiedSmsBlend(SmsBlend smsBlend) {
        //获取当前渠道的拦截开关是否开启
        Objects.requireNonNull(smsBlend);
        // 若已被代理则直接返回，避免重复代理
        if (smsBlend instanceof Proxied) {
            return smsBlend;
        }
        List<SmsMethodInterceptor> appliedInterceptors = INTERCEPTORS.stream()
                .filter(interceptor -> canApply(interceptor, smsBlend))
                .filter(interceptor -> isRestricted(interceptor.getClass().getInterfaces(),((SmsConfig)InterceptorStrategySmsManager.getSmsConfig()).getRestricted()))
                .collect(Collectors.toList());
        return (SmsBlend) Proxy.newProxyInstance(
                smsBlend.getClass().getClassLoader(),
                new Class[]{SmsBlend.class, Proxied.class},
                new SmsInvocationHandler(smsBlend, appliedInterceptors)
        );
    }

    private static boolean isRestricted( Class<?>[] interfaces,boolean restricted) {
        if (restricted){
            return true;
        }
        for (Class<?> anInterface : interfaces) {
            if (anInterface.equals(Restricted.class)){
                return false;
            }
            for (Class<?> anInterfaceInterface : anInterface.getInterfaces()) {
                if (!isRestricted(anInterfaceInterface.getInterfaces(),restricted)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 增加拦截器
     */
    public static void addInterceptor(SmsMethodInterceptor interceptor) {
        Objects.requireNonNull(interceptor);
        // 尝试移除旧拦截器避免重复添加
        INTERCEPTORS.remove(interceptor);
        INTERCEPTORS.add(interceptor);
        INTERCEPTORS.sort(Comparator.comparingInt(Order::getOrder));
    }

    private static boolean canApply(SmsMethodInterceptor interceptor, SmsBlend smsBlend) {
        // 判断当前的执行器有没有开厂商过滤，支不支持当前厂商
        return !(interceptor instanceof SupplierSupportedMethodInterceptor)
                || ((SupplierSupportedMethodInterceptor) interceptor).getSupportedSuppliers().contains(smsBlend.getSupplier());
    }

    public interface Proxied {
    }
}
