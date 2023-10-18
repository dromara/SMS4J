package org.dromara.oa.comm.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
@EqualsAndHashCode
public class OaConfig {

    private String OaType;

    private String tokenId;

    private String sign;
}

