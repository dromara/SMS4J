package kim.wind.sms.comm.enumerate;

/**
 * SupplierType
 * <p> 短信供应商枚举
 * @author :Wind
 * 2023/4/7  20:55
 **/
public enum SupplierType {
    /** 阿里云*/
    ALIBABA("alibaba"),
    /** 华为云*/
    HUAWEI("huawei"),
    /** 云片*/
    YUNPIAN("yunpian"),
    /** 腾讯云*/
    TENCENT("tencent"),
    /** 合一短信*/
    UNI_SMS("unisms")
    ;


    private final String name;

    SupplierType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
