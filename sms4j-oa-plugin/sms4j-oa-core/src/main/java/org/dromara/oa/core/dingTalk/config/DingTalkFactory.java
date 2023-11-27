package org.dromara.oa.core.dingTalk.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.oa.comm.content.OaContent;
import org.dromara.oa.core.dingTalk.service.DingTalkOaImpl;
import org.dromara.oa.core.provider.factory.OaAbstractProviderFactory;

import java.util.concurrent.Executor;

/**
 * @author dongfeng
 * @description 钉钉通知对象建造
 * @date 2023-10-22 21:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DingTalkFactory extends OaAbstractProviderFactory<DingTalkOaImpl, DingTalkConfig> {
    private static final DingTalkFactory INSTANCE = new DingTalkFactory();

    /**
     * 建造一个钉钉通知实现
     */
    @Override
    public DingTalkOaImpl createSmsOa(DingTalkConfig dingTalkConfig) {
        return new DingTalkOaImpl(dingTalkConfig);
    }


    @Override
    public String getSupplier() {
        return OaContent.DINGTALK;
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static DingTalkFactory instance() {
        return INSTANCE;
    }

}
