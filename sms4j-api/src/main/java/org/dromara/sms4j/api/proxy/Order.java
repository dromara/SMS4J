package org.dromara.sms4j.api.proxy;

/**
 * 排序接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface Order {

    /**
     * 获取排序值，排序值越大的对象则优先级越低
     *
     * @return 排序值
     */
    default int getOrder(){
        return Integer.MAX_VALUE;
    }
}
