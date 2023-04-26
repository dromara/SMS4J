package org.dromara.sms4j.aliyun.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.core.AbstractSmsConfig;


/**
 * AlibabaSmsConfig
 * <p> 阿里巴巴对象建造者
 *
 * @author :Wind
 * 2023/4/8  14:54
 **/
@Slf4j
public class AlibabaSmsConfig extends AbstractSmsConfig<AlibabaSmsImpl, AlibabaConfig> {

    @Override
    public AlibabaSmsImpl init(AlibabaConfig config) {
        return new AlibabaSmsImpl(
                config,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
    }

    /**
     * refresh
     * <p> 刷新对象
     *
     * @author :Wind
     */
    public AlibabaSmsImpl refresh(AlibabaConfig alibabaConfig) {
        return new AlibabaSmsImpl(
                alibabaConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
    }

    @Override
    protected SupplierType type() {
        return SupplierType.ALIBABA;
    }


}
