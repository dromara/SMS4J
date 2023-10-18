package org.dromara.oa.core.service;


import lombok.Data;
import org.dromara.oa.comm.config.OaConfig;

@Data
public class OaBuild {

    private String OaType;

    private OaConfig config;

    public OaBuild(OaConfig config) {
        this.config = config;
        this.OaType = getOaType();
    }

    public static SenderImpl build(OaConfig config)  {
        return SenderImpl.NewSender(new OaBuild(config));
    }
}
