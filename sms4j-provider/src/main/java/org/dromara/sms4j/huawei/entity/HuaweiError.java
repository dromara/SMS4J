package org.dromara.sms4j.huawei.entity;

/**
 * HuaweiError
 * <p> 华为官方状态码枚举
 *
 * @author :Wind
 * 2023/3/31  22:11
 **/
public enum HuaweiError {
    E000000("000000","短信平台处理请求成功"),
    E200015("E200015","待发送短信数量太大"),
    E200028("E200028","模板变量校验失败"),
    E200029("E200029","E200029"),
    E200030("E200030","模板未激活"),
    E200031("E200031","协议校验失败"),
    E200033("E200033","模板类型不正确"),
    E200041("E200041","同一短信内容接收号码重复")
    ;

    private final String value;

    private final String code;

    HuaweiError(String code, String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
    public static String getValue(String code){
       switch (code){
           case "000000":
               return E000000.getValue();
           case "E200015":
               return E200015.getValue();
           case "E200028":
               return E200028.getValue();
           case "E200029":
               return E200029.getValue();
           case "E200030":
               return E200030.getValue();
           case "E200031":
               return E200031.getValue();
           case "E200033":
               return E200033.getValue();
           case "E200041":
               return E200041.getValue();
       }
       return "服务异常，请查看官方异常码，异常码："+code;
    }
}
