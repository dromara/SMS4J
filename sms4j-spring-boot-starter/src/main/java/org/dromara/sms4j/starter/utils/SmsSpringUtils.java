package org.dromara.sms4j.starter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * <p>类名: SmsSpringUtil
 * <p>说明：spring bean工具
 *
 * @author :Wind
 * 2023/3/25  0:13
 **/
@Slf4j
public class SmsSpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private final DefaultListableBeanFactory beanFactory;

    public SmsSpringUtils(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SmsSpringUtils.applicationContext == null) {
            SmsSpringUtils.applicationContext = applicationContext;
        }
    }

    public static Object getBean(String name) {
        try {
            return getApplicationContext().getBean(name);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * <p>说明：创建一个bean
     * @name: createBean
     * @author :Wind
     */
    public void createBean(Class<?> clazz) {
        String name = clazz.getName();
        beanFactory.createBean(clazz);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
    }

    public void createBean(Object o) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(o);
    }

    public void replaceBean(String beanName, Class<?> clazz) {
        String name = clazz.getName();
        beanFactory.removeBeanDefinition(beanName);
        beanFactory.createBean(clazz);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
    }

    public void deleteBean(String beanName) {
        beanFactory.removeBeanDefinition(beanName);
    }

    /**
     *  interfaceExist
     * <p>判断容器中是否存在某类型的bean
     * @author :Wind
    */
    public static boolean interfaceExist(Class<?>interfaceType) {
        return !applicationContext.getBeansOfType(interfaceType).isEmpty();
    }

    public static  <T> Map<String, T> getBeansOfType(Class<T> interfaceType) {
        return applicationContext.getBeansOfType(interfaceType);
    }
}
