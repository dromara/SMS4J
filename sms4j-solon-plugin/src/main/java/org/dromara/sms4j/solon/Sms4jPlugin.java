package org.dromara.sms4j.solon;

import org.dromara.sms4j.solon.config.SmsMainConfigure;
import org.dromara.sms4j.solon.config.SupplierConfigure;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear 2023/5/16 created
 */
public class Sms4jPlugin implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanMake(SmsMainConfigure.class);
        context.beanMake(SupplierConfigure.class);
    }
}
