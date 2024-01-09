package org.dromara.oa.core.byteTalk.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.oa.comm.content.OaContent;
import org.dromara.oa.core.byteTalk.service.ByteTalkOaImpl;
import org.dromara.oa.core.provider.factory.OaAbstractProviderFactory;

/**
 * 飞书通知对象建造
 * @author dongfeng
 * 2023-10-22 21:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteTalkFactory extends OaAbstractProviderFactory<ByteTalkOaImpl, ByteTalkConfig> {
    private static final ByteTalkFactory INSTANCE = new ByteTalkFactory();

    /**
     * 建造一个飞书通知对象实现
     */
    @Override
    public ByteTalkOaImpl createSmsOa(ByteTalkConfig byteTalkConfig) {
        return new ByteTalkOaImpl(byteTalkConfig);
    }

    @Override
    public String getSupplier() {
        return OaContent.BYTETALK;
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static ByteTalkFactory instance() {
        return INSTANCE;
    }

}
