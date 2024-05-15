package org.dromara.sms4j.api.manage;

import lombok.Getter;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsBlendConfigAware;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsConfigAware;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.util.HashMap;
import java.util.Map;


/*
 * Sms主要组件管理
 *
 * */
public abstract class InterceptorStrategySmsManager {

    //拦截器策略映射关系
    private static final HashMap<Class<?>, IInterceptorStrategy> interceptorStrategyMap = new HashMap<>();

    private static boolean forzen = false;

    //sms全局配置
    @Getter
    private static Object smsConfig;

    //所有厂商配置
    @Getter
    private static Map<String, Map<String, Object>> blends;

    //缓存配置
    private static SmsDao smsDao;

    //获取缓存实现
    public static SmsDao getSmsDao() {
        return InterceptorStrategySmsManager.smsDao;
    }

    /*
     * 设置缓存实现，若初始化阶段已过，再调用此方法会抛出异常
     * */
    public static void setSmsDao(SmsDao smsDao) {
        checkStatus();
        if (null == smsDao) {
            InterceptorStrategySmsManager.smsDao = SmsDaoDefaultImpl.getInstance();
            return;
        }
        InterceptorStrategySmsManager.smsDao = smsDao;
    }

    /*
     * 设置全局配置，若初始化阶段已过，再调用此方法会抛出异常
     * */
    public static void setSmsConfig(Object smsConfig) {
        checkStatus();
        InterceptorStrategySmsManager.smsConfig = smsConfig;
    }


    /*
     * 设置各渠道配置，若初始化阶段已过，再调用此方法会抛出异常
     * */
    public static void setBlends(Map<String, Map<String, Object>> blends) {
        checkStatus();
        if (null == blends) {
            InterceptorStrategySmsManager.blends = new HashMap<>();
            return;
        }
        InterceptorStrategySmsManager.blends = blends;
    }

    /*
     * 添加一个拦截器和执行策略的映射
     * */
    public static void setInterceptorStrategyMapping(IInterceptorStrategy strategy) {
        awareTransfer(strategy);
        interceptorStrategyMap.put(strategy.aPendingProblemWith(), strategy);
    }

    private static void awareTransfer(IInterceptorStrategy strategy) {
        if (strategy instanceof InterceptorStrategySmsDaoAware) {
            ((InterceptorStrategySmsDaoAware) strategy).setSmsDao(InterceptorStrategySmsManager.getSmsDao());
        }
        if (strategy instanceof InterceptorStrategySmsConfigAware) {
            ((InterceptorStrategySmsConfigAware) strategy).setSmsConfig(InterceptorStrategySmsManager.getSmsConfig());
        }
        if (strategy instanceof InterceptorStrategySmsBlendConfigAware) {
            ((InterceptorStrategySmsBlendConfigAware) strategy).setSmsBlendsConfig(InterceptorStrategySmsManager.getBlends());
        }
    }

    /*
     * 根据拦截器类型获取一个执行策略
     * */
    public static IInterceptorStrategy getStrategyByProblemClass(Class<?> clazz) {
        return interceptorStrategyMap.get(clazz);
    }

    private static void checkStatus() {
        if (forzen) {
            throw new RuntimeException("初始化后不可以更改SMS4J的组件实现引用");
        }
    }

    public static void freezes() {
        forzen = true;
    }
}
