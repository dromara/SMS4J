package org.dromara.sms4j.core.load;

import cn.hutool.core.bean.BeanUtil;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.provider.base.BaseProviderFactory;
import org.dromara.sms4j.provider.enumerate.SupplierType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SmsLoad
 * <p> 自定义的一个平滑加权负载服务，可以用于存放多个短信实现负载
 *
 * @author :Wind
 * 2023/4/21  20:49
 **/
public class SmsLoad {
    // 服务器列表，每个服务器有一个权重和当前权重
    private final List<LoadServer> LoadServers = new ArrayList<>();

    // 实例列表
    private static final Map<Object, SmsBlend> smsBlendMap = new HashMap<>();

    private static final SmsLoad smsLoad = new SmsLoad();

    private SmsLoad() {
    }

    public static SmsLoad newSmsLoad() {
        return new SmsLoad();
    }

    /**
     * addLoadServer
     * <p> 添加服务及其权重
     * <p>添加权重应注意，不要把某个权重设置的与其他权重相差过大，否则容易出现无法负载，单一选择的情况
     *
     * @param LoadServer 短信实现
     * @param weight     权重
     * @author :Wind
     */
    public void addLoadServer(SmsBlend LoadServer, int weight) {
        LoadServers.add(new LoadServer(LoadServer, weight, weight));
    }

    /**
     * removeLoadServer
     * <p>移除短信服务
     *
     * @param LoadServer 要移除的服务
     * @author :Wind
     */
    public void removeLoadServer(SmsBlend LoadServer) {
        for (int i = 0; i < LoadServers.size(); i++) {
            if (LoadServers.get(i).getSmsServer().equals(LoadServer)) {
                LoadServers.remove(i);
                break;
            }
        }
    }

    /**
     * getLoadServer
     * <p>根据负载算法获取一个可获取到的短信服务，这里获取到的服务必然是addLoadServer方法中添加过的服务，不会凭空出现
     *
     * @return SmsBlend 短信实现
     * @author :Wind
     */
    public synchronized SmsBlend getLoadServer() {
        int totalWeight = 0;
        LoadServer selectedLoadServer = null;
        // 计算所有服务器的权重总和，并选择当前权重最大的服务器
        for (LoadServer LoadServer : LoadServers) {
            totalWeight += LoadServer.getWeight();
            int currentWeight = LoadServer.getCurrentWeight() + LoadServer.getWeight();
            LoadServer.setCurrentWeight(currentWeight);
            if (selectedLoadServer == null || LoadServer.getCurrentWeight() > selectedLoadServer.getCurrentWeight()) {
                selectedLoadServer = LoadServer;
            }
        }
        // 如果没有服务器，则返回空
        if (selectedLoadServer == null) {
            return null;
        }
        // 更新选择的服务器的当前权重，并返回其地址
        int i = selectedLoadServer.getCurrentWeight() - totalWeight;
        selectedLoadServer.setCurrentWeight(i);
        return selectedLoadServer.getSmsServer();
    }

    /**
     * starConfig
     * <p> 创建smsBlend并加入到负载均衡器
     *
     * @param supplierConfig 厂商配置
     * @param supplierType   厂商枚举
     * @author :Wind
     */
    public static void starConfig(SupplierConfig supplierConfig, SupplierType supplierType) {
        BaseProviderFactory providerFactory = supplierType.getProviderFactory();
        SmsBlend smsBlend = providerFactory.createMultitonSms(supplierConfig);
        Map<String, Object> supplierConfigMap = BeanUtil.beanToMap(supplierConfig);
        Object weight = supplierConfigMap.getOrDefault("weight", 1);
        smsLoad.addLoadServer(smsBlend, Integer.parseInt(weight.toString()));
    }

    public static SmsLoad getBeanLoad() {
        return smsLoad;
    }
}

