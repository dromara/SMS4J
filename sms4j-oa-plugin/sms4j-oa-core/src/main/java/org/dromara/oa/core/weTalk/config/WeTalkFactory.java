package org.dromara.oa.core.weTalk.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.oa.comm.content.OaContent;
import org.dromara.oa.core.provider.factory.OaAbstractProviderFactory;
import org.dromara.oa.core.weTalk.service.WeTalkOaImpl;

/**
 * @author dongfeng
 * @description 微信通知对象建造
 * @date 2023-10-22 21:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeTalkFactory extends OaAbstractProviderFactory<WeTalkOaImpl, WeTalkConfig> {
    private static final WeTalkFactory INSTANCE = new WeTalkFactory();

    /**
     * 建造一个微信通知服务
     */
    @Override
    public WeTalkOaImpl createSmsOa(WeTalkConfig weTalkConfig) {
        return new WeTalkOaImpl(weTalkConfig);
    }

    @Override
    public String getSupplier() {
        return OaContent.WETALK;
    }

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static WeTalkFactory instance() {
        return INSTANCE;
    }

}
