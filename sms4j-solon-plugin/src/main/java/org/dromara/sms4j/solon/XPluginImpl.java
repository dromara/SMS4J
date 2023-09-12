package org.dromara.sms4j.solon;

import org.dromara.sms4j.solon.config.SmsAutowiredConfig;
import org.dromara.sms4j.solon.config.SupplierConfig;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear 2023/5/16 created
 */
public class XPluginImpl implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanMake(SmsAutowiredConfig.class);
        context.beanMake(SupplierConfig.class);
    }
}
