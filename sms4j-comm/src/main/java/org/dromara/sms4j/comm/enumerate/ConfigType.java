package org.dromara.sms4j.comm.enumerate;

import lombok.Getter;

/**
 * ConfigType
 * <p>配置文件类型
 * @author :Wind
 * 2023/4/5  19:08
 **/
@Getter
public enum ConfigType {
    /** yaml配置文件 */
    YAML("yaml"),
    /** 接口 */
    INTERFACE("interface");

    private final String name;

    ConfigType(String name) {
        this.name = name;
    }

}
