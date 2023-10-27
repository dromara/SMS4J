package org.dromara.oa.core.byteTalk.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.core.provider.config.OaBaseConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class ByteTalkConfig extends OaBaseConfig {

    private final String requestUrl = OaType.BYTETALK.getUrl();

    @Override
    public String getSupplier() {
        return OaType.BYTETALK.getType();
    }
}