package kim.wind.sms.huawei.constant;


/**
 * Constant
 * <p> 华为云短信应用常量
 *
 * @author :Wind
 * 2023/3/31  19:33
 **/
public abstract class Constant {
    /**
     * 用于格式化鉴权头域,给"Authorization"参数赋值
     */
    public static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";
    /**
     * 用于格式化鉴权头域,给"X-WSSE"参数赋值
     */
    public static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    /**
     * 访问URI
     */
    public static final String REQUEST_URL = "/sms/batchSendSms/v1";
    /**
     * Content-Type
     */
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    /**
     * 华为云规定 java时间格式
     */
    public static final String JAVA_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Constant() {
    }
}
