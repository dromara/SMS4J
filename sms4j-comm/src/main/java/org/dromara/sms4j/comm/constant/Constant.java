package org.dromara.sms4j.comm.constant;

/**
 * Constant
 * <p> 短信应用常量
 *
 * @author :Wind
 * 2023/3/31  19:33
 **/
public abstract class Constant {
    /**
     * 项目版本号
     */
    public static final String VERSION = "V 3.3.1";

    /**
     * 用于格式化鉴权头域,给"Authorization"参数赋值
     */
    public static final String HUAWEI_AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    /**
     * 用于格式化鉴权头域,给"X-WSSE"参数赋值
     */
    public static final String HUAWEI_WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";

    /**
     * 华为云国内短信访问URI
     */
    public static final String HUAWEI_REQUEST_URL = "/sms/batchSendSms/v1";

    /**
     * Content-Type
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * Authorization
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * Accept
     */
    public static final String ACCEPT = "Accept";

    /**
     * x-www-form-urlencoded
     */
    public static final String APPLICATION_FROM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * application/json
     */
    public static final String APPLICATION_JSON = "application/json";

    /**
     * application/json; charset=utf-8
     */
    public static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

    /**
     * 华为云规定 java时间格式
     */
    public static final String HUAWEI_JAVA_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * 云片短信国内短信请求地址
     */
    public static final String YUNPIAN_URL = Constant.HTTPS_PREFIX + "sms.yunpian.com/v2";

    /**
     * https请求前缀
     */
    public static final String HTTPS_PREFIX = "https://";

    /**
     * 供应商配置键名
     */
    public static final String SUPPLIER_KEY = "supplier";

    /**
     * 自定义实现工厂路径
     */
    public static final String FACTORY_PATH = "factory";

    private Constant() {
    }
}
