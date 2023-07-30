package org.dromara.sms4j.unisms.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.unisms.service.UniSmsImpl;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UniConfig extends BaseConfig {

    /**
     * 是否为简易模式
     */
    @Builder.Default
    private Boolean isSimple = true;

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return UniSmsImpl.SUPPLIER;
    }

}
