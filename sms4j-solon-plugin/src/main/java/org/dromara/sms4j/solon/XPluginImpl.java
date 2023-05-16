package org.dromara.sms4j.solon;

import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.solon.aop.AopAdvice;
import org.dromara.sms4j.solon.config.SmsAutowiredConfig;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear 2023/5/16 created
 */
public class XPluginImpl implements Plugin {
    @Override
    public void start(AopContext context) throws Throwable {
        context.beanMake(SmsAutowiredConfig.class);
        context.beanAroundAdd(Restricted.class, new AopAdvice(context));
    }
}
