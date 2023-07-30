package org.dromara.sms4j.core.factory;

import cn.hutool.core.util.StrUtil;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.smsProxy.SmsInvocationHandler;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.core.load.SmsLoad;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SmsFactory
 * <p>构造工厂，用于获取一个厂商的短信实现对象
 * 在调用对应厂商的短信发送方法前，请先确保你的配置已经实现，否则无法发送该厂商对应的短信，一般情况下厂商会回执因缺少的配置所造成的的异常，但组件
 * 不会处理
 *
 * @author :Wind
 * 2023/4/8  15:55
 **/
public abstract class SmsFactory {

    /**
     * <p>框架维护的所有短信服务对象</p>
     * <p>key: configId，短信服务对象的唯一标识</p>
     * <p>value: 短信服务对象</p>
     */
    private final static Map<String, SmsBlend> blends = new LinkedHashMap<>();

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
        SmsBlend sms = create(config);
        register(sms);
        SmsLoad.starConfig(sms, config);
        return sms;
    }

    /**
     * createSmsBlend
     * <p> 创建一个指定厂商开启短信拦截后的实例，isRestricted为true时，创建出来的实例带有短信拦截性质，拦截的参数取决于配置文件
     * @param config 短信配置
     * @param isRestricted 是否为开启了短信拦截的实例
     * @author :Wind
    */
    public static SmsBlend createSmsBlend(SupplierConfig config, Boolean isRestricted) {
        SmsBlend sms = create(config);
        if(isRestricted){
            sms = renderWithRestricted(sms);
        }
        register(sms);
        SmsLoad.starConfig(sms, config);
        return sms;
    }

    private static SmsBlend create(SupplierConfig config) {
        BaseProviderFactory factory = ProviderFactoryHolder.requireForConfig(config);
        if(factory == null) {
            throw new SmsBlendException("不支持当前供应商配置");
        }
        return factory.createSms(config);
    }

    private static SmsBlend renderWithRestricted(SmsBlend sms) {
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

    /**
     * 通过configId获取短信服务对象
     * @param configId 唯一标识
     * @return 返回短信服务对象。如果未找到则返回null
     */
    public static SmsBlend getByConfigId(String configId) {
        return blends.get(configId);
    }

    /**
     * 通过供应商标识获取首个短信服务对象
     * @param supplier 供应商标识
     * @return 返回短信服务对象。如果未找到则返回null
     */
    public static SmsBlend getFirstBySupplier(String supplier) {
        if(StrUtil.isEmpty(supplier)) {
            throw new SmsBlendException("供应商标识不能为空");
        }
        return blends.values().stream()
                .filter(smsBlend -> supplier.equals(smsBlend.getSupplier()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 通过供应商标识获取短信服务对象列表
     * @param supplier 供应商标识
     * @return 返回短信服务对象列表。如果未找到则返回空列表
     */
    public static List<SmsBlend> getListBySupplier(String supplier) {
        List<SmsBlend> list = new ArrayList<>();
        if(StrUtil.isEmpty(supplier)) {
            throw new SmsBlendException("供应商标识不能为空");
        }
        list = blends.values().stream()
                .filter(smsBlend -> supplier.equals(smsBlend.getSupplier()))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 通过负载均衡服务获取短信服务对象
     * @return 返回短信服务列表
     */
    public static SmsBlend getByLoad() {
        return SmsLoad.getBeanLoad().getLoadServer();
    }

    /**
     * 获取全部短信服务对象
     * @return 短信服务对象列表
     */
    public static List<SmsBlend> getAll() {
        return new ArrayList<>(blends.values());
    }

    /**
     * 注册短信服务对象
     * @param smsBlend 短信服务对象
     */
    public static void register(SmsBlend smsBlend) {
        if(smsBlend == null) {
            throw new SmsBlendException("短信服务对象不能为空");
        }
        blends.put(smsBlend.getConfigId(), smsBlend);
    }

    /**
     * 以configId为标识，当短信服务对象不存在时，进行注册
     * @param smsBlend 短信服务对象
     * @return 是否注册成功
     * <p>当对象不存在时，进行注册并返回true</p>
     * <p>当对象已存在时，返回false</p>
     */
    public static boolean registerIfAbsent(SmsBlend smsBlend) {
        if(smsBlend == null) {
            throw new SmsBlendException("短信服务对象不能为空");
        }
        String configId = smsBlend.getConfigId();
        if(blends.containsKey(configId)) {
            return false;
        }
        blends.put(configId, smsBlend);
        return true;
    }

    /**
     * 注销短信服务对象
     * @param configId 标识
     * @return 是否注销成功
     * <p>当configId存在时，进行注销并返回true</p>
     * <p>当configId不存在时，返回false</p>
     */
    public static boolean unregister(String configId) {
        SmsBlend blend = blends.remove(configId);
        return blend != null;
    }

}
