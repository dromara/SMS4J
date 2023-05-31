package org.dromara.sms4j.solon;

import org.dromara.sms4j.solon.config.SmsAutowiredConfig;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear 2023/5/16 created
 */
public class XPluginImpl implements Plugin {
    @Override
    public void start(AopContext context) {
        context.beanMake(SmsAutowiredConfig.class);
        SmsAutowiredConfig.aopContext = context;
    }
}
