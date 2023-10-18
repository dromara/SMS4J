package org.dromara.oa.core.factory;

import org.dromara.oa.comm.config.OaConfig;
import org.dromara.oa.comm.errors.OaException;
import org.dromara.oa.core.service.OaBuild;
import org.dromara.oa.core.service.SenderImpl;

import java.util.HashMap;
import java.util.Map;

public class OaFactory {
    private static final Map<Object, OaConfig> configs = new HashMap<>();

    /**
     *  createMailClient
     * <p>从工厂获取一个OA发送实例
     * @param key 配置的标识key
     */
    public static SenderImpl createSender(Object key) {
        try {
            return OaBuild.build(configs.get(key));
        } catch (Exception e) {
            throw new OaException(e.getMessage());
        }
    }


    /**
     *  set
     * <p>将一个配置对象交给工厂
     * @param key 标识
     * @param config 配置对象
     */
    public static void put(Object key, OaConfig config){
        configs.put(key, config);
    }

}
