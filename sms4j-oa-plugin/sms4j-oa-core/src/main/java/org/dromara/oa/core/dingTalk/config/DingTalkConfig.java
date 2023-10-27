package org.dromara.oa.core.dingTalk.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.core.provider.config.OaBaseConfig;


/**
 * @author dongfeng
 * @date 2023-10-19 13:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkConfig extends OaBaseConfig {
    private final String requestUrl = OaType.DINGTALK.getUrl();

    @Override
    public String getSupplier() {
        return OaType.DINGTALK.getType();
    }
}
