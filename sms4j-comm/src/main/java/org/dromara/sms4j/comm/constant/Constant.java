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
    public static final String FROM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * 华为云规定 java时间格式
     */
    public static final String HUAWEI_JAVA_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /** 云片短信国内短信请求地址*/
    public static final String YUNPIAN_URL = "https://sms.yunpian.com/v2";


    private Constant() {
    }
}
