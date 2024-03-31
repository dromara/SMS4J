package org.dromara.sms4j.core.proxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.api.proxy.Order;
import org.dromara.sms4j.api.proxy.SmsProcessor;
import org.dromara.sms4j.api.proxy.SuppotFilter;
import org.dromara.sms4j.api.proxy.aware.SmsBlendConfigAware;
import org.dromara.sms4j.api.proxy.aware.SmsConfigAware;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SmsBlend代理工厂
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public abstract class SmsProxyFactory {
    private static final LinkedList<SmsProcessor> processors = new LinkedList<>();

    public static SmsBlend getProxySmsBlend(SmsBlend smsBlend) {
        LinkedList<SmsProcessor> ownerProcessors = processors.stream().filter(processor -> !shouldSkipProcess(processor,smsBlend)).collect(Collectors.toCollection(LinkedList::new));
        return (SmsBlend) Proxy.newProxyInstance(smsBlend.getClass().getClassLoader(), new Class[]{SmsBlend.class}, new SmsInvocationHandler(smsBlend, ownerProcessors));
    }

    /**
     * 增加拦截器
     */
    public static void addProcessor(SmsProcessor processor) {
        //校验拦截器是否正确
        processorValidate(processor);
        awareTransfer(processor);
        processors.add(processor);
        processors.sort(Comparator.comparingInt(Order::getOrder));
    }

    /*
     *   @see SuppotFilter
     */
    public static boolean shouldSkipProcess(SmsProcessor processor, SmsBlend smsBlend) {
        //判断当前的执行器有没有开厂商过滤，支不支持当前厂商
        if (processor instanceof SuppotFilter) {
            List<String> supports = ((SuppotFilter) processor).getSupports();
            boolean exsit = supports.stream().anyMatch(support -> support.equals(smsBlend.getSupplier()));
            return !exsit;
        }
        return false;
    }

    //所有处理器需要的各个参数可以通过这种aware接口形式传给对象
    private static void awareTransfer(SmsProcessor processor) {
        if (processor instanceof SmsDaoAware){
            ((SmsDaoAware) processor).setSmsDao(getSmsDaoFromFramework());
        }
        if (processor instanceof SmsConfigAware){
            ((SmsConfigAware) processor).setSmsConfig(EnvirmentHolder.getSmsConfig());
        }
        if (processor instanceof SmsBlendConfigAware){
            ((SmsBlendConfigAware) processor).setSmsBlendsConfig(EnvirmentHolder.getBlends());
        }
    }

    //校验拦截器是否正确
    private static void processorValidate(SmsProcessor processor) {

    }

    //获取Sms的实现
    private static SmsDao getSmsDaoFromFramework() {
        SmsDao smsDao;
        smsDao = getSmsDaoFromFramework("org.dromara.sms4j.javase.config.SESmsDaoHolder", "JavaSE");
        if (null != smsDao) {
            return smsDao;
        }
        smsDao = getSmsDaoFromFramework("org.dromara.sms4j.starter.holder.SpringSmsDaoHolder", "SpringBoot");
        if (null != smsDao) {
            return smsDao;
        }
        smsDao = getSmsDaoFromFramework("org.dromara.sms4j.solon.holder.SolonSmsDaoHolder", "Solon");
        if (null != smsDao) {
            return smsDao;
        }
        log.debug("未找到合适框架加载，最终使用默认SmsDao！如无自实现SmsDao请忽略本消息！");
        return SmsDaoDefaultImpl.getInstance();
    }

    //获取Sms的实现
    private static SmsDao getSmsDaoFromFramework(String className, String frameworkName) {
        try {
            Class<?> clazz = Class.forName(className);
            Method getSmsDao = clazz.getMethod("getSmsDao", null);
            SmsDao smsDao = (SmsDao) getSmsDao.invoke(null, null);
            log.debug("{}:加载SmsDao成功，使用{}", frameworkName,smsDao.getClass().getName());
            return smsDao;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.debug("{}:尝试其他框架加载......", frameworkName);
        }
        return null;
    }

}
