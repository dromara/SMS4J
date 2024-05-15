package org.dromara.sms4j.core.proxy.strategy;

import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.util.List;

/**
 * <p>说明：黑名单管理策略接口
 *
 * @author :sh1yu
 */
public interface IBlackListManageStrategy extends InterceptorStrategySmsDaoAware, IInterceptorStrategy  {

    /**
     * <p>说明：加入黑名单
     * addBlackListItem
     *
     * @param phone 需要加入黑名单的手机号
     * @author :sh1yu
     */
    void addBlackListItem(String phone);

    /**
     * <p>说明：批量加入黑名单
     * addBlackListItems
     *
     * @param phones 需要加入黑名单的手机号
     * @author :sh1yu
     */
    void addBlackListItems(List<String> phones);

    /**
     * <p>说明：从黑名单移除
     * removeBlackListItem
     *
     * @param phone 需要加入黑名单的手机号
     * @author :sh1yu
     */
    void removeBlackListItem(String phone);

    /**
     * <p>说明：批量从黑名单移除
     * removeBlackListItems
     *
     * @param phones 需要移除黑名单的手机号数组
     * @author :sh1yu
     */
    void removeBlackListItems(List<String> phones);

    /**
     * <p>说明：获取黑名单触发记录
     *
     * @author :sh1yu
     */
    List<String> getTriggerRecord();

    /**
     * <p>说明：清理黑名单触发记录
     *
     * @author :sh1yu
     */
    void clearTriggerRecord();
}
