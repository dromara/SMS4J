package org.dromara.sms4j.core.proxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.api.proxy.Order;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.proxy.SupplierSupportedMethodInterceptor;
import org.dromara.sms4j.api.proxy.aware.SmsBlendConfigAware;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.api.proxy.aware.SmsConfigAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
public abstract class SmsProxyFactory {

    private static final List<SmsMethodInterceptor> INTERCEPTORS = new ArrayList<>();
    private static final String SPRING_SMS_DAO_LOAD_PATH = "org.dromara.sms4j.starter.holder.SpringSmsDaoHolder";
    private static final String SOLON_SMS_DAO_LOAD_PATH = "org.dromara.sms4j.solon.holder.SolonSmsDaoHolder";

    public static final int CORE_PARAM_VALIDATE_METHOD_INTERCEPTOR_ORDER = -1;
    public static final int BLACK_LIST_METHOD_INTERCEPTOR_ORDER = 0;
    public static final int BLACK_LIST_RECORDING_METHOD_INTERCEPTOR_ORDER = 1;
    public static final int RESTRICTED_METHOD_INTERCEPTOR = 3;
    public static final int SINGLE_BLEND_RESTRICTED_METHOD_INTERCEPTOR_ORDER = 2;

    public static SmsBlend getProxiedSmsBlend(SmsBlend smsBlend) {
        Objects.requireNonNull(smsBlend);
        // 若已被代理则直接返回，避免重复代理
        if (smsBlend instanceof Proxied) {
            return smsBlend;
        }
        List<SmsMethodInterceptor> appliedInterceptors = INTERCEPTORS.stream()
            .filter(processor -> canApply(processor, smsBlend))
            .collect(Collectors.toList());
        return (SmsBlend) Proxy.newProxyInstance(
            smsBlend.getClass().getClassLoader(),
            new Class[]{ SmsBlend.class, Proxied.class },
            new SmsInvocationHandler(smsBlend, appliedInterceptors)
        );
    }

    /**
     * 增加拦截器
     *
     * @param processor 处理器
     * TODO 当在SpringBoot或Solon环境中使用时，应当在完成加载后，主动从IOC容器获取并注册用户自定义的方法拦截器
     */
    public static void addProcessor(SmsMethodInterceptor processor) {
        Objects.requireNonNull(processor);
        // 调用Aware接口，将必要参数传递给处理器
        awareTransfer(processor);
        // 尝试移除旧拦截器避免重复添加
        INTERCEPTORS.remove(processor);
        INTERCEPTORS.add(processor);
        INTERCEPTORS.sort(Comparator.comparingInt(Order::getOrder));
    }

    private static boolean canApply(SmsMethodInterceptor processor, SmsBlend smsBlend) {
        // 判断当前的执行器有没有开厂商过滤，支不支持当前厂商
        return !(processor instanceof SupplierSupportedMethodInterceptor)
            || ((SupplierSupportedMethodInterceptor)processor).getSupportedSuppliers().contains(smsBlend.getSupplier());
    }

    private static void awareTransfer(SmsMethodInterceptor processor) {
        if (processor instanceof SmsDaoAware){
            ((SmsDaoAware) processor).setSmsDao(getSmsDaoFromFramework());
        }
        if (processor instanceof SmsConfigAware){
            ((SmsConfigAware) processor).setSmsConfig(EnvironmentHolder.getSmsConfig());
        }
        if (processor instanceof SmsBlendConfigAware){
            ((SmsBlendConfigAware) processor).setSmsBlendsConfig(EnvironmentHolder.getBlends());
        }
    }

    private static SmsDao getSmsDaoFromFramework() {
        SmsDao smsDao;
        smsDao = getSmsDaoFromFramework(SPRING_SMS_DAO_LOAD_PATH, "SpringBoot");
        if (null != smsDao) {
            return smsDao;
        }
        smsDao = getSmsDaoFromFramework(SOLON_SMS_DAO_LOAD_PATH, "Solon");
        if (null != smsDao) {
            return smsDao;
        }
        log.error("尝试框架加载失败，最终使用默认SmsDao！");
        return SmsDaoDefaultImpl.getInstance();
    }

    private static SmsDao getSmsDaoFromFramework(String className, String frameworkName) {
        try {
            Class<?> clazz = Class.forName(className);
            Method getSmsDao = clazz.getMethod("getSmsDao");
            return (SmsDao) getSmsDao.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("{}:加载SmsDao失败，尝试其他框架加载......", frameworkName);
        }
        return null;
    }

    public interface Proxied {}
}
