package org.dromara.oa.core.weTalk.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.core.provider.config.OaBaseConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeTalkConfig extends OaBaseConfig {

    private final String requestUrl = OaType.WE_TALK.getUrl();

    @Override
    public String getSupplier() {
        return OaType.WE_TALK.getType();
    }
}