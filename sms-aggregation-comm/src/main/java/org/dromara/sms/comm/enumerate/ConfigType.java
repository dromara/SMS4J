package org.dromara.sms.comm.enumerate;

/**
 * ConfigType
 * <p>配置文件类型
 * @author :Wind
 * 2023/4/5  19:08
 **/
public enum ConfigType {
    /** 配置文件*/
    CONFIG_FILE("configFile"),

    /** 数据库配置*/
    SQL_CONFIG("sqlConfig"),

    /** nacos配置*/
    NACOS_CONFIG("nacosConfig");

    private final String name;

    ConfigType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
