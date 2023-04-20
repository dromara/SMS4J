package org.dromara.sms4j.unisms.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.comm.config.BaseConfig;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UniConfig extends BaseConfig {

    /**
     * 是否为简易模式
     */
    private Boolean isSimple = true;

    /**
     * 模板变量名称
     */
    private String templateName;
}
