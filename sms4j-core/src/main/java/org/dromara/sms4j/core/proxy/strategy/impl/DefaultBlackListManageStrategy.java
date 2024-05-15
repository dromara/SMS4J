package org.dromara.sms4j.core.proxy.strategy.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.Setter;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.core.proxy.strategy.IBlackListManageStrategy;
import org.dromara.sms4j.core.proxy.interceptor.BlackListRecordingProxyInterceptor;

import java.util.ArrayList;
import java.util.List;

public class DefaultBlackListManageStrategy implements IBlackListManageStrategy {
    private static final String REDIS_KEY_PREFIX = "sms:blacklist:global";
    private static final String TRIGGER_RECORDING_PREFIX = "sms:blacklist:trigger";


    @Setter
    private SmsDao smsDao;

    public List<String> getTriggerRecord(){
        List<String> triggerList = (List<String>) smsDao.get(TRIGGER_RECORDING_PREFIX);
        if (CollUtil.isEmpty(triggerList)){
            triggerList = new ArrayList<>();
        }
        return triggerList;
    }

    public void clearTriggerRecord(){
        smsDao.set(TRIGGER_RECORDING_PREFIX,new ArrayList<String>());
    }

    public void addBlackListItem(String phone) {
        List<String> blackList = getBlackList();
        blackList.add(phone);
        flushBlackList(blackList);
    }

    public void addBlackListItems(List<String> phones) {
        List<String> blackList = getBlackList();
        blackList.addAll(phones);
        flushBlackList(blackList);
    }

    public void removeBlackListItem(String phone) {
        List<String> blackList = getBlackList();
        blackList.remove(phone);
        flushBlackList(blackList);
    }

    public void removeBlackListItems(List<String> phones) {
        List<String> blackList = getBlackList();
        blackList.removeAll(phones);
        flushBlackList(blackList);
    }


    @SuppressWarnings("unchecked")
    private List<String> getBlackList() {
        List<String> blackList;
        Object cache = smsDao.get(REDIS_KEY_PREFIX);
        if (null != cache) {
            blackList = (List<String>) cache;
            return blackList;
        }
        blackList = new ArrayList<>();
        smsDao.set(REDIS_KEY_PREFIX, blackList);
        return blackList;
    }

    /**
     * 刷新黑名单
     *
     * @param blackList 新的黑名单列表
     */
    private void flushBlackList(List<String> blackList) {
        smsDao.set(REDIS_KEY_PREFIX, blackList);
    }

    @Override
    public Class<?> aPendingProblemWith() {
        return BlackListRecordingProxyInterceptor.class;
    }
}
