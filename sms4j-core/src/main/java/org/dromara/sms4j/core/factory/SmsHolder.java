package org.dromara.sms4j.core.factory;

import cn.hutool.core.util.StrUtil;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.load.SmsLoad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短信服务对象持有者
 *
 * @author xiaoyan
 * @since 3.0.0
 */
public abstract class SmsHolder {

    /**
     * <p>框架维护的所有短信服务对象</p>
     * <p>key: configId，短信服务对象的唯一标识</p>
     * <p>value: 短信服务对象</p>
     */
    private final static Map<String, SmsBlend> blends = new LinkedHashMap<>();

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
    public static void put(SmsBlend smsBlend) {
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
    public static boolean putIfAbsent(SmsBlend smsBlend) {
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

}
