package kim.wind.sms.sql.config;


import kim.wind.sms.sql.err.SmsSqlException;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * SmsSqlConfig
 * <p> sql配置信息
 *
 * @author :Wind
 * 2023/4/5  18:28
 **/
@Data
public class SmsSqlConfig {

    /**
     * 连接地址
     */
    private String url;

    /**
     * 驱动
     */
    private String driverClassName;

    /**
     * 账号名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库名
     */
    private String databaseName;

    /**
     * 配置表 表名
     */
    private String tableName;

    /**
     * 启用字段名
     */
    private String startName;

    /**
     * 启用字段标识
     */
    private String isStart;

    /**
     * 配置字段名
     */
    private String configName;

    /** 厂商配置字段名*/
    private String supplierFieldName;

}
