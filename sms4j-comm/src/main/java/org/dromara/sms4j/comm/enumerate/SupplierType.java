package org.dromara.sms4j.comm.enumerate;

/**
 * SupplierType
 * <p> 短信供应商枚举
 * @author :Wind
 * 2023/4/7  20:55
 **/
public enum SupplierType {
    /** 阿里云*/
    ALIBABA("阿里云短信"),
    /** 华为云*/
    HUAWEI("华为云短信"),
    /** 云片*/
    YUNPIAN("云片短信"),
    /** 腾讯云*/
    TENCENT("腾讯云短信"),
    /** 合一短信*/
    UNI_SMS("合一短信"),
    /** 京东云 */
    JD_CLOUD("京东云短信"),
    /** 容联云 */
    CLOOPEN("容联云短信"),

    /**
     * 亿美软通
     */
    EMAY("亿美软通"),
    /** 天翼云 */
    CTYUN("天翼云短信"),
    ;


    private final String name;

    SupplierType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
