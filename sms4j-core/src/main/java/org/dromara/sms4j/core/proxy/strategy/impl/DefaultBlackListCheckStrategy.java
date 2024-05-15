package org.dromara.sms4j.core.proxy.strategy.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.proxy.interceptor.BlackListMethodInterceptor;
import org.dromara.sms4j.core.proxy.strategy.IListCheckMethodStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 默认黑名单前置拦截策略
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class DefaultBlackListCheckStrategy implements InterceptorStrategySmsDaoAware, IListCheckMethodStrategy {

    private static final String CONFIG_PROPERTIES_PREFIX = "sms:blacklist:global";
    private static final String TRIGGER_RECORDING_PREFIX = "sms:blacklist:trigger";

    @Setter
    private SmsDao smsDao;

    @Override
    public Class<?> aPendingProblemWith() {
        return BlackListMethodInterceptor.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void checkListWithRecord(List<String> phones) {
        List<String> blackList = (List<String>) smsDao.get(CONFIG_PROPERTIES_PREFIX);
        if (CollUtil.isEmpty(blackList)) {
            return;
        }
        for (String phone : phones) {
            if (blackList.stream().anyMatch(black -> black.replace("-","").equals(phone))) {
                List<String> triggerList = (List<String>) smsDao.get(TRIGGER_RECORDING_PREFIX);
                if (CollUtil.isEmpty(triggerList)){
                    triggerList = new ArrayList<>();
                }
                triggerList.add(phone+": has trigger the blackList Check at - "+new Date());
                smsDao.set(TRIGGER_RECORDING_PREFIX,triggerList);
                throw new SmsBlendException("The phone:", phone + " hit global blacklist！");
            }
        }
    }
}
