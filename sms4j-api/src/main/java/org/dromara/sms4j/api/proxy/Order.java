package org.dromara.sms4j.api.proxy;
/**
 * 排序接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface Order {
    default public int getOrder(){
        return 999;
    }
}
