package kim.wind.sms.comm.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>类名: SpringUtil
 * <p>说明：spring bean工具
 *
 * @author :Wind
 * 2023/3/25  0:13
 **/
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private final DefaultListableBeanFactory beanFactory;

    public SpringUtil(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static Object getBean(String name) {
        try {
            return getApplicationContext().getBean(name);
        } catch (Exception e) {
            return null;
        }

    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>说明：创建一个bean
     *
     * @param
     * @name: createBean
     * @author :Wind
     */
    public void createBean(Class<?> clazz) {
        String name = clazz.getName();
        beanFactory.createBean(clazz);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
    }

    public void createBean(String name, Object o) {
        beanFactory.registerSingleton(name, o);
    }

    public void replaceBean(String beanName,Class<?> clazz){
        String name = clazz.getName();
        beanFactory.removeBeanDefinition(beanName);
        beanFactory.createBean(clazz);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
    }
    public void deleteBean(String beanName){
        beanFactory.removeBeanDefinition(beanName);
    }
}
