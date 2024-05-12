package org.dromara.sms4j.qiniu.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.qiniu.service.QiNiuSmsImpl;

/**
 * @author YYM
 * @Date: 2024/1/30 16:06 29
 * @描述: QiNiuFactory
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QiNiuFactory extends AbstractProviderFactory<QiNiuSmsImpl, QiNiuConfig> {

    private static final QiNiuFactory INSTANCE = new QiNiuFactory();


    public static QiNiuFactory instance() {
        return INSTANCE;
    }


    @Override
    public QiNiuSmsImpl createSms(QiNiuConfig qiNiuConfig) {
        return new QiNiuSmsImpl(qiNiuConfig);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.QINIU;
    }
}
